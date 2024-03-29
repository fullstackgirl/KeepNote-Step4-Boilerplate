package com.stackroute.keepnote.controller;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.stackroute.keepnote.exception.UserNotFoundException;
import com.stackroute.keepnote.model.User;
import com.stackroute.keepnote.service.UserService;

/*
 * As in this assignment, we are working on creating RESTful web service, hence annotate
 * the class with @RestController annotation. A class annotated with the @Controller annotation
 * has handler methods which return a view. However, if we use @ResponseBody annotation along
 * with @Controller annotation, it will return the data directly in a serialized 
 * format. Starting from Spring 4 and above, we can use @RestController annotation which 
 * is equivalent to using @Controller and @ResposeBody annotation
 */
@RestController
public class UserController {

	/*
	 * Autowiring should be implemented for the UserService. (Use Constructor-based
	 * autowiring) Please note that we should not create an object using the new
	 * keyword
	 */
	private Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	private UserService userService;

	public static final String LOGGEDIN_USERID = "loggedInUserId";

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	/*
	 * Define a handler method which will create a specific user by reading the
	 * Serialized object from request body and save the user details in a User table
	 * in the database. This handler method should return any one of the status
	 * messages basis on different situations: 1. 201(CREATED) - If the user created
	 * successfully. 2. 409(CONFLICT) - If the userId conflicts with any existing
	 * user
	 * 
	 * Note: ------ This method can be called without being logged in as well as
	 * when a new user will use the app, he will register himself first before
	 * login.
	 * 
	 * This handler method should map to the URL "/user/register" using HTTP POST
	 * method
	 */
	@PostMapping(path = "/user/register")
	public ResponseEntity<?> createUser(@RequestBody User user, HttpSession session) {
		LOGGER.info("UserController : createUser : Begin");
		ResponseEntity<User> response = null;
		try {
			boolean result = userService.registerUser(user);
			if (result) {
				response = new ResponseEntity<>(HttpStatus.CREATED);
			} else {
				response = new ResponseEntity<>(HttpStatus.CONFLICT);
			}
		} catch (Exception e) {
			response = new ResponseEntity<>(HttpStatus.CONFLICT);
		}
		LOGGER.info("UserController : createUser : End");
		return response;
	}

	/**
	 * 
	 * @param userId
	 * @return
	 */
	private boolean isValidUser(HttpSession session, String userId) {
		boolean result = false;
		try {
			if (session.getAttribute(LOGGEDIN_USERID).equals(userId)) {
				result = true;
			}
		} catch (Exception e) {
			result = false;
		}
		return result;
	}

	/*
	 * Define a handler method which will update a specific user by reading the
	 * Serialized object from request body and save the updated user details in a
	 * user table in database handle exception as well. This handler method should
	 * return any one of the status messages basis on different situations: 1.
	 * 200(OK) - If the user updated successfully. 2. 404(NOT FOUND) - If the user
	 * with specified userId is not found. 3. 401(UNAUTHORIZED) - If the user trying
	 * to perform the action has not logged in.
	 * 
	 * This handler method should map to the URL "/user/{id}" using HTTP PUT method.
	 */
	@PutMapping(value = "/user/{id}")
	public ResponseEntity<?> updateUser(@PathVariable String id, @RequestBody User user, HttpSession session) {
		LOGGER.info("UserController : updateUser : Begin");
		ResponseEntity<User> response = null;
		try {
			if (isValidUser(session, user.getUserId())) {
				User result = userService.updateUser(user, id);
				if (result != null) {
					response = new ResponseEntity<>(HttpStatus.OK);
				} else {
					response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}
			} else {
				response = new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		LOGGER.info("UserController : updateUser : End");
		return response;
	}

	/*
	 * Define a handler method which will delete a user from a database.
	 * 
	 * This handler method should return any one of the status messages basis on
	 * different situations: 1. 200(OK) - If the user deleted successfully from
	 * database. 2. 404(NOT FOUND) - If the user with specified userId is not found.
	 * 3. 401(UNAUTHORIZED) - If the user trying to perform the action has not
	 * logged in.
	 * 
	 * This handler method should map to the URL "/user/{id}" using HTTP Delete
	 * method" where "id" should be replaced by a valid userId without {}
	 */
	@DeleteMapping(value = "/user/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable String id, HttpSession session) {
		LOGGER.info("UserController : deleteUser : Begin");
		ResponseEntity<User> response = null;
		try {
			if (!ObjectUtils.isEmpty(session.getAttribute(LOGGEDIN_USERID))) {
				boolean result = userService.deleteUser(id);
				if (result) {
					response = new ResponseEntity<>(HttpStatus.OK);
				} else {
					response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}
			} else {
				response = new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		LOGGER.info("UserController : deleteUser : End");
		return response;
	}

	/*
	 * Define a handler method which will show details of a specific user handle
	 * UserNotFoundException as well. This handler method should return any one of
	 * the status messages basis on different situations: 1. 200(OK) - If the user
	 * found successfully. 2. 401(UNAUTHORIZED) - If the user trying to perform the
	 * action has not logged in. 3. 404(NOT FOUND) - If the user with specified
	 * userId is not found. This handler method should map to the URL "/user/{id}"
	 * using HTTP GET method where "id" should be replaced by a valid userId without
	 * {}
	 */
	@GetMapping(value = "/user/{id}")
	public ResponseEntity<?> getUserDetails(@PathVariable String id, HttpSession session) {
		LOGGER.info("UserController : getUserDetails : Begin");
		ResponseEntity<User> response = null;
		if (!ObjectUtils.isEmpty(session.getAttribute(LOGGEDIN_USERID))) {
			User user;
			try {
				user = userService.getUserById(id);
				if (user != null) {
					response = ResponseEntity.ok().body(user);
				} else {
					response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}
			} catch (UserNotFoundException e) {
				response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}

		} else {
			response = new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		LOGGER.info("UserController : getUserDetails : End");
		return response;
	}

}