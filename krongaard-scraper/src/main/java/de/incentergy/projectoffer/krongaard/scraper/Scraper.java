package de.incentergy.projectoffer.krongaard.scraper;

import java.text.SimpleDateFormat;
import java.util.Date;
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
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

@Singleton
@Startup
public class Scraper {

	@Inject
	private JMSContext context;

	@Resource(mappedName = "java:/jms/projectoffer/projectoffer")
	Topic topic;

	static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'"); // create new date format

	private static Logger log = Logger.getLogger(Scraper.class.getName());

	@PostConstruct
	@Schedule(hour = "*")
	public void scrap() {
		scrap(new Object[] {});
	}

	public void scrap(Object... entityLoggingFilters) {
		Client client = ClientBuilder.newClient();
		client.register(JsonObjectTextHtmlMessageBodyReader.class);
		if (entityLoggingFilters != null) {
			for (Object entityLoggingFilter : entityLoggingFilters) {
				client.register(entityLoggingFilter);
			}
		}
		// {"query":"","regions":[],"cities":[],"projectTypes":[],"limit":20,"page":0}
		JsonObject projects = client.target("https://www.krongaard.de/jobs/search").request(MediaType.APPLICATION_JSON)
				.post(Entity.json("keyword=&page=1&bundsland=")).readEntity(JsonObject.class);
		log.fine(projects.toString());
//			AmountNeeded: 1
//			Begin: "06/2019"
//			ChangeDate: "/Date(1574787364000+0100)/"
//			ClosingDate: null
//			Comments: ""
//			ComplianceTemplateId: 4
//			ComplianceTestId: null
//			ComplianceTestPoints: null
//			ComplianceTestResult: null
//			CountryCode: "DE"
//			CreatedBy: null
//			CreationDate: "/Date(1549964268000+0100)/"
//			CustomFields: [{ChangeDate: null, CreatedBy: null, CreationDate: null, Id: 106, LastEditor: null,…},…]
//			DateDue: "/Date(-2209165200000+0100)/"
//			DisplayJobMarket: true
//			End: "6 MM++"
//			HasApplications: null
//			Id: 12984
//			JobDescription: "Für unseren Kunden im Norden suchen wir ab sofort einen Aktuar für Lebensversicherungen (m/w/d). ...
//			↵"
//			JobDescriptionHTML: "
//			↵<p lang="de-DE" style="margin-top:0pt;margin-bottom:0pt;"><span style="font-size:10pt;">F&uuml;r unseren Kunden im Norden suchen wir ab sofort einen Aktuar f&uuml;r Lebensversicherungen (m/w/d).</span></p>
//			↵<p lang="de-DE" style="margin-top:0pt;margin-bottom:0pt;"><span style="font-size:10pt;">&nbsp;</span></p>
//			↵<p lang="de-DE" style="margin-top:0pt;margin-bottom:0pt;"><span style="font-size:10pt;font-weight:bold;">Ihre Aufgaben:</span></p>
//			↵<p lang="de-DE" style="margin-top:0pt;margin-bottom:0pt;"><span style="font-size:10pt;">- Aktuarielle Testdurchf&uuml;hrung im Rahmen einer Migration</span></p>
//			↵<p lang="de-DE" style="margin-top:0pt;margin-bottom:0pt;"><span style="font-size:10pt;">- Durchf&uuml;hrung von Wertetests zur Berechnung des Deckungskapitals</span></p>
//			↵<p lang="de-DE" style="margin-top:0pt;margin-bottom:0pt;"><span style="font-size:10pt;">- Mathematische Pr&uuml;fung/Analyse von Bestandsvertr&auml;gen</span></p>
//			↵<p lang="de-DE" style="margin-top:0pt;margin-bottom:0pt;"><span style="font-size:10pt;">- Durchf&uuml;hrung von aktuariellen Tests von alten Gesch&auml;ftsvorf&auml;llen</span></p>
//			↵<p lang="de-DE" style="margin-top:0pt;margin-bottom:0pt;"><span style="font-size:10pt;">&nbsp;</span></p>
//			↵<p lang="de-DE" style="margin-top:0pt;margin-bottom:0pt;"><span style="font-size:10pt;font-weight:bold;">Ihre Qualifikation:</span></p>
//			↵<p lang="de-DE" style="margin-top:0pt;margin-bottom:0pt;"><span style="font-size:10pt;">- Umfassende Erfahrungen als Aktuar im Bereich Leben</span></p>
//			↵<p lang="de-DE" style="margin-top:0pt;margin-bottom:0pt;"><span style="font-size:10pt;">- Sehr gute Erfahrungen im Bereich Bestandsf&uuml;hrung</span></p>
//			↵<p lang="de-DE" style="margin-top:0pt;margin-bottom:0pt;"><span style="font-size:10pt;">- Gute Erfahrungen im Testing</span></p>
//			↵<p lang="de-DE" style="margin-top:0pt;margin-bottom:0pt;"><span style="font-size:10pt;">- Versicherungsmathematischer Background</span></p>
//			↵"
//			JobDescriptionRTF: "{}"
//			JobLocation: "Nordrhein-Westfalen"
//			JobTitle: "Aktuar im Lebensversicherungsumfeld (m/w/d)"
//			JobTitleInternal: "Leben-Aktuar (m/w/d)"
//			Language: 0
//			LastEditor: null
//			MainClientContact: 129052
//			MustHaveSkills: "Branche: Versicherung, fachl. Spezialist / Berater, Versicherung: Aktuar, Mathematiker"
//			NiceToHaveSkills: ""
//			OrgProfile: null
//			OtherClientContacts: []
//			OtherRemarks: "SAF"
//			OutDT: "12/2019"
//			PriceNote: null
//			Priority: null
//			Profile: "IT"
//			Stage: null
//			Status: 4
//			StatusC: null
//			Suggestions: ""
//			Type: 1
//			TypeStr: ["Freiberuflich", "Contractor"]
//			User: "NHM"
//			Volume: null
//			VolumeCurrency: null
//			VolumeDays: null
//			VolumeHours: null
		for (JsonValue projectValue : projects.getJsonArray("data")) {
			if (!(projectValue instanceof JsonObject)) {
				continue;
			}
			JsonObject projectObject = (JsonObject) projectValue;
			JsonObject projectOfferJson = krongaardJsonToProjectOfferJson(projectObject);
			if(context != null) {
				context.createProducer().send(topic, projectOfferJson.toString());
			}
		}
	}

//	{
//		"agencyOrganization": "GULP Information Services GmbH",
//		"contactCity": "Hamburg",
//		"contactCountry": "Deutschland",
//		"contactEmail": "lilith.konzan@krongaard.de",
//		"contactFirstname": "Lilith",
//		"contactLastname": "Konzan",
//		"contactPhone": "+49 40 468987-834",
//		"contactStreet": "Amsinckstraße 34",
//		"contactZip": "20097",
//		"description": "Unser Kunde aus der Energiewirtschaft sucht eine / n Senior Software Engineer für den Bereich Java Middleware/ Backend. Der Einsatz soll Anfang November starten und vorerst bis Ende des Jahres laufen. Eine Vollauslastung ist gewünscht, welche vor Ort in Hamburg geleistet werden soll.\n\n \n\nFolgende Aufgabe erwartet Sie:\n\nMiddleware Entwicklung in internationalem, agilen Projektteam in komplexer Architektur mit diversen SAP Systemen und Eigenentwicklungen als Randsystemen sowie Frontends und Workflowsystem als Projektapplikationen",
//		"htmlDescription": "<span _ngcontent-uor-c201=\"\" class=\"form-input\"><p>Unser Kunde aus der Energiewirtschaft sucht eine / n Senior Software Engineer für den Bereich Java Middleware/ Backend. Der Einsatz soll Anfang November starten und vorerst bis Ende des Jahres laufen. Eine Vollauslastung ist gewünscht, welche vor Ort in Hamburg geleistet werden soll.</p> \n<p>&nbsp;</p> \n<p>Folgende Aufgabe erwartet Sie:</p> \n<p>Middleware Entwicklung in internationalem, agilen Projektteam in komplexer Architektur mit diversen SAP Systemen und Eigenentwicklungen als Randsystemen sowie Frontends und Workflowsystem als Projektapplikationen</p></span>",
//		"id": "11517d47-6b8b-432d-bee9-2a33dabe2ad1",
//		"location": "20095 Hamburg",
//		"originalPublicationDate": "2020-10-16T10:33:00+02:00[Europe/Berlin]",
//		"publishingOrganization": "GULP Information Services GmbH",
//		"publishingOrganizationId": "a0w3X00000UIsGXQA1",
//		"skills": [{
//			"id": "b1f6591b-9695-4abc-947e-d867a8ff9a92",
//			"name": "Agile Softwareentwicklung (TDD / Code Review / Pair Programming)"
//		}, {
//			"id": "36dc05e9-463f-4d74-8fce-4c11aa992fdf",
//			"name": "Clean Code"
//		}],
//		"title": "Senior Software Engineer Java Middleware/Backend (m/w/d)",
//		"url": "https://www.krongaard.de/krongaard2/g/projekte/agentur/a0w3X00000UIsGXQA1"
//	}
	private JsonObject krongaardJsonToProjectOfferJson(JsonObject projectObject) {

		JsonArrayBuilder skills = Json.createArrayBuilder();

		for (String skill : projectObject.getString("MustHaveSkills").split(",")) {
			skills.add(Json.createObjectBuilder().add("name", skill).build());
		}

		String creationDate = toIso8601String(projectObject.getString("CreationDate"));

		JsonObject jsonObject = Json.createObjectBuilder().add("id", "krongaard-" + projectObject.get("Id"))
				.add("publishingOrganizationId", projectObject.get("Id")).add("title", projectObject.get("JobTitle"))
				.add("description", projectObject.get("JobDescription"))
				.add("htmlDescription", projectObject.get("JobDescriptionHTML"))
				.add("location", projectObject.get("JobLocation")).add("publishingOrganization", "Krongaard AG")
				.add("agencyOrganization", "Krongaard AG")
				.add("url", "https://www.krongaard.de/projektmarkt/Job?job_id=" + projectObject.get("Id"))
				.add("originalPublicationDate", creationDate).add("skills", skills).build();
		return jsonObject;
	}

	static String toIso8601String(String s) {
		if (s != null) {
			long timestamp = Long.parseLong(s.substring(6, 16));
			Date date = new Date(timestamp * 1000);
			return format.format(date);
		}
		return format.format(new Date());
	}

}
