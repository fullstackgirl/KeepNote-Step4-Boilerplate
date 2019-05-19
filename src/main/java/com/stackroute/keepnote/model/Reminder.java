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
 * The class "Reminder" will be acting as the data model for the Reminder Table in the database. 
 * Please note that this class is annotated with @Entity annotation. 
 * Hibernate will scan all package for any Java objects annotated with the @Entity annotation. 
 * If it finds any, then it will begin the process of looking through that particular 
 * Java object to recreate it as a table in your database.
 */
@Entity
@Table(name="Reminder")
public class Reminder {
	/*
	 * This class should have seven fields
	 * (reminderId,reminderName,reminderDescription,reminderType,
	 * reminderCreatedBy,reminderCreationDate,notes). Out of these seven fields, the
	 * field reminderId should be primary key and auto-generated. This class should
	 * also contain the getters and setters for the fields along with the no-arg ,
	 * parameterized constructor and toString method. The value of
	 * reminderCreationDate should not be accepted from the user but should be
	 * always initialized with the system date. annotate notes field with @OneToMany
	 * and @JsonIgnore
	 */

	@Id
	@Column( name = "reminder_id")
	private int reminderId;
	
	@Column( name = "reminder_name")
	private String reminderName;
	
	@Column( name = "reminder_descr")
	private String reminderDescription;
	
	@Column( name = "reminder_type")
	private String reminderType;
	
	@Column( name = "reminder_creator")
	private String reminderCreatedBy;
	
	@Column( name = "reminder_creation_date")
	private Date reminderCreationDate;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="reminder")
	@JsonIgnore
	private List<Note> notes;
	

	public Reminder() {

	}

	public Reminder(int reminderId, String reminderName, String reminderDescription, String reminderType, String string3, List<Note> notes,
			Date date) {
		this.reminderId = reminderId;
		this.reminderName = reminderName;
		this.reminderDescription = reminderDescription;
		this.reminderType = reminderType;
		this.reminderCreatedBy = string3;
		this.notes = notes;
		this.reminderCreationDate = date;
	}

	public int getReminderId() {
		return this.reminderId;

	}

	public void setReminderId(int reminderId) {
		this.reminderId = reminderId;
	}
	
	public String getReminderName() {
		return this.reminderName;
	}

	public void setReminderName(String reminderName) {
		this.reminderName = reminderName;
	}

	public String getReminderDescription() {
		return this.reminderDescription;
	}

	public void setReminderDescription(String reminderDescription) {
		this.reminderDescription = reminderDescription;
	}
	
	public String getReminderType() {
		return this.reminderType;
	}

	public void setReminderType(String reminderType) {
		this.reminderType = reminderType;
	}
	
	public Date getReminderCreationDate() {
		return this.reminderCreationDate;
	}

	public void setReminderCreationDate(Date reminderCreationDate) {
		this.reminderCreationDate = reminderCreationDate;
	}
	
	public String getReminderCreatedBy() {
		return this.reminderCreatedBy;
	}

	public void setReminderCreatedBy(String reminderCreatedBy) {
		this.reminderCreatedBy = reminderCreatedBy;
	}
	
	public List<Note> getNotes() {
		return this.notes;
	}

	public void setNotes(List<Note> notes) {
		this.notes = notes;
	}
	
	public String toString() {
		return "reminderId : "+getReminderId()+" "
				+"reminderName : "+getReminderName()+" "
				+"reminderDescription : "+getReminderDescription()+" "
				+"reminderType : "+getReminderType()+" "
				+"reminderCreationDate : "+getReminderCreationDate()+" "
				+"reminderCreatedBy : "+getReminderCreatedBy();
	}

}
