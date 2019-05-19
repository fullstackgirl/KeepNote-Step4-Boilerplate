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

import com.stackroute.keepnote.exception.NoteNotFoundException;
import com.stackroute.keepnote.model.Note;

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
public class NoteDAOImpl implements NoteDAO {

	/*
	 * Autowiring should be implemented for the SessionFactory.(Use
	 * constructor-based autowiring.
	 */
	private SessionFactory sessionFactory;
	
	@Autowired
	public NoteDAOImpl(SessionFactory sessionFactory) {
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
	 * Create a new note
	 */
	
	public boolean createNote(Note note) {
		boolean result = false;
		try {
			getSession().save(note);
			getSession().flush();
			result =  true;
		} catch (HibernateException e) {
			result =  false;
		}
		return result;

	}

	/*
	 * Remove an existing note
	 */
	
	public boolean deleteNote(int noteId) throws NoteNotFoundException {
		boolean result = false;
		try {
			Note note = getSession().get(Note.class, noteId);
			if (note != null) {
				getSession().delete(note);
				getSession().flush();
				result = true;
			} else {
				throw new NoteNotFoundException("Note not found");
			}
		} catch (Exception e) {
			throw new NoteNotFoundException("Note not found");
		}
		return result;
	}

	/*
	 * Retrieve details of all notes by userId
	 */
	
	public List<Note> getAllNotesByUserId(String userId) {
		CriteriaBuilder builder = getSession().getCriteriaBuilder();
		CriteriaQuery<Note> criteriaQuery = builder.createQuery(Note.class);
		Root<Note> root = criteriaQuery.from(Note.class);
		criteriaQuery.select(root).where(builder.equal(root.get("createdBy"), userId));
		return getSession().createQuery(criteriaQuery).getResultList();

	}

	/*
	 * Retrieve details of a specific note
	 */
	
	public Note getNoteById(int noteId) throws NoteNotFoundException {
		try {
			Note note = getSession().get(Note.class, noteId);
			if (note == null) {
				throw new NoteNotFoundException("Note not found");
			}
			return note;
		} catch (Exception e) {
			throw new NoteNotFoundException("Note not found");
		}
	}

	/*
	 * Update an existing note
	 */

	public boolean UpdateNote(Note note) {
		boolean status = false; 
		try {
			if(getNoteById(note.getNoteId()) != null){
				getSession().update(note);
				getSession().flush();
				status = true;
			}
		} catch (HibernateException | NoteNotFoundException e) {
			status = false;
		}
		return status;

	}

}
