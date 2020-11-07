package de.incentergy.projectoffer.jms;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.junit.jupiter.api.Test;

class DownloadProjectOfferMessageListenerTest {

	@Test
	void testGetAndPersistPage() {
		Client client = ClientBuilder.newClient();
		DownloadProjectOfferMessageListener.getAndPersistPage(null, client, "https://www.etengo.de/index.php?id=30&tx_etengowebsite_pi4%5Baction%5D=detail&tx_etengowebsite_pi4%5Bjob%5D=57563&L=0%22");
	}

}
