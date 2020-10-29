package de.incentergy.projectoffer.gulp.scraper;

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
import javax.ws.rs.client.Entity;

@Singleton
@Startup
public class Scraper {

	@Inject
	private JMSContext context;

	@Resource(mappedName = "java:/jms/projectoffer/projectoffer")
	Topic topic;

	private static Logger log = Logger.getLogger(Scraper.class.getName());

	@PostConstruct
	@Schedule(hour = "*")
	public void scrap() {
		Client client = ClientBuilder.newClient();
		// {"query":"","regions":[],"cities":[],"projectTypes":[],"limit":20,"page":0}
		JsonObject projects = client.target("https://www.gulp.de/gulp2/rest/internal/projects/search").request()
				.post(Entity.json(
						"{\"query\":\"\",\"regions\":[],\"cities\":[],\"projectTypes\":[],\"limit\":1000,\"page\":0}"))
				.readEntity(JsonObject.class);
		log.fine(projects.toString());

//		{
//			"totalCount": 571,
//			"projects": [{
//				"id": "d26071600f9a11ebb49d0242ac110003",
//				"idInIndex": "d26071600f9a11ebb49d0242ac110003",
//				"type": "EXTERNAL",
//				"title": "Anforderungsmanagement (Versicherung, Hamburg)",
//				"location": "Hamburg",
//				"description": " – Sie nehmen im Projekt die Rolle des Anforderungsmanagers (m/w/d) u.a. mit folgenden Inhalten wahr. – Sie verantworten die Erhebung, Prüfung und fachliche und organisatorische Abstimmung der Anfragen im Projekt Kontext. – Sie leisten Mitarbeit bei der Erstellung von fachlichen Bedarfsbeschreibungen zu den Anforderungen. – Sie analysieren die Geschäftsprozesse die durch das Projekt migriert werden. – Sie verantworten die Erarbeitung und Empfehlung von erforderlichen Lösungen – Sie führen ...",
//				"companyName": "U.N.P. Software GmbH",
//				"companyLogoUrl": "https://www.gulp.de/firma/img/big/projekt_ohne_logo.png",
//				"url": "https://gulp.de/robi/backend/tracker/url?project_url=https%3A%2F%2Funp.de%2Fanforderungsmanager-m-w-d%2F",
//				"originalPublicationDate": "2020-10-16T14:31:00.000",
//				"startDate": "01.01.2021",
//				"favorite": false,
//				"hidden": false,
//				"memoText": null,
//				"duration": null,
//				"skills": [],
//				"active": null,
//				"projectKey": null
//			}]
//		}

		for (JsonValue projectValue : projects.getJsonArray("projects")) {
			if (!(projectValue instanceof JsonObject)) {
				continue;
			}
			JsonObject projectObject = (JsonObject) projectValue;
			JsonObject projectOfferJson = gulpJsonToProjectOfferJson(projectObject);
			context.createProducer().send(topic, projectOfferJson.toString());
		}
	}

//	{
//		"agencyOrganization": "GULP Information Services GmbH",
//		"contactCity": "Hamburg",
//		"contactCountry": "Deutschland",
//		"contactEmail": "lilith.konzan@gulp.de",
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
//		"url": "https://www.gulp.de/gulp2/g/projekte/agentur/a0w3X00000UIsGXQA1"
//	}
	private JsonObject gulpJsonToProjectOfferJson(JsonObject projectObject) {

		JsonArrayBuilder skills = Json.createArrayBuilder();

		for (JsonValue skill : projectObject.getJsonArray("skills")) {
			skills.add(Json.createObjectBuilder().add("name", skill).build());
		}

		JsonObject jsonObject = Json.createObjectBuilder().add("id", "gulp-" + projectObject.get("id"))
				.add("publishingOrganizationId", projectObject.get("id")).add("title", projectObject.get("title"))
				.add("description", projectObject.get("description")).add("location", projectObject.get("location"))
				.add("publishingOrganization", "GULP Information Services GmbH")
				.add("agencyOrganization", projectObject.get("companyName")).add("url", projectObject.get("url"))
				.add("originalPublicationDate", projectObject.get("originalPublicationDate")).add("skills", skills)
				.build();
		return jsonObject;
	}

}
