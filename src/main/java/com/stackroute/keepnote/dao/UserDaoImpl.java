package com.stackroute.keepnote.dao;

import javax.transaction.Transactional;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.stackroute.keepnote.exception.UserNotFoundException;
import com.stackroute.keepnote.model.User;

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
public class UserDaoImpl implements UserDAO {

	/*
	 * Autowiring should be implemented for the SessionFactory.(Use
	 * constructor-based autowiring.
	 */

	private SessionFactory sessionFactory;
	
	@Autowired
	public UserDaoImpl(SessionFactory sessionFactory) {
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
	 * Create a new user
	 */

	public boolean registerUser(User user) {
		boolean result = false;
		try {
			getSession().save(user);
			getSession().flush();
			result =  true;
		} catch (HibernateException e) {
			e.printStackTrace();
			result =  false;
		}
		return result;
	}

	/*
	 * Update an existing user
	 */

	public boolean updateUser(User user) {
		boolean status = false; 
		try {
			if(getUserById(user.getUserId()) != null){
				getSession().update(user);
				getSession().flush();
				status = true;
			}
		} catch (HibernateException e) {
			status = false;
		}
		return status;

	}

	/*
	 * Retrieve details of a specific user
	 */
	public User getUserById(String UserId) {

		return getSession().load(User.class, UserId);
	}

	/*
	 * validate an user
	 */

	public boolean validateUser(String userId, String password) throws UserNotFoundException {
		boolean result = false;
		try {
			User user = getSession().load(User.class, userId);
			if (user != null) {
				if (password.equals(user.getUserPassword())) {
					result = true;
				}
			} else {
				throw new UserNotFoundException("User Not Found");
			}
		} catch (Exception e) {
			throw new UserNotFoundException("User Not Found");
		}
		return result;
	}

	/*
	 * Remove an existing user
	 */
	public boolean deleteUser(String userId) {
		boolean result = false;
		try {
			User user = getSession().load(User.class, userId);
			if (user != null) {
				getSession().delete(user);
				getSession().flush();
				result = true;
			}
		} catch (Exception e) {
			result = false;
		}
		return result;

	}

}
