package de.incentergy.projectoffer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;

class BootstrapTest {

	@Test
	void testInit() {
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.em = mock(EntityManager.class);
		bootstrap.init();
		verify(bootstrap.em, times(1)).persist(any());
	}
	
	@Test
	void testJson() {
		Jsonb jsonb = JsonbBuilder.create();
		System.out.println(jsonb.toJson(Data.createProjectOffer()));
	}

}
