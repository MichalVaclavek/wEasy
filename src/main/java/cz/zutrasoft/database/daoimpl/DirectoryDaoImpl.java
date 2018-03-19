package cz.zutrasoft.database.daoimpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaQuery;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.zutrasoft.database.dao.IDirectoryDao;
import cz.zutrasoft.database.model.Category;
import cz.zutrasoft.database.model.Comment;
import cz.zutrasoft.database.model.Directory;

public class DirectoryDaoImpl implements IDirectoryDao
{

	static final Logger logger = LoggerFactory.getLogger(DirectoryDaoImpl.class);
	

	@Override
	public List<Directory> getAllDirectories()
	{
		List<Directory> list = new ArrayList<Directory>();
        
        SessionFactory factory = HibernateUtils.getSessionFactory(); 		 
        Session session = factory.getCurrentSession();
     
        try
        {                               
            session.getTransaction().begin();

            CriteriaQuery<Directory> cq = session.getCriteriaBuilder().createQuery(Directory.class);
            cq.from(Directory.class);
            
            list = (List<Directory>)session.createQuery(cq).getResultList();
  			           
            session.getTransaction().commit();
        } 
        catch (Exception e)
    	{
        	logger.error("Directories loading failed. Error: " + e.getMessage());
    	    session.getTransaction().rollback();
    	}
         
       return list;
	}

	@Override
	public List<Directory> getAllDirectoriesForCategory(long categoryId)
	{
		List<Directory> directories = new ArrayList<Directory>();
		 
		SessionFactory factory = HibernateUtils.getSessionFactory();		 
	    Session session = factory.getCurrentSession();
    	
    	try
	    {
			session.getTransaction().begin();
			
			String sql = "Select dir from " + Directory.class.getName() + " dir "
		               	+ " Where dir.category.id = :id";
		    
			Query<Directory> query = session.createQuery(sql);
		       
		    query.setParameter("id", categoryId);
		       		    		 
		    directories = query.getResultList();

		    session.getTransaction().commit();
	    }
		catch (Exception e)
		{
			logger.error("Error finding Directories for Category id = {} . Exception: {}", categoryId, e.getMessage());
			session.getTransaction().rollback();
		}
    	
		return directories;
	}
	
	@Override
	public List<Directory> getAllDirectoriesForCategory(Category category)
	{
		return getAllDirectoriesForCategory(category.getId());
	}
	
	
	@Override
	public void saveDirectory(Directory directory)
	{
		SessionFactory factory = HibernateUtils.getSessionFactory(); 
	    Session session = factory.getCurrentSession();
 
	    try
	    {
			session.getTransaction().begin();
								
    		session.persist(directory);
  	            
    		session.getTransaction().commit(); // ukonceni transakce
    		
    		logger.info("Directory saved into DB. {}", directory.getName());
    	} 
	    catch (Exception e)
    	{
    		logger.error("Directory saving into DB failed. Error: " + e.getMessage());
    		session.getTransaction().rollback();
    		throw e; // to detect that Directory saving failed
    	}
		
	}

	@Override
	public Directory getFirstDirectoryByName(String dirName)
	{
		Directory directory = null;
    	
    	SessionFactory factory = HibernateUtils.getSessionFactory();		 
	    Session session = factory.getCurrentSession();
    	
    	try
	    {
			session.getTransaction().begin();
			
			String sql = "Select d from " + Directory.class.getName() + " d "
		               	+ " Where d.name = :name";
		    
			Query<Directory> query = session.createQuery(sql);
		       
		    query.setParameter("name", dirName);
		       		    		    		    
		    directory = query.getResultList().get(query.getFirstResult()); 
		    
		    session.getTransaction().commit();
	    }
		catch (Exception e)
		{
			logger.error("Error loading Directory name {} . Exception: " + e.getMessage(), dirName);
			session.getTransaction().rollback();
		}
                      
        return directory;
	}
	
	@Override
	public Directory getDirectoryFromCategory(String directoryName, Category category)
	{
		Directory directory = null;
		
		SessionFactory factory = HibernateUtils.getSessionFactory();		 
	    Session session = factory.getCurrentSession();
 
    	try
    	{
    		session.getTransaction().begin();
		
    		String sql = "Select dir from " + Directory.class.getName() + " dir "
    					+ " Where dir.category.id = :cid AND dir.name = :dname";
    
    		Query<Directory> query = session.createQuery(sql);
       
    		query.setParameter("cid", category.getId());
    		query.setParameter("dname", directoryName);
       		    		 
    		//directory = query.getFirstResult();
    		directory = query.getResultList().get(query.getFirstResult());
    	
    		session.getTransaction().commit();
	    }
		catch (Exception e)
		{
			logger.error("Error loading Directory name {} . Exception: " + e.getMessage(), directoryName);
			session.getTransaction().rollback();
		}
                      
        return directory;
	}
	

	@Override
	public void delete(Directory directoryDel)
	{
		SessionFactory factory = HibernateUtils.getSessionFactory();		 
	    Session session = factory.getCurrentSession();
 
    	try
    	{
    		
    		session.getTransaction().begin();
	    	
	    	Object persistentInstance = session.load(Directory.class, directoryDel.getId());
	    	if (persistentInstance != null)
	    	{
	    		session.delete(persistentInstance);
	    		session.getTransaction().commit();
	    	}
    	}
    	catch (Exception e)
	    {
	    	logger.error("Error deleting Directory from DB. Exception {} ", e.getMessage());
	    	session.getTransaction().rollback();
	    }
		
	}

	@Override
	public void delete(String directoryName, Category category)
	{
		Directory dirForDelete = getDirectoryFromCategory(directoryName, category);
		
		if (dirForDelete != null)
		{
		
			SessionFactory factory = HibernateUtils.getSessionFactory();		 
			Session session = factory.getCurrentSession();
 
	    	try
	    	{	    		
	    		session.getTransaction().begin();	    	   		
	    		session.delete(dirForDelete);
		    	session.getTransaction().commit();	    			    		
		    	
	    	}
	    	catch (Exception e)
		    {
		    	logger.error("Error deleting Directory from DB. Exception {} ", e.getMessage());
		    	session.getTransaction().rollback();
		    }
		}
		
	}

	

	/*
	@Override
	public void delete(String directoryName, String categoryName)
	{
		// TODO Auto-generated method stub
		
	}
	*/
	
	
}
