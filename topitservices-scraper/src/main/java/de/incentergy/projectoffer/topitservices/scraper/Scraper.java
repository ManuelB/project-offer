package de.incentergy.projectoffer.topitservices.scraper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

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

	@PostConstruct
	@Schedule(hour = "*")
	public void scrap() {
		String baseUrl = "https://www.top-itservices.com";
		String url = baseUrl + "/annoncen/";
		Document doc;
		try {
			doc = Jsoup.connect(url).data("tx_topitannoncen_annoncenlisting[__referrer][@extension]", "Topitannoncen")
					.data("tx_topitannoncen_annoncenlisting[__referrer][@vendor]", "Topit")
					.data("tx_topitannoncen_annoncenlisting[__referrer][@controller]", "Annonce")
					.data("tx_topitannoncen_annoncenlisting[__referrer][@action]", "list")
					.data("tx_topitannoncen_annoncenlisting[__referrer][arguments]",
							"YTo0OntzOjY6ImFjdGlvbiI7czo0OiJsaXN0IjtzOjEwOiJjb250cm9sbGVyIjtzOjc6IkFubm9uY2UiO3M6MTA6InNlYXJjaFdvcmQiO3M6MDoiIjtzOjk6InNlYXJjaExvYyI7czowOiIiO30=f49e65e34182ec24573ff69c66b9240209f9d848")
					.data("tx_topitannoncen_annoncenlisting[__referrer][@request]",
							"a:4:{s:10:\"@extension\";s:13:\"Topitannoncen\";s:11:\"@controller\";s:7:\"Annonce\";s:7:\"@action\";s:4:\"list\";s:7:\"@vendor\";s:5:\"Topit\";}d9a3add027efc8636d05d77114268bd23dfc01c8")
					.data("tx_topitannoncen_annoncenlisting[__trustedProperties]",
							"a:7:{s:10:\"searchWord\";i:1;s:9:\"searchLoc\";i:1;s:14:\"annonceUmkreis\";i:1;s:11:\"annonceType\";i:1;s:11:\"annonceLogo\";i:1;s:7:\"filter1\";i:1;s:12:\"itemsperpage\";i:1;}86764384a2e4c2b3e456044fbd1b123dd088af0d")
					.data("tx_topitannoncen_annoncenlisting[searchWord]", "")
					.data("tx_topitannoncen_annoncenlisting[searchLoc]", "")
					.data("tx_topitannoncen_annoncenlisting[annonceUmkreis]", "99")
					.data("tx_topitannoncen_annoncenlisting[annonceType]", "Freiberuflich")
					.data("tx_topitannoncen_annoncenlisting[annonceLogo]", "99")
					.data("tx_topitannoncen_annoncenlisting[filter1]", "99")
					.data("tx_topitannoncen_annoncenlisting[itemsperpage]", "100").post();
			log.fine(doc.title());
			Elements projectRows = doc.select(".row.annoncenTitle");
			int i = 0;
			for (Element projectRow : projectRows) {
				try {
					JsonObjectBuilder builder = Json.createObjectBuilder();
					builder.add("publishingOrganization", "top itservices AG");

					Element titleA = projectRow.select(".annonceLinkAction").first();
					if (titleA == null) {
						log.warning("Did not find a title for topitservices posting project " + i);
						continue;
					}
					String title = titleA.text();
					builder.add("title", title);
					String projectUrl = titleA.attr("href");
					builder.add("url", baseUrl + projectUrl);

					String id = projectRow.attr("id");

					builder.add("id", "topitservices-" + id);
					builder.add("publishingOrganizationId", id);

					Element location = projectRow.select("div").get(4).selectFirst(".hidden-xs-down");

					builder.add("location", location.text().trim());

					builder.add("agencyOrganization", "top itservices AG");

					Element publishingDate = projectRow.select("div").get(8);

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
