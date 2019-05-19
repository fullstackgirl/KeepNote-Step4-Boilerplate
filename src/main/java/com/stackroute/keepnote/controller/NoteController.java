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

import com.stackroute.keepnote.model.Note;
import com.stackroute.keepnote.service.NoteService;

/*
 * As in this assignment, we are working with creating RESTful web service, hence annotate
 * the class with @RestController annotation.A class annotated with @Controller annotation
 * has handler methods which returns a view. However, if we use @ResponseBody annotation along
 * with @Controller annotation, it will return the data directly in a serialized 
 * format. Starting from Spring 4 and above, we can use @RestController annotation which 
 * is equivalent to using @Controller and @ResposeBody annotation
 */
@RestController
public class NoteController {

	/*
	 * Autowiring should be implemented for the NoteService. (Use Constructor-based
	 * autowiring) Please note that we should not create any object using the new
	 * keyword
	 */
	private Logger LOGGER = LoggerFactory.getLogger(NoteController.class);

	private NoteService noteService;
	
	public static final String LOGGEDIN_USERID = "loggedInUserId";

	@Autowired
	public NoteController(NoteService noteService) {
		this.noteService = noteService;
	}

	/*
	 * Define a handler method which will create a specific note by reading the
	 * Serialized object from request body and save the note details in a Note table
	 * in the database.Handle ReminderNotFoundException and
	 * CategoryNotFoundException as well. please note that the loggedIn userID
	 * should be taken as the createdBy for the note.This handler method should
	 * return any one of the status messages basis on different situations: 1.
	 * 201(CREATED) - If the note created successfully. 2. 409(CONFLICT) - If the
	 * noteId conflicts with any existing user3. 401(UNAUTHORIZED) - If the user
	 * trying to perform the action has not logged in.
	 * 
	 * This handler method should map to the URL "/note" using HTTP POST method
	 */
	@PostMapping(value = "/note")
	public ResponseEntity<?> createNote(@RequestBody Note note, HttpSession session) {
		LOGGER.info("NoteController : createNote : Begin");
		ResponseEntity<Note> response = null;
		try {
			if (isValidUser(session, note.getCreatedBy())) {
				boolean result = noteService.createNote(note);
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
		LOGGER.info("NoteController : createNote : End : HttpStatus : "+response.getStatusCode());
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
	 * Define a handler method which will delete a note from a database handle NoteNotFoundException
	 * as well.
	 * This handler method should return any one of the status messages basis on
	 * different situations: 1. 200(OK) - If the note deleted successfully from
	 * database. 2. 404(NOT FOUND) - If the note with specified noteId is not found.
	 * 3. 401(UNAUTHORIZED) - If the user trying to perform the action has not
	 * logged in.
	 * 
	 * This handler method should map to the URL "/note/{id}" using HTTP Delete
	 * method" where "id" should be replaced by a valid noteId without {}
	 */
	@DeleteMapping(value="/note/{id}")
	public ResponseEntity<?> deleteNote(@PathVariable int id, HttpSession session) {
		LOGGER.info("NoteController : deleteNote : Begin : Id : "+id);
		ResponseEntity<Note> response = null;
		try {
			if (!ObjectUtils.isEmpty(session.getAttribute(LOGGEDIN_USERID))) {
				boolean result = noteService.deleteNote(id);
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
		LOGGER.info("NoteController : deleteNote : End : HttpStatus : "+response.getStatusCode());
		return response;
	}

	/*
	 * Define a handler method which will update a specific note by reading the
	 * Serialized object from request body and save the updated note details in a
	 * note table in database handle ReminderNotFoundException,
	 * NoteNotFoundException, CategoryNotFoundException as well. please note that
	 * the loggedIn userID should be taken as the createdBy for the note. This
	 * handler method should return any one of the status messages basis on
	 * different situations: 1. 200(OK) - If the note updated successfully. 2.
	 * 404(NOT FOUND) - If the note with specified noteId is not found. 3.
	 * 401(UNAUTHORIZED) - If the user trying to perform the action has not logged
	 * in.
	 * 
	 * This handler method should map to the URL "/note/{id}" using HTTP PUT method.
	 */
	@PutMapping(value="/note/{id}")
	public ResponseEntity<?> updateNote(@PathVariable int id, @RequestBody Note note, HttpSession session) {
		LOGGER.info("NoteController : updateNote : Begin : Id : "+id);
		ResponseEntity<Note> response = null;
		try {
			if (isValidUser(session, note.getCreatedBy())) {
				Note result = noteService.updateNote(note, id);
				if (result != null) {
					response = new ResponseEntity<>(HttpStatus.OK);
				} else {
					response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}
			} else {
				response = new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);;
		}
		LOGGER.info("NoteController : updateNote : End : HttpStatus : "+response.getStatusCode());
		return response;
	}


	/*
	 * Define a handler method which will get us the notes by a userId.
	 * 
	 * This handler method should return any one of the status messages basis on
	 * different situations: 1. 200(OK) - If the note found successfully. 2.
	 * 401(UNAUTHORIZED) -If the user trying to perform the action has not logged
	 * in.
	 * 
	 * 
	 * This handler method should map to the URL "/note" using HTTP GET method
	 */
	@GetMapping(value = "/note")
	public ResponseEntity<?> getNotes(HttpSession session) {
		LOGGER.info("NoteController : getNotes : Begin");
		ResponseEntity<List<Note>> response = null;
		if (!ObjectUtils.isEmpty(session.getAttribute(LOGGEDIN_USERID))) {
			List<Note> list = noteService
					.getAllNotesByUserId(session.getAttribute(LOGGEDIN_USERID).toString());
			response = ResponseEntity.ok().body(list);
		} else {
			response = new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		LOGGER.info("NoteController : getNotes : End");
		return response;
	}

}
