package de.incentergy.projectoffer;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import de.incentergy.projectoffer.entities.ProjectOffer;

@Singleton
@Startup
public class Bootstrap {

	@PersistenceContext
	EntityManager em;

	@PostConstruct
	public void init() {
		ProjectOffer projectOffer = em.find(ProjectOffer.class, Data.createProjectOffer().getId());
		if (projectOffer == null) {
			projectOffer = Data.createProjectOffer();
			em.persist(projectOffer);
		}

	}
}
