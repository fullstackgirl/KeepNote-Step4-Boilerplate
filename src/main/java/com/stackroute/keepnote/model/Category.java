package com.stackroute.keepnote.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

/*
 * The class "Category" will be acting as the data model for the Category Table in the database. 
 * Please note that this class is annotated with @Entity annotation. 
 * Hibernate will scan all package for any Java objects annotated with the @Entity annotation. 
 * If it finds any, then it will begin the process of looking through that particular 
 * Java object to recreate it as a table in your database.
 */
@Entity
@Table(name="Category")
public class Category {
	/*
	 * This class should have six fields
	 * (categoryId,categoryName,categoryDescription,
	 * categoryCreatedBy,categoryCreationDate,notes). Out of these six fields, the
	 * field categoryId should be primary key and auto-generated. This class should
	 * also contain the getters and setters for the fields along with the no-arg ,
	 * parameterized constructor and toString method. The value of
	 * categoryCreationDate should not be accepted from the user but should be
	 * always initialized with the system date. annotate notes field with @OneToMany
	 * and @JsonIgnore
	 */
	@Id
	@Column( name ="category_id")
	private int categoryId;
	
	@Column( name ="category_name")
	private String categoryName;
	
	@Column( name ="category_descr")
	private String categoryDescription;
	
	@Column( name ="category_creation_date")
	private Date categoryCreationDate;
	
	@Column( name ="category_creator")
	private String categoryCreatedBy;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="category")
	@JsonIgnore
	private List<Note> notes;
	
	public Category() {
		// Default Constructor
	}

	public Category(int categoryId, String categoryName, String categoryDescription, Date categoryCreationDate, String categoryCreatedBy, List<Note> notes) {

		this.categoryId = categoryId;
		this.categoryName = categoryName;
		this.categoryDescription = categoryDescription;
		this.categoryCreationDate = (categoryCreationDate != null) ? categoryCreationDate : new Date();
		this.categoryCreatedBy = categoryCreatedBy;
		this.notes = notes;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public int getCategoryId() {
		return this.categoryId;
	}

	public String getCategoryName() {
		return this.categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getCategoryDescription() {
		return this.categoryDescription;
	}

	public void setCategoryDescription(String string) {
		this.categoryDescription = string;
	}
	
	public Date getCategoryCreationDate() {
		return this.categoryCreationDate;
	}

	public void setCategoryCreationDate(Date date) {
		this.categoryCreationDate = date;
	}
	
	public String getCategoryCreatedBy() {
		return this.categoryCreatedBy;
	}

	public void setCategoryCreatedBy(String string) {
		this.categoryCreatedBy = string;
	}

	public void setNotes(List<Note> notes) {
		this.notes = notes;
	}
	
	public List<Note> getNotes() {
		return this.notes;
	}
	
	public String toString() {
		return "categoryId : "+getCategoryId()+" "
				+"categoryName : "+getCategoryName()+" "
				+"categoryDescription : "+getCategoryDescription()+" "
				+"categoryCreationDate : "+getCategoryCreationDate()+" "
				+"categoryCreatedBy : "+getCategoryCreatedBy();
	}
}