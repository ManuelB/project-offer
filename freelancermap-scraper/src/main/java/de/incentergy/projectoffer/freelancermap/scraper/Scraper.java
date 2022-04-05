package de.incentergy.projectoffer.freelancermap.scraper;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.Topic;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@Singleton
@Startup
public class Scraper {

	@Inject
	private JMSContext context;

	@Resource(mappedName = "java:/jms/projectoffer/projectoffer")
	Topic topic;

	private static Logger log = Logger.getLogger(Scraper.class.getName());

	private final static DateTimeFormatter DATE_TIME_PATTERN = DateTimeFormatter.ofPattern("dd.MM.yyyy '/' HH:mm");

	private static final Pattern PROJECT_ID_PATTERN = Pattern.compile(".*(\\d{7,10}).*");
	
	@PostConstruct
	@Schedule(hour = "*")
	public void scrap() {
		String baseUrl = "https://www.freelancermap.de";
		String url = baseUrl+"/projektboerse.html";
		int page = 1;
		do {
			Document doc;
			try {
				doc = Jsoup.connect(url).get();
				log.fine(doc.title());
				Elements projectRows = doc.select(".project-container");
				int i = 0;
				for (Element projectRow : projectRows) {
					try {
						JsonObjectBuilder builder = Json.createObjectBuilder();
						builder.add("publishingOrganization", "freelancermap GmbH");
	
						Element titleA = projectRow.select("h3.body a.project-title").first();
						String title = titleA.text();
						builder.add("title", title);
						String projectUrl = titleA.attr("href");
						builder.add("url", baseUrl+projectUrl);
						Matcher m = PROJECT_ID_PATTERN.matcher(projectUrl);
						m.matches();
						String id = m.group(1);
						builder.add("id", "freelancermap-" + id);
						builder.add("publishingOrganizationId", id);
	
						Elements detailsCountry = projectRow.select(".project-location .city");
	
						builder.add("location", detailsCountry.text().trim());
	
						String company = projectRow.select(".company a").text();
						builder.add("agencyOrganization", company);
	
						Elements created = projectRow.select(".created-date");
						String strip = created.text().strip();
						LocalDateTime dateTime = LocalDateTime.now();
						if(strip.length() > 16) {
							String dateString = strip.substring(16);
							dateTime = LocalDateTime.parse(dateString, DATE_TIME_PATTERN);
						}
						
						builder.add("originalPublicationDate", dateTime.toString());
	
						String description = projectRow.select(".description").text();
						builder.add("description", description);
	
						List<String> skills = projectRow.select(".keywords-container a").stream().map(e -> e.text())
								.collect(Collectors.toList());
	
						JsonArrayBuilder skillArray = Json.createArrayBuilder();
						for (String s : skills) {
							skillArray.add(Json.createObjectBuilder().add("name", s).build());
						}
	
						builder.add("skills", skillArray);
						String jsonString = builder.build().toString();
						log.fine(jsonString);
						if (context != null) {
							context.createProducer().send(topic, jsonString);
						}
					} catch (Exception e) {
						log.log(Level.WARNING, "Could not parse project row: " + i, e);
					}
					i++;
				}
			} catch (IOException e) {
				log.log(Level.WARNING, "Could not access url", e);
			}
			page++;
			url = "https://www.freelancermap.de/projektboerse.html?contractTypes=&endcustomer=20&created=&excludeDachProjects=0&partner=&poster=&lastRun=&currentPlatform=1&query=&queryParts=&continents=&countries%5B0%5D=1&states=&location=&radius=&city=&categories=&subCategories=&sort=1&pagenr="+page;
		} while(page < 5);
	}

}
