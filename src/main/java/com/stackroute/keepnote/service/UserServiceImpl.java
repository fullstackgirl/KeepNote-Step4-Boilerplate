package com.stackroute.keepnote.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.stackroute.keepnote.dao.UserDAO;
import com.stackroute.keepnote.exception.UserAlreadyExistException;
import com.stackroute.keepnote.exception.UserNotFoundException;
import com.stackroute.keepnote.model.User;

/*
* Service classes are used here to implement additional business logic/validation 
* This class has to be annotated with @Service annotation.
* @Service - It is a specialization of the component annotation. It doesn�t currently 
* provide any additional behavior over the @Component annotation, but it�s a good idea 
* to use @Service over @Component in service-layer classes because it specifies intent 
* better. Additionally, tool support and additional behavior might rely on it in the 
* future.
* */
@Service("userService")
public class UserServiceImpl implements UserService {

	/*
	 * Autowiring should be implemented for the userDAO. (Use Constructor-based
	 * autowiring) Please note that we should not create any object using the new
	 * keyword.
	 */
private UserDAO userDAO;
	
	@Autowired
	public UserServiceImpl(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	/*
	 * This method should be used to save a new user.
	 */

	public boolean registerUser(User user) throws UserAlreadyExistException {
		boolean status = false;
		try {
			User fuser = userDAO.getUserById(user.getUserId());
			if (!ObjectUtils.isEmpty(fuser)) {
				throw new UserAlreadyExistException("User Already Exist");
			}
			status = userDAO.registerUser(user);
			if(!status) {
				throw new UserAlreadyExistException("User Already Exist");
			}
		} catch (Exception e) {
			status =false;
			e.printStackTrace();
			throw new UserAlreadyExistException("User Already Exist");
		}
		return status;
	}

	/*
	 * This method should be used to update a existing user.
	 */

	public User updateUser(User user, String userId) throws Exception {
		User fuser = userDAO.getUserById(userId);
		if(fuser == null) {
			throw new UserNotFoundException("User Not Found");
		} else {
			userDAO.updateUser(user);
		}
		return user;

	}

	/*
	 * This method should be used to get a user by userId.
	 */

	public User getUserById(String userId) throws UserNotFoundException {
		User user = userDAO.getUserById(userId);
		if(user == null) {
			throw new UserNotFoundException("User Not Found");
		}
		return user;

	}

	/*
	 * This method should be used to validate a user using userId and password.
	 */

	public boolean validateUser(String userId, String password) throws UserNotFoundException {
		boolean result = false;
		result = userDAO.validateUser(userId, password);
		if(!result) {
			throw new UserNotFoundException("User Not Found");
		}
		return result;
	}

	/* This method should be used to delete an existing user. */
	public boolean deleteUser(String userId) {
		return userDAO.deleteUser(userId);

	}

}
