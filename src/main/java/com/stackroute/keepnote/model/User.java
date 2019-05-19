package com.stackroute.keepnote.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/*
 * The class "User" will be acting as the data model for the User Table in the database. 
 * Please note that this class is annotated with @Entity annotation. 
 * Hibernate will scan all package for any Java objects annotated with the @Entity annotation. 
 * If it finds any, then it will begin the process of looking through that particular 
 * Java object to recreate it as a table in your database.
 */
@Entity
@Table(name = "User")
public class User {

	/*
	 * This class should have five fields (userId,userName,
	 * userPassword,userMobile,userAddedDate). Out of these five fields, the field
	 * userId should be the primary key. This class should also contain the getters
	 * and setters for the fields, along with the no-arg , parameterized constructor
	 * and toString method.The value of userAddedDate should not be accepted from
	 * the user but should be always initialized with the system date
	 */


	@Id
	@Column( name = "user_id")
	private String userId;

	@Column( name = "user_name")
	private String userName;

	@Column( name = "user_password")
	private String userPassword;

	@Column( name = "user_mobile")
	private String userMobile;

	@Column( name = "user_added_date")
	private Date userAddedDate;

	public User() {

	}

	public User(String string, String string1, String string2, String string3, Date date) {

		this.userId = string;
		this.userName = string1;
		this.userPassword = string2;
		this.userMobile = string3;
		this.userAddedDate = date;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String string) {
		this.userId = string;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String string) {
		this.userName = string;
	}

	public String getUserPassword() {
		return this.userPassword;
	}

	public void setUserPassword(String string) {
		this.userPassword = string;
	}

	public String getUserMobile() {
		return this.userMobile;
	}

	public void setUserMobile(String string) {
		this.userMobile = string;
	}

	public Date getUserAddedDate() {
		return this.userAddedDate;
	}

	public void setUserAddedDate(Date date) {
		this.userAddedDate = date;
	}
	
	public String toString() {
		return "userId : "+getUserId()+" "
				+ "userName : "+getUserName()+" "
				+ "userPassword : "+getUserPassword()+" "
				+ "userMobile : "+getUserMobile()+" "
				+ "userAddedDate : "+getUserAddedDate();
	}

}
