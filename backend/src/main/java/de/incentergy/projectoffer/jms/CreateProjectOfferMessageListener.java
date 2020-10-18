package de.incentergy.projectoffer.jms;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.jms.JMSDestinationDefinition;
import javax.jms.JMSDestinationDefinitions;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import de.incentergy.projectoffer.entities.ProjectOffer;

@JMSDestinationDefinitions({
		@JMSDestinationDefinition(name = "java:/jms/projectoffer/projectoffer", interfaceName = "javax.jms.Queue", destinationName = "projectoffer") })
@MessageDriven(activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "java:/jms/projectoffer/projectoffer"),
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(propertyName = "messageSelector", propertyValue = "") })
public class CreateProjectOfferMessageListener implements MessageListener {

	private static final Logger log = Logger.getLogger(CreateProjectOfferMessageListener.class.getName());

	@PersistenceContext
	EntityManager em;

	@Resource
	private MessageDrivenContext mesageDrivenContext;

	private Jsonb jsonb;

	public CreateProjectOfferMessageListener() {
		jsonb = JsonbBuilder.create();
	}

	@Override
	public void onMessage(Message message) {
		if (message instanceof TextMessage) {
			try {
				ProjectOffer projectOffer = jsonb.fromJson(((TextMessage) message).getText(), ProjectOffer.class);
				log.log(Level.INFO, "Processing project offer event {0}", new Object[] { projectOffer.getId() });

				em.merge(projectOffer);

			} catch (JMSException e) {
				log.log(Level.SEVERE, "Exception during createing ProjectOffer: {0}", e.toString());
				mesageDrivenContext.setRollbackOnly();
			}
		} else {
			log.log(Level.WARNING, "Unexpected project offer message type. Expected: TextMessage but was: {0}",
					message.getClass().getName());
		}
	}

}
