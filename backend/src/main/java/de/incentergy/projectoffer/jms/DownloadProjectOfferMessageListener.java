package de.incentergy.projectoffer.jms;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.RedirectionException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import de.incentergy.projectoffer.entities.ProjectOffer;
import de.incentergy.projectoffer.entities.ProjectOfferPage;

@MessageDriven(activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "java:/jms/projectoffer/projectoffer"),
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"),
		@ActivationConfigProperty(propertyName = "messageSelector", propertyValue = ""),
		@ActivationConfigProperty(propertyName = "maxSession", propertyValue = "1") })
public class DownloadProjectOfferMessageListener implements MessageListener {

	private static final Logger log = Logger.getLogger(DownloadProjectOfferMessageListener.class.getName());

	@PersistenceContext
	EntityManager em;

	@Resource
	private MessageDrivenContext mesageDrivenContext;

	private Jsonb jsonb;

	private Client client;

	public DownloadProjectOfferMessageListener() {
		jsonb = JsonbBuilder.create();
		client = ClientBuilder.newClient();
	}

	@Override
	public void onMessage(Message message) {
		// do not process redelivered messages
		try {
			if (message.getJMSRedelivered()) {
				return;
			}
		} catch (JMSException e1) {
			log.log(Level.SEVERE, "Exception during ignoring message", e1);
		}
		if (message instanceof TextMessage) {
			try {
				ProjectOffer projectOffer = jsonb.fromJson(((TextMessage) message).getText(), ProjectOffer.class);

				String url = projectOffer.getUrl();
				getAndPersistPage(em, client, url);

			} catch (JMSException e) {
				log.log(Level.SEVERE, "Exception during createing ProjectOfferPage", e);
				mesageDrivenContext.setRollbackOnly();
			}
		} else {
			log.log(Level.WARNING, "Unexpected project offer message type. Expected: TextMessage but was: {0}",
					message.getClass().getName());
		}
	}

	public static void getAndPersistPage(EntityManager em, Client client, String url) {
		if (em == null || em.find(ProjectOfferPage.class, url) == null) {
			log.log(Level.INFO, "Downloading project offer event {0}", new Object[] { url });
			ProjectOfferPage projectOfferPage = new ProjectOfferPage();
			projectOfferPage.setUrl(url);

			String htmlContent = null;
			String textContent = null;
			int redirectCount = 0;
			do {
				try {
					htmlContent = client.target(url).request().get(String.class);
				} catch (RedirectionException ex) {
					url = ex.getLocation().toString();
					projectOfferPage.setUrl(url);

					log.log(Level.INFO, "Following redirect {0}", new Object[] { url });
					redirectCount++;
				}
			} while (htmlContent == null && redirectCount < 3);
			if (htmlContent != null) {
				textContent = htmlContent.replaceAll("<[^>]*>", "");
			}
			projectOfferPage.setHtmlContent(htmlContent);
			projectOfferPage.setTextContent(textContent);
			if (em != null) {
				em.persist(projectOfferPage);
			} else {
				log.warning("em is null. In a test case this might be normal");
			}

		}
	}

}
