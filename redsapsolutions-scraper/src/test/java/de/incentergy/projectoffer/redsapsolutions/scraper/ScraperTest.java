package de.incentergy.projectoffer.redsapsolutions.scraper;

import java.io.IOException;
import java.util.logging.LogManager;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ScraperTest {

	
	@BeforeAll
	static void configureLogging() {
		try {
			// https://community.oracle.com/thread/1307033?start=0&tstart=0
			LogManager.getLogManager().readConfiguration(
					ScraperTest.class
							.getResourceAsStream("/logging.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	@Test
	void testScrap() {
		var scraper = new Scraper();
		scraper.scrap();
	}

}
