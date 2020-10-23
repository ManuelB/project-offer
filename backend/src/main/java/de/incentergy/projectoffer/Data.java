package de.incentergy.projectoffer;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

import de.incentergy.projectoffer.entities.ProjectOffer;

public class Data {

	private static Logger log = Logger.getLogger(Data.class.getName());

	public static ProjectOffer createProjectOffer() {
		ProjectOffer projectOffer = new ProjectOffer();
		projectOffer.setId("11517d47-6b8b-432d-bee9-2a33dabe2ad1");
		projectOffer.setUrl("https://www.gulp.de/gulp2/g/projekte/agentur/a0w3X00000UIsGXQA1");
		projectOffer.setAgencyOrganization("GULP Information Services GmbH");
		projectOffer.setPublishingOrganization("GULP Information Services GmbH");
		projectOffer.setPublishingOrganizationId("a0w3X00000UIsGXQA1");
		projectOffer.setTitle("Senior Software Engineer Java Middleware/Backend (m/w/d)");
		String s = "2020-10-16T08:33:00Z";
		TemporalAccessor ta = DateTimeFormatter.ISO_INSTANT.parse(s);
	    Instant i = Instant.from(ta);
	    Date d = Date.from(i);
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(d);
		projectOffer.setOriginalPublicationDate(calendar);
		projectOffer.setLocation("20095 Hamburg");
		projectOffer.setHtmlDescription("<span _ngcontent-uor-c201=\"\" class=\"form-input\"><p>Unser Kunde aus der Energiewirtschaft sucht eine / n Senior Software Engineer für den Bereich Java Middleware/ Backend. Der Einsatz soll Anfang November starten und vorerst bis Ende des Jahres laufen. Eine Vollauslastung ist gewünscht, welche vor Ort in Hamburg geleistet werden soll.</p> \n" + 
				"<p>&nbsp;</p> \n" + 
				"<p>Folgende Aufgabe erwartet Sie:</p> \n" + 
				"<p>Middleware Entwicklung in internationalem, agilen Projektteam in komplexer Architektur mit diversen SAP Systemen und Eigenentwicklungen als Randsystemen sowie Frontends und Workflowsystem als Projektapplikationen</p></span>");
		projectOffer.setDescription("Unser Kunde aus der Energiewirtschaft sucht eine / n Senior Software Engineer für den Bereich Java Middleware/ Backend. Der Einsatz soll Anfang November starten und vorerst bis Ende des Jahres laufen. Eine Vollauslastung ist gewünscht, welche vor Ort in Hamburg geleistet werden soll.\n" + 
				"\n" + 
				" \n" + 
				"\n" + 
				"Folgende Aufgabe erwartet Sie:\n" + 
				"\n" + 
				"Middleware Entwicklung in internationalem, agilen Projektteam in komplexer Architektur mit diversen SAP Systemen und Eigenentwicklungen als Randsystemen sowie Frontends und Workflowsystem als Projektapplikationen");
		
		projectOffer.addSkill("Agile Softwareentwicklung (TDD / Code Review / Pair Programming)");
		projectOffer.addSkill("Clean Code");
		projectOffer.addSkill("Agile Methoden");
		projectOffer.addSkill("Prozessmodellierung");
		projectOffer.addSkill("Basiskenntnisse in Anwendungsarchitektur (Aufnahme und Prüfung nicht-funktionaler Anforderungen)");
		projectOffer.addSkill("Java 8 (-12)");
		projectOffer.addSkill("Spring Boot");
		projectOffer.addSkill("JUnit / assertJ");
		projectOffer.addSkill("Interfaceentwicklung mit REST und SOAP");
		projectOffer.addSkill("SQL, Relationale Datenbanken");
		projectOffer.addSkill("Java Backend-Entwicklung");
		projectOffer.addSkill("Webanwendungen");
		projectOffer.addSkill("Datenbanken");
		projectOffer.addSkill("Splunk");
		projectOffer.addSkill("JIRA / Confluence");
		projectOffer.addSkill("Gitlab / Gitlab-CI");

		projectOffer.setContactFirstname("Lilith");
		projectOffer.setContactLastname("Konzan");
		projectOffer.setContactEmail("lilith.konzan@gulp.de");
		projectOffer.setContactPhone("+49 40 468987-834");
		projectOffer.setContactStreet("Amsinckstraße 34");
		projectOffer.setContactZip("20097");
		projectOffer.setContactCity("Hamburg");
		projectOffer.setContactCountry("Deutschland");

		return projectOffer;
	}

}
