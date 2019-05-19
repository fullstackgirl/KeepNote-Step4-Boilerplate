package com.stackroute.keepnote.dao;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import com.stackroute.keepnote.exception.ReminderNotFoundException;
import com.stackroute.keepnote.model.Reminder;

/*
 * This class is implementing the UserDAO interface. This class has to be annotated with 
 * @Repository annotation.
 * @Repository - is an annotation that marks the specific class as a Data Access Object, 
 * thus clarifying it's role.
 * @Transactional - The transactional annotation itself defines the scope of a single database 
 * 					transaction. The database transaction happens inside the scope of a persistence 
 * 					context.  
 * */
@Repository
@Transactional
public class ReminderDAOImpl implements ReminderDAO {
	
	/*
	 * Autowiring should be implemented for the SessionFactory.(Use
	 * constructor-based autowiring.
	 */
	private SessionFactory sessionFactory;
	
	@Autowired
	public ReminderDAOImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public SessionFactory getSessionFactory() {
		return this.sessionFactory;
	}
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	

	private Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	/*
	 * Create a new reminder
	 */

	public boolean createReminder(Reminder reminder) {
		boolean result = false;
		try {
			getSession().save(reminder);
			getSession().flush();
			result =  true;
		} catch (HibernateException e) {
			result =  false;
		}
		return result;

	}
	
	/*
	 * Update an existing reminder
	 */

	public boolean updateReminder(Reminder reminder) {
		boolean status = false; 
		try {
			if(getReminderById(reminder.getReminderId()) != null){
				getSession().update(reminder);
				getSession().flush();
				status = true;
			}
		} catch (HibernateException | ReminderNotFoundException e) {
			status = false;
		}
		return status;
	}

	/*
	 * Remove an existing reminder
	 */
	
	public boolean deleteReminder(int reminderId) {
		boolean result = false;
		try {
			Reminder reminder = getSession().get(Reminder.class, reminderId);
			if (reminder != null) {
				getSession().delete(reminder);
				getSession().flush();
				result = true;
			}
		} catch (Exception e) {
			result = false;
		}
		return result;

	}

	/*
	 * Retrieve details of a specific reminder
	 */
	
	public Reminder getReminderById(int reminderId) throws ReminderNotFoundException {
		try {
			Reminder reminder = getSession().get(Reminder.class, reminderId);
			if (ObjectUtils.isEmpty(reminder)) {
				throw new ReminderNotFoundException("Reminder not found");
			}
			return reminder;
		} catch (Exception e) {
			throw new ReminderNotFoundException("Reminder not found");
		}

	}

	/*
	 * Retrieve details of all reminders by userId
	 */
	
	public List<Reminder> getAllReminderByUserId(String userId) {
		CriteriaBuilder builder = getSession().getCriteriaBuilder();
		CriteriaQuery<Reminder> criteriaQuery = builder.createQuery(Reminder.class);
		Root<Reminder> root = criteriaQuery.from(Reminder.class);
		criteriaQuery.select(root).where(builder.equal(root.get("reminderCreatedBy"), userId));
		return getSession().createQuery(criteriaQuery).getResultList();
	}

}
