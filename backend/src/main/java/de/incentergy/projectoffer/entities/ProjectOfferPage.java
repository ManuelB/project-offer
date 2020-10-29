package de.incentergy.projectoffer.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class ProjectOfferPage {
	// the id of the page
	@Id
	@Column(length=65536)
	private String url;

	@Lob
	private String htmlContent;

	@Lob
	private String textContent;

	public String getUrl() {
		return url;
	}

	public void setUrl(String id) {
		this.url = id;
	}

	public String getHtmlContent() {
		return htmlContent;
	}

	public void setHtmlContent(String htmlContent) {
		this.htmlContent = htmlContent;
	}

	public String getTextContent() {
		return textContent;
	}

	public void setTextContent(String textContent) {
		this.textContent = textContent;
	}

}
