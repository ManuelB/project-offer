package de.incentergy.projectoffer.entities;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.jboss.resteasy.plugins.providers.atom.Entry;

import de.incentergy.base.opensearch.Searchable;

@Entity
@Indexed
public class ProjectOffer implements Searchable {
	
	private static Logger log = Logger.getLogger(ProjectOffer.class.getName());
	
	@Id
	private String id = UUID.randomUUID().toString();
	
	private String url;

	@Field
	private String publishingOrganization;

	private String publishingOrganizationId;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar originalPublicationDate;
	
	@Field
	private String agencyOrganization;
	
	@Field
	private String searchingOrganization;

	@Field
	private String title;
	
	@Field
	private String location;
	
	@Column(length=65536)
	private String htmlDescription;
	
	@Column(length=65536)
	@Field
	private String description;
	
	private String contactFirstname;
	private String contactLastname;
	private String contactPhone;
	private String contactEmail;
	private String contactStreet;
	private String contactZip;
	private String contactCity;
	private String contactCountry;
	
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Skill> skills = new ArrayList<>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPublishingOrganization() {
		return publishingOrganization;
	}

	public void setPublishingOrganization(String publishingOrganization) {
		this.publishingOrganization = publishingOrganization;
	}

	public String getPublishingOrganizationId() {
		return publishingOrganizationId;
	}

	public void setPublishingOrganizationId(String publishingOrganizationId) {
		this.publishingOrganizationId = publishingOrganizationId;
	}

	public Calendar getOriginalPublicationDate() {
		return originalPublicationDate;
	}

	public void setOriginalPublicationDate(Calendar originalPublicationDate) {
		this.originalPublicationDate = originalPublicationDate;
	}

	public String getAgencyOrganization() {
		return agencyOrganization;
	}

	public void setAgencyOrganization(String agencyOrganization) {
		this.agencyOrganization = agencyOrganization;
	}

	public String getSearchingOrganization() {
		return searchingOrganization;
	}

	public void setSearchingOrganization(String searchingOrganization) {
		this.searchingOrganization = searchingOrganization;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getHtmlDescription() {
		return htmlDescription;
	}

	public void setHtmlDescription(String description) {
		this.htmlDescription = description;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Skill> getSkills() {
		return skills;
	}
	
	public void addSkill(String name) {
		getSkills().add(new Skill(name));
	}

	public void setSkills(List<Skill> skills) {
		this.skills = skills;
	}

	public String getContactFirstname() {
		return contactFirstname;
	}

	public void setContactFirstname(String contactFirstname) {
		this.contactFirstname = contactFirstname;
	}

	public String getContactLastname() {
		return contactLastname;
	}

	public void setContactLastname(String contactLastname) {
		this.contactLastname = contactLastname;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getContactEmail() {
		return contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	public String getContactStreet() {
		return contactStreet;
	}

	public void setContactStreet(String contactStreet) {
		this.contactStreet = contactStreet;
	}

	public String getContactZip() {
		return contactZip;
	}

	public void setContactZip(String contactZip) {
		this.contactZip = contactZip;
	}

	public String getContactCity() {
		return contactCity;
	}

	public void setContactCity(String contactCity) {
		this.contactCity = contactCity;
	}

	public String getContactCountry() {
		return contactCountry;
	}

	public void setContactCountry(String contactCountry) {
		this.contactCountry = contactCountry;
	}

	@Override
	public Entry toEntry() {
		Entry entry = new Entry();
		entry.setTitle(getTitle());
		try {
			entry.setId(new URI("#projectoffer/ProjectOffers('"+getId()+"')/TwoColumnsMidExpanded"));
		} catch (URISyntaxException e) {
			log.log(Level.WARNING, "Could not generate search uri", e);
		}
		return entry;
	}



}
