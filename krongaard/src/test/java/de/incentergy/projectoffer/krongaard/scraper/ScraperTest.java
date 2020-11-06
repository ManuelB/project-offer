package de.incentergy.projectoffer.krongaard.scraper;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
		scraper.scrap(new EntityLoggingFilter());
	}
	
	@Test
	void testIso8601String() {
		assertEquals("2019-02-12T10:37:48Z", Scraper.toIso8601String("/Date(1549964268000+0100)/"));
	}

}
