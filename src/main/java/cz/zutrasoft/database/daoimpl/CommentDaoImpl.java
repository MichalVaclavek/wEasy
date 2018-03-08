package cz.zutrasoft.database.daoimpl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

//import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.zutrasoft.database.dao.ICommentDao;
import cz.zutrasoft.database.model.Article;
import cz.zutrasoft.database.model.Comment;

public class CommentDaoImpl implements ICommentDao
{
	
	static final Logger logger = LoggerFactory.getLogger(CommentDaoImpl.class);
	
	@Override
	public void saveComment(Comment comment)
	{
		SessionFactory factory = HibernateUtils.getSessionFactory();		 
	    Session session = factory.getCurrentSession();
	       
	    try
	    {
	        session.getTransaction().begin();	 
	        session.persist(comment);
	  	            
	        session.getTransaction().commit();
	     } 
	     catch (Exception e)
	     {
	    	 logger.error("Error saving comment into DB. Exception: {}", e.getMessage());
	         session.getTransaction().rollback();
	     }
	 
	    // Zatim slouzi pro kontrolu, ve vyslednem kodu nemusi byt
	    logger.info("Comment inserted. User id: {} ", comment.getUserId());

	}

	
	@Override
	public List<Comment> getAllComments()
	{
		List<Comment> comments = new ArrayList<>();
		       
		SessionFactory factory = HibernateUtils.getSessionFactory();		 
        Session session = factory.getCurrentSession();
 
        try
        {                                 
           session.getTransaction().begin();
 	         	       
           String sql = "Select c from " + Comment.class.getName() + " c order by c.created desc";
    
           Query<Comment> query = session.createQuery(sql);
    
           comments = query.getResultList();
             
           session.getTransaction().commit();
	    } catch (Exception e)
	    {
	    	logger.error("Error retrieving commnets from DB. Exception: {}", e.getMessage());
	        session.getTransaction().rollback();
	    }
		
        return comments;
	}

	
	@Override
	public List<Comment> getAllCommentsFromUser(int userID)	
	{
		List<Comment> comments = new ArrayList<>();
        
		SessionFactory factory = HibernateUtils.getSessionFactory();		 
	    Session session = factory.getCurrentSession();
	 
	    try
	    {           	                    
	        session.getTransaction().begin();
	 	        	 	    	           
	        // Varianta, kdy neni pot5eba psat HQL prikaz sql = "Select c from " + Comment.class.getName() + " c order by c.email";
	        // Podle stackowvrflow.com - https://stackoverflow.com/questions/39415703/alternative-code-for-createcriteria-method-in-hibernate-5-2-2
	        CriteriaBuilder cb = session.getCriteriaBuilder();
	        CriteriaQuery<Comment> cq = cb.createQuery(Comment.class);
	        Root<Comment> comms = cq.from(Comment.class);

	        cq.select(comms).where(cb.equal(comms.get("userId"), userID));
	        
	        comments = (List<Comment>)session.createQuery(cq).getResultList();
	 
	        logger.info("All comments from userID: {}", userID);
	        
	        /*
	        for (Comment comm : comments)
	        {
	        	logger.info("Comment from user id: {} and for article id: {}", comm.getUserId() , comm.getArticle().getHeader());
	        }
	        logger.info("======================================");
	        */

	         session.getTransaction().commit();
	     } catch (Exception e)
	     {
	    	 logger.error("Error retrieving commnets of user id {} from DB. Exception: {}", userID, e.getMessage());
	         session.getTransaction().rollback();
	     }
		
        return comments;
	}

	
	@Override
	public List<Comment> getAllCommentsForArticleId(int artId)
	{
		List<Comment> articles = new ArrayList<Comment>();
		 
		SessionFactory factory = HibernateUtils.getSessionFactory();		 
	    Session session = factory.getCurrentSession();
    	
    	try
	    {
			session.getTransaction().begin();
			
			String sql = "Select c from " + Comment.class.getName() + " c "
		               	+ " Where c.article.id = :id";
		    
			Query<Comment> query = session.createQuery(sql);
		       
		    query.setParameter("id", artId);
		       		    		 
		    articles = query.getResultList();
		    
		    session.getTransaction().commit();
	    }
		catch (Exception e)
		{
			logger.error("Chyba při hledání komentařů k Article.id {} . Exception: {}", artId, e.getMessage());
			session.getTransaction().rollback();
		}
	    
	    
		return articles;
	}

	@Override
	public List<Comment> getAllCommentsForArticle(Article a)
	{
		if (a != null)
			return getAllCommentsForArticleId(a.getId());
		else
			return null;
	}


	@Override
	public void deleteComment(Comment comment)
	{
		SessionFactory factory = HibernateUtils.getSessionFactory();		 
	    Session session = factory.getCurrentSession();
 
    	try
    	{
    		session.getTransaction().begin();
	    	
	    	Object persistentInstance = session.load(Comment.class, comment.getId());
	    	if (persistentInstance != null)
	    	{
	    		session.delete(persistentInstance);
	    		session.getTransaction().commit();
	    	}
    	}
    	catch (Exception e)
	    {
    		logger.error("Error deleting commnet from DB. Comment id {}, Exception: {}", comment.getId(), e.getMessage());

	    	session.getTransaction().rollback();
	    }
		
	}


	@Override
	public Comment getById(int id)
	{
		Comment comment = null;
    	
    	SessionFactory factory = HibernateUtils.getSessionFactory();		 
	    Session session = factory.getCurrentSession();
    	
    	try
	    {
			session.getTransaction().begin();
			
			String sql = "Select c from " + Comment.class.getName() + " c "
		               	+ " Where c.id = :id";
		    
			Query<Comment> query = session.createQuery(sql);
		       
		    query.setParameter("id", id);
		       		    		 
		    comment = query.getResultList().get(query.getFirstResult()); 
		    
		    session.getTransaction().commit();
	    }
		catch (Exception e)
		{
			logger.error("Error finding comment by id, Id {} . Exception: " + e.getMessage(), id);
			session.getTransaction().rollback();
		}
                      
        return comment;
	}
	
	
}
