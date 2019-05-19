package com.stackroute.keepnote.controller;

import java.util.List;

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

import com.stackroute.keepnote.exception.ReminderNotFoundException;
import com.stackroute.keepnote.model.Reminder;
import com.stackroute.keepnote.service.ReminderService;

/*
 * As in this assignment, we are working with creating RESTful web service, hence annotate
 * the class with @RestController annotation.A class annotated with @Controller annotation
 * has handler methods which returns a view. However, if we use @ResponseBody annotation along
 * with @Controller annotation, it will return the data directly in a serialized 
 * format. Starting from Spring 4 and above, we can use @RestController annotation which 
 * is equivalent to using @Controller and @ResposeBody annotation
 */
@RestController
public class ReminderController {

	/*
	 * From the problem statement, we can understand that the application requires
	 * us to implement five functionalities regarding reminder. They are as
	 * following:
	 * 
	 * 1. Create a reminder 2. Delete a reminder 3. Update a reminder 2. Get all
	 * reminders by userId 3. Get a specific reminder by id.
	 * 
	 * we must also ensure that only a user who is logged in should be able to
	 * perform the functionalities mentioned above.
	 * 
	 */

	/*
	 * Autowiring should be implemented for the ReminderService. (Use
	 * Constructor-based autowiring) Please note that we should not create any
	 * object using the new keyword
	 */
	private Logger LOGGER = LoggerFactory.getLogger(ReminderController.class);

	private ReminderService reminderService;

	public static final String LOGGEDIN_USERID = "loggedInUserId";

	@Autowired
	public ReminderController(ReminderService reminderService) {
		this.reminderService = reminderService;
	}

	/*
	 * Define a handler method which will create a reminder by reading the
	 * Serialized reminder object from request body and save the reminder in
	 * reminder table in database. Please note that the reminderId has to be unique
	 * and the loggedIn userID should be taken as the reminderCreatedBy for the
	 * reminder. This handler method should return any one of the status messages
	 * basis on different situations: 1. 201(CREATED - In case of successful
	 * creation of the reminder 2. 409(CONFLICT) - In case of duplicate reminder ID
	 * 3. 401(UNAUTHORIZED) - If the user trying to perform the action has not
	 * logged in.
	 * 
	 * This handler method should map to the URL "/reminder" using HTTP POST
	 * method".
	 */
	@PostMapping(path = "/reminder")
	public ResponseEntity<?> createReminder(@RequestBody Reminder reminder, HttpSession session) {
		LOGGER.info("ReminderController : createReminder : Begin");
		ResponseEntity<Reminder> response = null;
		try {
			if (isValidUser(session, reminder.getReminderCreatedBy())) {
				boolean result = reminderService.createReminder(reminder);
				if (result) {
					response = new ResponseEntity<>(HttpStatus.CREATED);
				} else {
					response = new ResponseEntity<>(HttpStatus.CONFLICT);
				}
			} else {
				response = new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		LOGGER.info("ReminderController : createReminder : End");
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
	 * Define a handler method which will delete a reminder from a database.
	 * 
	 * This handler method should return any one of the status messages basis on
	 * different situations: 1. 200(OK) - If the reminder deleted successfully from
	 * database. 2. 404(NOT FOUND) - If the reminder with specified reminderId is
	 * not found. 3. 401(UNAUTHORIZED) - If the user trying to perform the action
	 * has not logged in.
	 * 
	 * This handler method should map to the URL "/reminder/{id}" using HTTP Delete
	 * method" where "id" should be replaced by a valid reminderId without {}
	 */
	@DeleteMapping(value = "/reminder/{id}")
	public ResponseEntity<?> deleteReminder(@PathVariable int id, HttpSession session) {
		LOGGER.info("ReminderController : deleteReminder : Begin");
		ResponseEntity<Reminder> response = null;
		try {
			if (!ObjectUtils.isEmpty(session.getAttribute(LOGGEDIN_USERID))) {
				boolean result = reminderService.deleteReminder(id);
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
		LOGGER.info("ReminderController : deleteReminder : End");
		return response;
	}

	/*
	 * Define a handler method which will update a specific reminder by reading the
	 * Serialized object from request body and save the updated reminder details in
	 * a reminder table in database handle ReminderNotFoundException as well. please
	 * note that the loggedIn userID should be taken as the reminderCreatedBy for
	 * the reminder. This handler method should return any one of the status
	 * messages basis on different situations: 1. 200(OK) - If the reminder updated
	 * successfully. 2. 404(NOT FOUND) - If the reminder with specified reminderId
	 * is not found. 3. 401(UNAUTHORIZED) - If the user trying to perform the action
	 * has not logged in.
	 * 
	 * This handler method should map to the URL "/reminder/{id}" using HTTP PUT
	 * method.
	 */
	@PutMapping(value = "/reminder/{id}")
	public ResponseEntity<?> updateReminder(@PathVariable int id, @RequestBody Reminder reminder, HttpSession session) {
		LOGGER.info("ReminderController : updateReminder : Begin");
		ResponseEntity<Reminder> response = null;
		try {
			if (isValidUser(session, reminder.getReminderCreatedBy())) {
				Reminder result = reminderService.updateReminder(reminder, id);
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
			;
		}
		LOGGER.info("ReminderController : updateReminder : End");
		return response;
	}

	/*
	 * Define a handler method which will get us the reminders by a userId.
	 * 
	 * This handler method should return any one of the status messages basis on
	 * different situations: 1. 200(OK) - If the reminder found successfully. 2.
	 * 401(UNAUTHORIZED) -If the user trying to perform the action has not logged
	 * in.
	 * 
	 * 
	 * This handler method should map to the URL "/reminder" using HTTP GET method
	 */
	@GetMapping(value = "/reminder")
	public ResponseEntity<?> getReminder(HttpSession session) {
		LOGGER.info("ReminderController : getReminder : Begin");
		ResponseEntity<List<Reminder>> response = null;
		if (!ObjectUtils.isEmpty(session.getAttribute(LOGGEDIN_USERID))) {
			List<Reminder> list = reminderService
					.getAllReminderByUserId(session.getAttribute(LOGGEDIN_USERID).toString());
			response = ResponseEntity.ok().body(list);
		} else {
			response = new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		LOGGER.info("ReminderController : getReminder : End");
		return response;
	}

	/*
	 * Define a handler method which will show details of a specific reminder handle
	 * ReminderNotFoundException as well. This handler method should return any one
	 * of the status messages basis on different situations: 1. 200(OK) - If the
	 * reminder found successfully. 2. 401(UNAUTHORIZED) - If the user trying to
	 * perform the action has not logged in. 3. 404(NOT FOUND) - If the reminder
	 * with specified reminderId is not found. This handler method should map to the
	 * URL "/reminder/{id}" using HTTP GET method where "id" should be replaced by a
	 * valid reminderId without {}
	 */
	@GetMapping(value = "/reminder/{id}")
	public ResponseEntity<?> getReminderDetails(@PathVariable int id, HttpSession session) {
		LOGGER.info("ReminderController : getReminderDetails : Begin");
		ResponseEntity<Reminder> response = null;
		if (!ObjectUtils.isEmpty(session.getAttribute(LOGGEDIN_USERID))) {
			Reminder reminder;
			try {
				reminder = reminderService.getReminderById(id);
				if (reminder != null) {
					response = ResponseEntity.ok().body(reminder);
				} else {
					response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}
			} catch (ReminderNotFoundException e) {
				response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}

		} else {
			response = new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		LOGGER.info("ReminderController : getReminderDetails : End");
		return response;
	}

}