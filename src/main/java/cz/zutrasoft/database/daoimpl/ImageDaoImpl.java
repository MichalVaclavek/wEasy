package cz.zutrasoft.database.daoimpl;

import java.io.ByteArrayInputStream;
import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaQuery;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.zutrasoft.database.dao.ImageDao;
import cz.zutrasoft.database.model.Article;
import cz.zutrasoft.database.model.Category;
import cz.zutrasoft.database.model.Comment;
import cz.zutrasoft.database.model.Directory;
import cz.zutrasoft.database.model.Image;

public class ImageDaoImpl implements ImageDao
{
	static final Logger logger = LoggerFactory.getLogger(ImageDaoImpl.class);
	
	@Override
	public List<Image> getAllImages()
	{
		List<Image> images = new ArrayList<>();
	       
		SessionFactory factory = HibernateUtils.getSessionFactory();		 
        Session session = factory.getCurrentSession();
 
        try
        {                                 
           session.getTransaction().begin();
 	         	       
           CriteriaQuery<Image> cq = session.getCriteriaBuilder().createQuery(Image.class);
           cq.from(Image.class);
           
           images = (List<Image>)session.createQuery(cq).getResultList();
                               
           session.getTransaction().commit();
	    } 
        catch (Exception e)
	    {
	    	logger.error("Error retrieving images from DB. Exception: {}", e.getMessage());
	        session.getTransaction().rollback();
	    }
		
        return images;
	}


	@Override
	public List<Image> getAllImagesInDirectory(Directory directory)
	{
		List<Image> images = new ArrayList<Image>();
		 
		SessionFactory factory = HibernateUtils.getSessionFactory();		 
	    Session session = factory.getCurrentSession();

	    if (directory != null)
	    {
	    	try
		    {
				session.getTransaction().begin();
				
				String sql = "Select i from " + Image.class.getName() + " i "
			               	+ " Where i.directory.id = :id";
			    
				Query<Image> query = session.createQuery(sql);
			       
			    query.setParameter("id", directory.getId());
			       		    		 
			    images = query.getResultList();
			    
			    session.getTransaction().commit();
		    }
			catch (Exception e)
			{
				logger.error("Error finding images in Directory name {} . Exception: " + e.getMessage(), directory.getName());
				session.getTransaction().rollback();
			}
	    }
    	
		return images;
	}

	@Override
	public void saveImageFile(Image uploadedImageFile)
	{
		SessionFactory factory = HibernateUtils.getSessionFactory();		 
	    Session session = factory.getCurrentSession();
	    
	    if (uploadedImageFile != null)
	    {
	    	if (uploadedImageFile.getSaved() == null)
	    		uploadedImageFile.setSaved(new Timestamp(new Date().getTime()));
		    try
		    {
		        session.getTransaction().begin();	
		        		        
		        session.persist(uploadedImageFile);
		  	            
		        session.getTransaction().commit(); // ukonceni transakce
		        logger.info("Image inserted. Image id: {} ", uploadedImageFile.getFileName());
		     } 
		     catch (Exception e)
		     {
		    	 logger.error("Error saving image into DB. Exception: {}", e.getMessage());
		         session.getTransaction().rollback();
		     }
	    }
		
	}

	@Override
	public Image getImageById(int id)
	{
		Image image = null;
    	
    	SessionFactory factory = HibernateUtils.getSessionFactory();		 
	    Session session = factory.getCurrentSession();
    	
    	try
	    {
			session.getTransaction().begin();
			
			String sql = "Select i from " + Image.class.getName() + " i "
		               	+ " Where i.id = :id";
		    
			Query<Image> query = session.createQuery(sql);
		       
		    query.setParameter("id", id);
		       		    		 
		    image = query.getResultList().get(query.getFirstResult()); 
		    
		    session.getTransaction().commit();
	    }
		catch (Exception e)
		{
			logger.error("Error finding image by id, Id {} . Exception: " + e.getMessage(), id);
			session.getTransaction().rollback();
		}
                      
        return image;
	}

	@Override
	public void deleteImageById(int id)
	{
		SessionFactory factory = HibernateUtils.getSessionFactory();		 
	    Session session = factory.getCurrentSession();
 
    	try
    	{
    		session.getTransaction().begin();
	    	
	    	Object persistentInstance = session.load(Image.class, id);
	    	if (persistentInstance != null)
	    	{
	    		session.delete(persistentInstance);
	    		session.getTransaction().commit();
	    	}
    	}
    	catch (Exception e)
	    {
    		logger.error("Error deleting image from DB. Image id {}, Exception: {}", id, e.getMessage());    		
	    	session.getTransaction().rollback();
	    }
	}

	
}
