package de.incentergy.projectoffer.redsapsolutions.scraper;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.Topic;
import javax.json.Json;
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

	private static final Pattern SEIT_DIGIT_TAGEN = Pattern.compile("seit (\\d+) Tagen");

	private static final Pattern ETWA_STUNDEN_TAGEN = Pattern.compile("etwa (\\d+) Stunden?");

	@PostConstruct
	@Schedule(hour = "*")
	public void scrap() {
		String baseUrl = "https://www.redsapsolutions.de";
		String url = baseUrl + "/job-search";
		int page = 1;
		do {
			Document doc;
			try {
				doc = Jsoup.connect(url).get();
				log.fine(doc.title());
				Elements projectRows = doc.select(".job-result-item");
				int i = 0;
				for (Element projectRow : projectRows) {
					try {
						JsonObjectBuilder builder = Json.createObjectBuilder();
						builder.add("publishingOrganization", "RED SAP Solutions");

						Element titleA = projectRow.select(".job-title a").first();
						String title = titleA.text();
						builder.add("title", title);

						String projectId = projectRow.select(".job-save-job-link").attr("id").replace("job-save-job-",
								"");

						builder.add("id", "redsapsolutions-" + projectId);
						builder.add("publishingOrganizationId", projectId);
						String projectUrl = titleA.attr("href");
						builder.add("url", baseUrl + projectUrl);

						Element detailsCountry = projectRow.select(".results-job-location").first();

						builder.add("location", detailsCountry.text().trim());

						Elements created = projectRow.select(".results-posted-at");
						// e.g. seit 5 Tagen
						String relativeTime = created.text().trim();
						
						Long sinceDays = 0l;
						Matcher m = SEIT_DIGIT_TAGEN.matcher(relativeTime);
						if(m.find()) {
							sinceDays = Long.parseLong(m.group(1));
						}
						
						Long hoursDays = 0l;
						m = ETWA_STUNDEN_TAGEN.matcher(relativeTime);
						if(m.find()) {
							hoursDays = Long.parseLong(m.group(1));
						}
						LocalDateTime dateTime = LocalDateTime.now().minusDays(sinceDays).minusHours(hoursDays);

						builder.add("originalPublicationDate", dateTime.toString());

						String description = projectRow.select(".job-description").text();
						builder.add("description", description);

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
			url = "https://www.redsapsolutions.de/job-search/page/" + page;
		} while (page < 5);
	}

}
