package de.incentergy.projectoffer.olingo;

public class JpaODataServiceFactory extends de.incentergy.base.olingo.JpaODataServiceFactory {

	public String getPersistenceUnitName() {
		return "projectoffer";
	}
}