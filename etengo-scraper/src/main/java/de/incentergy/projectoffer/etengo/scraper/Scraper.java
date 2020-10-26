package de.incentergy.projectoffer.etengo.scraper;

import java.util.logging.Logger;

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
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

@Singleton
@Startup
public class Scraper {

	@Inject
	private JMSContext context;

	@Resource(mappedName = "java:/jms/projectoffer/projectoffer")
	Queue queue;

	private static Logger log = Logger.getLogger(Scraper.class.getName());

	@PostConstruct
	@Schedule(hour = "*")
	public void scrap() {
		Client client = ClientBuilder.newClient().register(JsonObjectTextPlainMessageBodyReader.class);
		JsonObject projects = client.target("https://www.etengo.de/index.php").queryParam("type", "3732")
				.queryParam("tx_etengowebsite_pi3[action]", "solr").queryParam("tx_etengowebsite_pi3[query]", "")
				.request().get().readEntity(JsonObject.class);
		log.fine(projects.toString());

//	{
//		"responseHeader": {
//			"status": 0,
//			"QTime": 0,
//			"params": {
//				"q": "type:jobs",
//				"qf": "skills_stringM title^2 content",
//				"rows": "400"
//			}
//		},
//		"response": {
//			"numFound": 85,
//			"start": 0,
//			"docs": [{
//				"id": "71d488e3c1bd39a08e01aaf8b055084dfb09e054\/tx_etengowebsite_domain_model_job\/57637",
//				"type": "jobs",
//				"site": "www.etengo.de",
//				"siteHash": "71d488e3c1bd39a08e01aaf8b055084dfb09e054",
//				"uid": 57637,
//				"pid": 44,
//				"variantId": "6e49394f277e6989ce8be5fcbc4fff449e86f6d7\/tx_etengowebsite_domain_model_job\/57637",
//				"created": "2020-10-26T06:03:01Z",
//				"changed": "2020-10-26T12:38:02Z",
//				"access": ["r:0"],
//				"jobId_stringS": "CA-80207",
//				"title": "MS SQL-Server Berater (w\/m\/d)",
//				"titleExact": "MS SQL-Server Berater (w\/m\/d)",
//				"url": "index.php?id=30&tx_etengowebsite_pi4%5Baction%5D=detail&tx_etengowebsite_pi4%5Bjob%5D=57637&L=0%22",
//				"category": ["Freelancing"],
//				"skills_stringM": ["C#", "Dokumentation", "Test", "SSRS", "Konzeption", "Anforderungsanalyse", "MS SQL Server 2016", "Integration", "MS SQL Server", "Datenbankmanagementsystem", "SQL", "ICAAP", "Datenmodellierung", "JIRA", "SSIS", "Power BI", "T-SQL"],
//				"branch_stringS": "Finanzwirtschaft und Bankwesen",
//				"label_stringS": "Freelancing",
//				"duration_stringS": "25",
//				"location_stringS": "DE 40XXX",
//				"plz_stringS": "DE 40",
//				"begin_stringS": "ab 01.12.2020",
//				"timeago_stringS": "3 Tagen",
//				"abstract": "F\u00fcr unseren Kunden im Bankensektor suchen wir zum 01.12.2020 zwei MS SQL-Server Berater (w\/m\/d) zur Unterst\u00fctzung im Projekt.\nPROJEKTHINTERGRUND: Im Rahmen des Projekts \u201aWeiterentwicklung ICAAP\/ILAAP\u2018 wird eine Datenbank im MS SQL-Server 2016 neu konzipiert und entwickelt, in dem der Fachbereich Risikocontrolling verschiedene Stressszenarien simulieren und f\u00fcr Auswertungen aufbereiten kann.Es sind diverse fachliche Anforderungen im Umfeld der Stressszenarien zu implementieren. Dies beinhaltet neben der Konfiguration der Stressszenarien auch die Berechnung zus\u00e4tzlicher Kennzahlen im Stressmodul. Zudem muss die Stresstestinfrastruktur kompatibel zu weiteren Banksystemen im\nSAP-Umfeld und im MS-SQL Server Umfeld gemacht werden. Hierzu m\u00fcssen Schnittstellen zu diesen Systemen neu angelegt werden.\nAUFGABEN:\n\nIT-Konzeption auf Basis eines bestehenden Fachkonzepts und ggfs. weitere Anforderungsanalyse\nDatenmodellierung (Tabellen, Sichten usw.) im MS SQL-Server 2016\nProgrammierung von Stored Procedures, Funktionen im MS SQL-Server 2016\nProgrammierung eines Frontends mittels C#\nErstellung von SSIS-Packages\nErzeugung von Reports mit SSRS oder Power BI\n-Test und Dokumentation der Umsetzung\n\nQUALIFIKATIONEN:\n\nSehr gute Kenntnisse in der Programmierung mittels SQL\/T-SQL\nSehr gute Kenntnisse im Bereich des Berichtswesens des MS SQL-Server 2016 (SSRS) sowie in den Integration Services (SSIS)\nSehr gute Kenntnisse in der Programmierung mittels C#\nKenntnisse im Themenfeld von Risikotragf\u00e4higkeitskonzepten und den in den ICAAP- und ILAAP-Leitf\u00e4den formulierten Anforderungen sind w\u00fcnschenswert\nErfahrungen mit der In-Memory-Technologie im MS SQL Server sind w\u00fcnschenswert\nBranchenerfahrung im Bankbereich\nJIRA- und Automic-Kenntnisse von Vorteil\n",
//				"content": "F\u00fcr unseren Kunden im Bankensektor suchen wir zum 01.12.2020 zwei MS SQL-Server Berater (w\/m\/d) zur Unterst\u00fctzung im Projekt.\nPROJEKTHINTERGRUND: Im Rahmen des Projekts \u201aWeiterentwicklung ICAAP\/ILAAP\u2018 wird eine Datenbank im MS SQL-Server 2016 neu konzipiert und entwickelt, in dem der Fachbereich Risikocontrolling verschiedene Stressszenarien simulieren und f\u00fcr Auswertungen aufbereiten kann.Es sind diverse fachliche Anforderungen im Umfeld der Stressszenarien zu implementieren. Dies beinhaltet neben der Konfiguration der Stressszenarien auch die Berechnung zus\u00e4tzlicher Kennzahlen im Stressmodul. Zudem muss die Stresstestinfrastruktur kompatibel zu weiteren Banksystemen im\nSAP-Umfeld und im MS-SQL Server Umfeld gemacht werden. Hierzu m\u00fcssen Schnittstellen zu diesen Systemen neu angelegt werden.\nAUFGABEN:\n\nIT-Konzeption auf Basis eines bestehenden Fachkonzepts und ggfs. weitere Anforderungsanalyse\nDatenmodellierung (Tabellen, Sichten usw.) im MS SQL-Server 2016\nProgrammierung von Stored Procedures, Funktionen im MS SQL-Server 2016\nProgrammierung eines Frontends mittels C#\nErstellung von SSIS-Packages\nErzeugung von Reports mit SSRS oder Power BI\n-Test und Dokumentation der Umsetzung\n\nQUALIFIKATIONEN:\n\nSehr gute Kenntnisse in der Programmierung mittels SQL\/T-SQL\nSehr gute Kenntnisse im Bereich des Berichtswesens des MS SQL-Server 2016 (SSRS) sowie in den Integration Services (SSIS)\nSehr gute Kenntnisse in der Programmierung mittels C#\nKenntnisse im Themenfeld von Risikotragf\u00e4higkeitskonzepten und den in den ICAAP- und ILAAP-Leitf\u00e4den formulierten Anforderungen sind w\u00fcnschenswert\nErfahrungen mit der In-Memory-Technologie im MS SQL Server sind w\u00fcnschenswert\nBranchenerfahrung im Bankbereich\nJIRA- und Automic-Kenntnisse von Vorteil\n",
//				"contentExact": "F\u00fcr unseren Kunden im Bankensektor suchen wir zum 01.12.2020 zwei MS SQL-Server Berater (w\/m\/d) zur Unterst\u00fctzung im Projekt.\nPROJEKTHINTERGRUND: Im Rahmen des Projekts \u201aWeiterentwicklung ICAAP\/ILAAP\u2018 wird eine Datenbank im MS SQL-Server 2016 neu konzipiert und entwickelt, in dem der Fachbereich Risikocontrolling verschiedene Stressszenarien simulieren und f\u00fcr Auswertungen aufbereiten kann.Es sind diverse fachliche Anforderungen im Umfeld der Stressszenarien zu implementieren. Dies beinhaltet neben der Konfiguration der Stressszenarien auch die Berechnung zus\u00e4tzlicher Kennzahlen im Stressmodul. Zudem muss die Stresstestinfrastruktur kompatibel zu weiteren Banksystemen im\nSAP-Umfeld und im MS-SQL Server Umfeld gemacht werden. Hierzu m\u00fcssen Schnittstellen zu diesen Systemen neu angelegt werden.\nAUFGABEN:\n\nIT-Konzeption auf Basis eines bestehenden Fachkonzepts und ggfs. weitere Anforderungsanalyse\nDatenmodellierung (Tabellen, Sichten usw.) im MS SQL-Server 2016\nProgrammierung von Stored Procedures, Funktionen im MS SQL-Server 2016\nProgrammierung eines Frontends mittels C#\nErstellung von SSIS-Packages\nErzeugung von Reports mit SSRS oder Power BI\n-Test und Dokumentation der Umsetzung\n\nQUALIFIKATIONEN:\n\nSehr gute Kenntnisse in der Programmierung mittels SQL\/T-SQL\nSehr gute Kenntnisse im Bereich des Berichtswesens des MS SQL-Server 2016 (SSRS) sowie in den Integration Services (SSIS)\nSehr gute Kenntnisse in der Programmierung mittels C#\nKenntnisse im Themenfeld von Risikotragf\u00e4higkeitskonzepten und den in den ICAAP- und ILAAP-Leitf\u00e4den formulierten Anforderungen sind w\u00fcnschenswert\nErfahrungen mit der In-Memory-Technologie im MS SQL Server sind w\u00fcnschenswert\nBranchenerfahrung im Bankbereich\nJIRA- und Automic-Kenntnisse von Vorteil\n",
//				"teaser": "F\u00fcr unseren Kunden im Bankensektor suchen wir zum 01.12.2020 zwei MS SQL-Server Berater (w\/m\/d) zur Unterst\u00fctzung im Projekt.\nPROJEKTHINTERGRUND: Im Rahmen des Projekts \u201aWeiterentwicklung ICAAP\/ILAAP\u2018 wird eine Datenbank im MS SQL-Server 2016 neu konzipiert und entwickelt, in dem der Fachbereich Ris",
//				"_version_": 1681618238386995200,
//				"indexed": "2020-10-26T12:42:03Z"
//			}]
//		}
//	}

		for (JsonValue projectValue : projects.getJsonObject("response").getJsonArray("docs")) {
			if (!(projectValue instanceof JsonObject)) {
				continue;
			}
			JsonObject projectObject = (JsonObject) projectValue;
			JsonObject projectOfferJson = etengoJsonToProjectOfferJson(projectObject);
			log.fine(projectOfferJson.toString());
			if (context != null) {
				context.createProducer().send(queue, projectOfferJson.toString());
			}
		}
	}

//	{
//		"agencyOrganization": "GULP Information Services GmbH",
//		"contactCity": "Hamburg",
//		"contactCountry": "Deutschland",
//		"contactEmail": "lilith.konzan@etengo.de",
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
//		"url": "https://www.etengo.de/gulp2/g/projekte/agentur/a0w3X00000UIsGXQA1"
//	}
	private JsonObject etengoJsonToProjectOfferJson(JsonObject projectObject) {

		JsonArrayBuilder skills = Json.createArrayBuilder();

		for (JsonValue skill : projectObject.getJsonArray("skills_stringM")) {
			skills.add(Json.createObjectBuilder().add("name", skill).build());
		}

		JsonObject jsonObject = Json.createObjectBuilder()
				.add("id", "etengo-" + ((JsonString) projectObject.get("id")).getString())
				.add("publishingOrganizationId", projectObject.get("id")).add("title", projectObject.get("title"))
				.add("description", projectObject.get("content")).add("location", projectObject.get("location_stringS"))
				.add("publishingOrganization", "etengo AG").add("agencyOrganization", "etengo AG")
				.add("url", "https://www.etengo.de/" + ((JsonString) projectObject.get("url")).getString())
				.add("originalPublicationDate", projectObject.get("created")).add("skills", skills).build();
		return jsonObject;
	}

}
