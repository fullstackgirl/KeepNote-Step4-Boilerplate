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

import com.stackroute.keepnote.exception.CategoryNotFoundException;
import com.stackroute.keepnote.model.Category;

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
public class CategoryDAOImpl implements CategoryDAO {

	/*
	 * Autowiring should be implemented for the SessionFactory.(Use
	 * constructor-based autowiring.
	 */
	private SessionFactory sessionFactory;

	@Autowired
	public CategoryDAOImpl(SessionFactory sessionFactory) {
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
	 * Create a new category
	 */
	public boolean createCategory(Category category) {
		boolean result = false;
		try {
			getSession().save(category);
			getSession().flush();
			result = true;
		} catch (HibernateException e) {
			result = false;
		}
		return result;

	}

	/*
	 * Remove an existing category
	 */
	public boolean deleteCategory(int categoryId) {
		boolean result = false;
		try {
			Category category = getSession().get(Category.class, categoryId);
			if (category != null) {
				getSession().delete(category);
				getSession().flush();
				result = true;
			}
		} catch (Exception e) {
			result = false;
		}
		return result;
	}
	/*
	 * Update an existing category
	 */

	public boolean updateCategory(Category category) {
		boolean status = false;
		try {
			if (getCategoryById(category.getCategoryId()) != null) {
				getSession().update(category);
				getSession().flush();
				status = true;
			}
		} catch (HibernateException | CategoryNotFoundException e) {
			status = false;
		}
		return status;

	}
	/*
	 * Retrieve details of a specific category
	 */

	public Category getCategoryById(int categoryId) throws CategoryNotFoundException {
		try {
			Category category = getSession().get(Category.class, categoryId);
			if (category == null) {
				throw new CategoryNotFoundException("Category not found");
			}
			return category;
		} catch (Exception e) {
			throw new CategoryNotFoundException("Category not found");
		}
	}

	/*
	 * Retrieve details of all categories by userId
	 */
	public List<Category> getAllCategoryByUserId(String userId) {
		CriteriaBuilder builder = getSession().getCriteriaBuilder();
		CriteriaQuery<Category> criteriaQuery = builder.createQuery(Category.class);
		Root<Category> root = criteriaQuery.from(Category.class);
		criteriaQuery.select(root).where(builder.equal(root.get("categoryCreatedBy"), userId));
		return getSession().createQuery(criteriaQuery).getResultList();

	}
}
