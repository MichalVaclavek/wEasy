package cz.zutrasoft.database.daoimpl;

import java.sql.Connection;
//import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaQuery;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.zutrasoft.database.dao.IArticleDao;
import cz.zutrasoft.database.model.Article;
import cz.zutrasoft.database.model.Category;
import cz.zutrasoft.database.model.CategoryWithArticles;
import cz.zutrasoft.database.model.Comment;
import cz.zutrasoft.database.model.User;

//public class ArticleDaoImpl extends DaoImpl implements ArticleDao
public class ArticleDaoImpl implements IArticleDao
{

	static final Logger logger = LoggerFactory.getLogger(ArticleDaoImpl.class);
	
	@Override
	public List<Article> getAllArticles()
	{
        List<Article> list = new ArrayList<Article>();
            
        SessionFactory factory = HibernateUtils.getSessionFactory(); 		 
        Session session = factory.getCurrentSession();
     
        try
        {                                 
            session.getTransaction().begin();

            CriteriaQuery<Article> cq = session.getCriteriaBuilder().createQuery(Article.class);
            cq.from(Article.class);
            
            list = (List<Article>)session.createQuery(cq).getResultList();  			
           
            session.getTransaction().commit();
        } 
        catch (Exception e)
    	{
        	logger.error("Articles loading failed. Error: " + e.getMessage());
    	    session.getTransaction().rollback();
    	}
         
       return list;
    }

	@Override
	//public void saveArticle(Article newArticle)
	public Article saveArticle(Article newArticle)
    {    	
		SessionFactory factory = HibernateUtils.getSessionFactory(); 
	    Session session = factory.getCurrentSession();
	    
	    Article savedArtToRet = null;
	    
	    if (newArticle != null)
	    {
		    if (newArticle.getSaved() == null)
		    	newArticle.setSaved(new Timestamp(new Date().getTime()));
	 
		    try
		    {
				session.getTransaction().begin();									    		
	    		session.persist(newArticle);
	  	            
	    		session.getTransaction().commit();
	    		
	    		logger.info("Article saved.");
	    		savedArtToRet = newArticle;
	    	} 
		    catch (Exception e)
	    	{
	    		logger.error("Article saving failed. Error: " + e.getMessage());
	    		session.getTransaction().rollback();
	    	}
	    } 
	    
	    return savedArtToRet;
    }
		

	@Override
	public void updateArticle(Article article)
	{
		SessionFactory factory = HibernateUtils.getSessionFactory(); 
	    Session session = factory.getCurrentSession();
		
	    try
	    {
	    	session.getTransaction().begin();	
	    	session.saveOrUpdate(article);		
	    	session.flush();
		
	    	session.getTransaction().commit();
	    }
	    catch (Exception e)
	     {
	    	logger.error("Articles update in DB failed. Error: " + e.getMessage());
	        session.getTransaction().rollback();
	     }		
	}
	
	
	
	@Override
	public List<Article> getAllArticlesInCategory(Category category)
	{
		return getAllArticlesInCategory(category.getId());
	}
	
	
	@Override
	public Article getArticleById(int id)
	{
		Article article = null;
    	
    	SessionFactory factory = HibernateUtils.getSessionFactory();		 
	    Session session = factory.getCurrentSession();
    	
    	try
	    {
			session.getTransaction().begin();
			
			String sql = "Select a from " + Article.class.getName() + " a "
		               + " Where a.id = :id";
		    
			Query<Article> query = session.createQuery(sql);
		       
		    query.setParameter("id", id);
		    
		    article = query.getResultList().get(query.getFirstResult()); 
		    // ukonceni transakce
		    session.getTransaction().commit();
	    }
		catch (Exception e)
		{
			logger.error("Error finding article in DB. Article id {} . Exception: " + e.getMessage(), id);
			session.getTransaction().rollback();
		}
                      
        return article;
		
	}

	@Override
	public void deleteArticle(Article article)
	{
		SessionFactory factory = HibernateUtils.getSessionFactory();		 
	    Session session = factory.getCurrentSession();
 
    	try
    	{
    		session.getTransaction().begin();
	    	
	    	Object persistentInstance = session.load(Article.class, article.getId());
	    	if (persistentInstance != null)
	    	{
	    		session.delete(persistentInstance);
	    		session.getTransaction().commit();
	    	}
    	}
    	catch (Exception e)
	    {
	    	logger.error("Error deleting article from DB. Exception {} ", e.getMessage());
	    	session.getTransaction().rollback();
	    }
		
	}

	
	@Override
	public List<Article> getAllArticlesInCategory(long categoryId)
	{
		List<Article> articles = new ArrayList<Article>();
		 
		SessionFactory factory = HibernateUtils.getSessionFactory();		 
	    Session session = factory.getCurrentSession();
    	    	    
	    if (categoryId > 0)
	    {
	    	try
		    {
				session.getTransaction().begin();
				
				String sql = "Select a from " + Article.class.getName() + " a "
			               	+ " Where a.category.id = :id";
			    
				Query<Article> query = session.createQuery(sql);
			       
			    query.setParameter("id", categoryId);
			       		    		 
			    articles = query.getResultList();
			    
			    session.getTransaction().commit();
		    }
			catch (Exception e)
			{
				logger.error("Error finding Articles in Category id {} . Exception: " + e.getMessage(), categoryId);
				session.getTransaction().rollback();
			}
	    }
	    
		return articles;
	}
	
	
}
