package de.incentergy.projectoffer.entities;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(indexes = @Index(columnList = "projectoffer_id"))
public class Skill {
	
	@Id
	private String id = UUID.randomUUID().toString();
	
	@Column(length=65536)
	private String name;
	
	@ManyToOne()
	private ProjectOffer projectOffer;
	
	public Skill() {
		super();
	}

	public Skill(String name) {
		setName(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ProjectOffer getProjectOffer() {
		return projectOffer;
	}

	public void setProjectOffer(ProjectOffer projectOffer) {
		this.projectOffer = projectOffer;
	}

}
