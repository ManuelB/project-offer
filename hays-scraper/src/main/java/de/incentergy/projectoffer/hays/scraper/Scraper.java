package de.incentergy.projectoffer.hays.scraper;

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
import javax.jms.Queue;
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
	Queue queue;

	private static Logger log = Logger.getLogger(Scraper.class.getName());

	private static final Pattern END_ID = Pattern.compile("\\d+/\\d+$");
	
	@PostConstruct
	@Schedule(hour = "*")
	public void scrap() {
		String baseUrl = "https://www.hays.de";
		String url = baseUrl+"/jobsuche/stellenangebote-jobs/j/Contracting/3/p/1/?q=&e=false";
		int page = 1;
		do {
			Document doc;
			try {
				doc = Jsoup.connect(url).get();
				log.fine(doc.title());
				Elements projectRows = doc.select(".search__result");
				int i = 0;
				for (Element projectRow : projectRows) {
					try {
						JsonObjectBuilder builder = Json.createObjectBuilder();
						builder.add("publishingOrganization", "Hays AG");
	
						Element titleA = projectRow.select(".search__result__header__a").first();
						if(titleA == null) {
							log.warning("Did not find a title for Hays posting page: "+page+" project "+i);
							continue;
						}
						String title = titleA.text();
						builder.add("title", title);
						String projectUrl = titleA.attr("href");
						builder.add("url", projectUrl);
						
						Matcher m = END_ID.matcher(projectUrl);
						m.find();
						String id = m.group(0);
						
						builder.add("id", "hays-" + id);
						builder.add("publishingOrganizationId", id);
	
						Element location = projectRow.select(".search__result__meta .info-text").first();
	
						builder.add("location", location.text().trim());
	
						builder.add("agencyOrganization", "Hays AG");
	
						LocalDateTime dateTime = LocalDateTime.now();
						
						builder.add("originalPublicationDate", dateTime.toString());
	
						String description = projectRow.select(".search__result__teaser").text();
						builder.add("description", description);
	
						String jsonString = builder.build().toString();
						log.fine(jsonString);
						if (context != null) {
							context.createProducer().send(queue, jsonString);
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
			url = "https://www.hays.de/jobsuche/stellenangebote-jobs/j/Contracting/3/i/Software-EDV-IT-Dienstleistung/D2361F07-89E8-DE11-BAE0-0007E92E2CEA/p/"+page+"/?q=&e=false";
		} while(page < 5);
	}

}
