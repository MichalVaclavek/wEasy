package cz.zutrasoft.database.daoimpl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.criteria.CriteriaQuery;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.zutrasoft.database.dao.ICategoryDao;
import cz.zutrasoft.database.model.Article;
import cz.zutrasoft.database.model.Category;
import cz.zutrasoft.database.model.CategoryWithArticles;
import cz.zutrasoft.database.model.Comment;

public class CategoryDaoImpl implements ICategoryDao
{

	static final Logger logger = LoggerFactory.getLogger(CategoryDaoImpl.class);
	
	@Override
	public void saveCategory(String categoryName)
	{
		Category newCategory = new Category(categoryName);		
		saveCategory(newCategory);		
	}

	@Override
	public List<Category> getAllCategories()
	{
		List<Category> list = new ArrayList<Category>();
        
        SessionFactory factory = HibernateUtils.getSessionFactory(); 		 
        Session session = factory.getCurrentSession();
     
        try
        {                                
            session.getTransaction().begin();

            CriteriaQuery<Category> cq = session.getCriteriaBuilder().createQuery(Category.class);
            cq.from(Category.class);
            
            list = (List<Category>)session.createQuery(cq).getResultList();
  			           
            session.getTransaction().commit();
        } 
        catch (Exception e)
    	{
    	    //e.printStackTrace();
        	logger.error("Categories loading failed. Error: " + e.getMessage());
    	    // Rollback in case of an error occurred.
    	    session.getTransaction().rollback();
    	}
         
       return list;
	}

	@Override
	public Category getCategoryById(long id)
	{
		Category category = null;
    	
    	SessionFactory factory = HibernateUtils.getSessionFactory();		 
	    Session session = factory.getCurrentSession();
    	
    	try
	    {
			session.getTransaction().begin();
			
			String sql = "Select c from " + Category.class.getName() + " c "
		               	+ " Where c.id = :id";
		    
			Query<Category> query = session.createQuery(sql);
		       
		    query.setParameter("id", id);
		       		    		    		    
		    category = query.getResultList().get(query.getFirstResult()); 
		    
		    session.getTransaction().commit();
	    }
		catch (Exception e)
		{
			logger.error("Error loading Category id {} . Exception: " + e.getMessage(), id);
			session.getTransaction().rollback();
		}
                      
        return category;
	}
	
	@Override
	public Category getCategoryByName(String name)
	{
		Category category = null;
    	
    	SessionFactory factory = HibernateUtils.getSessionFactory();		 
	    Session session = factory.getCurrentSession();

    	try
	    {
			session.getTransaction().begin();
			
			String sql = "Select c from " + Category.class.getName() + " c "
		               	+ " Where c.name = :name";
		    
			Query<Category> query = session.createQuery(sql);
		       
		    query.setParameter("name", name);
		       		    		    		    
		    category = query.getResultList().get(query.getFirstResult()); 
		    
		    session.getTransaction().commit();
	    }
		catch (Exception e)
		{
			logger.error("Error loading Category name {} . Exception: " + e.getMessage(), name);
			session.getTransaction().rollback();
		}
                      
        return category;
	}

	@Override
	public void saveCategory(Category category)
	{
		SessionFactory factory = HibernateUtils.getSessionFactory(); 
	    Session session = factory.getCurrentSession();
 
	    try
	    {
			session.getTransaction().begin();
			
    		session.persist(category);
  	            
    		session.getTransaction().commit();
    		
    		logger.info("Category saved into DB.");
    	} 
	    catch (Exception e)
    	{
    		logger.error("Category saving into DB failed. Error: " + e.getMessage());
    		session.getTransaction().rollback();
    		throw e;  // to detect that Category saving failed
    	}   			
	}

	@Override
	public void delete(Category categorDel)
	{
		SessionFactory factory = HibernateUtils.getSessionFactory();		 
	    Session session = factory.getCurrentSession();
 
    	try
    	{
    		session.getTransaction().begin();
	    	
	    	Object persistentInstance = session.load(Category.class, categorDel.getId());
	    	if (persistentInstance != null)
	    	{
	    		session.delete(persistentInstance);
	    		session.getTransaction().commit();
	    	}
    	}
    	catch (Exception e)
	    {
	    	logger.error("Error deleting Category from DB. Exception {} ", e.getMessage());
	    	session.getTransaction().rollback();
	    }
		
	}

	@Override
	public List<CategoryWithArticles> getAllCategoriesWithArticles()
	{
		List<CategoryWithArticles> categoriesWithArticles = new ArrayList<>();
		List<Category> categories = getAllCategories();
	
		for (Category category : categories)
		{
			CategoryWithArticles cwa = new CategoryWithArticles();
			cwa.setCategory(category);
			cwa.setArticles(new ArticleDaoImpl().getAllArticlesInCategory(category));
			categoriesWithArticles.add(cwa);
		}
	
		return categoriesWithArticles;
	}

	
}
