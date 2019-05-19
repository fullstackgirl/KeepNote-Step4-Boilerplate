package com.stackroute.keepnote.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/*
 * The class "Note" will be acting as the data model for the Note Table in the database. 
 * Please note that this class is annotated with @Entity annotation. 
 * Hibernate will scan all package for any Java objects annotated with the @Entity annotation. 
 * If it finds any, then it will begin the process of looking through that particular 
 * Java object to recreate it as a table in your database.
 */
@Entity
@Table(name="Note")
public class Note {
	/*
	 * This class should have eight fields
	 * (noteId,noteTitle,noteContent,noteStatus,createdAt,
	 * category,reminder,createdBy). Out of these eight fields, the field noteId
	 * should be primary key and auto-generated. This class should also contain the
	 * getters and setters for the fields along with the no-arg , parameterized
	 * constructor and toString method. The value of createdAt should not be
	 * accepted from the user but should be always initialized with the system date.
	 * annotate category and reminder field with @ManyToOne.
	 */
	@Id
	@Column(name="note_id")
	private int noteId;
	
	@Column(name="note_title")
	private String noteTitle;
	
	@Column(name="note_content")
	private String noteContent;
	
	@Column(name="note_status")
	private String noteStatus;
	
	@Column(name="note_creation_date")
	private Date createdAt;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "category_id")
	private Category category;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "reminder_id")
	private Reminder reminder;
	
	@Column(name="note_creator")
	private String createdBy;

	public Note() {
		// Default Constructor
	}

	public Note(int noteId, String noteTitle, String noteContent, String noteStatus, Date date, Category category, Reminder reminder,
			String createdBy) {
		this.noteId = noteId;
		this.noteTitle = noteTitle;
		this.noteContent = noteContent;
		this.noteStatus = noteStatus;
		this.createdAt = date;
		this.category = category;
		this.reminder = reminder;
		this.createdBy = createdBy;
	}

	public int getNoteId() {
		return this.noteId;
	}

	public void setNoteId(int noteId) {
		this.noteId = noteId;
	}

	public String getNoteTitle() {
		return this.noteTitle;
	}

	public void setNoteTitle(String noteTitle) {
		this.noteTitle = noteTitle;
	}

	public String getNoteContent() {
		return this.noteContent;
	}

	public void setNoteContent(String noteContent) {
		this.noteContent = noteContent;
	}

	public String getNoteStatus() {
		return this.noteStatus;
	}
	
	public void setNoteStatus(String noteStatus) {
		this.noteStatus = noteStatus;
	}

	public Date getNoteCreatedAt() {
		return this.createdAt;
	}
	
	public void setNoteCreatedAt(Date date) {
		this.createdAt = date;
	}
	
	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Reminder getReminder() {
		return this.reminder;
	}
	
	public void setReminder(Reminder reminder) {
		this.reminder = reminder;
	}
	
	public Category getCategory() {
		return this.category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	@Override
	public String toString() {
		return "noteId : "+getNoteId()+" "
				+"noteTitle : "+getNoteTitle()+" "
				+"noteContent : "+getNoteContent()+" "
				+"noteStatus : "+getNoteStatus()+" "
				+"createdAt : "+getNoteCreatedAt()+" "
				+"createdBy : "+getCreatedBy();
	}

}
