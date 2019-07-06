package org.brainteam.lunchbox.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

@Entity
public class Meal implements Comparable<Meal> {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Version
	@Column(unique=false, nullable=true)
	private Long version;
	
	@Column(unique=false, nullable=false)
	private String headline;
	
	@Column(unique=false, nullable=true)
	private String description;
	
	@Column(unique=false, nullable=true)
	private String ingredients;
	
	@Column(unique=false, nullable=false)
	private boolean salad;
	
	@Column(unique=false, nullable=false)
	private boolean veggie;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getVersion() {
		return version;
	}
	
	public void setVersion(Long version) {
		this.version = version;
	}
	
	public String getHeadline() {
		return headline;
	}
	
	public void setHeadline(String headline) {
		this.headline = headline;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getIngredients() {
		return ingredients;
	}
	
	public void setIngredients(String ingredients) {
		this.ingredients = ingredients;
	}
	
	public boolean isSalad() {
		return salad;
	}
	
	public void setSalad(boolean salad) {
		this.salad = salad;
	}
	
	public boolean isVeggie() {
		return veggie;
	}
	
	public void setVeggie(boolean veggie) {
		this.veggie = veggie;
	}
	
	@Override
	public int compareTo(Meal o) {
		return getHeadline().compareTo(o.getHeadline());
	}

}
