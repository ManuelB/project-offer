package de.incentergy.projectoffer.topitservices.scraper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Locale;
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

	SimpleDateFormat sdtF = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY);
	private static final Pattern PROJECT_ID_PATTERN = Pattern.compile(".*(\\d{3}-\\d{4,6}).*");

	@PostConstruct
	@Schedule(hour = "*")
	public void scrap() {
		String baseUrl = "https://www.top-itservices.com";
		String url = baseUrl + "/annoncen/suchbegriff/Freiberuflich/ort/umkreis/20";
		Document doc;
		try {
			doc = Jsoup.connect(url).get();
			log.fine(doc.title());
			Elements projectRows = doc.select("table.table tr");
			int i = 0;
			for (Element projectRow : projectRows) {
				try {
					JsonObjectBuilder builder = Json.createObjectBuilder();
					builder.add("publishingOrganization", "top itservices AG");

					Element titleA = projectRow.select(".forum-title").first();
					if (titleA == null) {
						log.warning("Did not find a title for topitservices posting project " + i);
						continue;
					}
					String title = titleA.text();
					builder.add("title", title);
					String projectUrl = titleA.attr("href");
					builder.add("url", baseUrl + projectUrl);

					Matcher m = PROJECT_ID_PATTERN.matcher(projectUrl);
					m.matches();
					String id = m.group(1);

					builder.add("id", "topitservices-" + id);
					builder.add("publishingOrganizationId", id);

					Element location = projectRow.select("td").get(2);

					builder.add("location", location.text().trim());

					builder.add("agencyOrganization", "top itservices AG");

					Element publishingDate = projectRow.select("td").get(3);

					Date germanDate = sdtF.parse(publishingDate.text().trim());

					LocalDateTime dateTime = LocalDateTime.now().withYear(germanDate.getYear()+1900)
							.withMonth(germanDate.getMonth() + 1).withDayOfMonth(germanDate.getDate());

					builder.add("originalPublicationDate", dateTime.toString());

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
	}

}
