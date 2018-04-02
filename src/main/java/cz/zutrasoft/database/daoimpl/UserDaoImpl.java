package cz.zutrasoft.database.daoimpl;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.zutrasoft.database.dao.IUserDao;
import cz.zutrasoft.database.model.User;

public class UserDaoImpl implements IUserDao
{

	static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);
    
	@SuppressWarnings("unchecked")
    @Override
    public User findById(Integer id)
    {
        User user = null;
    	
    	SessionFactory factory = HibernateUtils.getSessionFactory();		 
	    Session session = factory.getCurrentSession();
    	
    	try
	    {
			session.getTransaction().begin();
			
			String sql = "Select u from " + User.class.getName() + " u "
		               	+ " Where u.id = :id";
		    
			Query<User> query = session.createQuery(sql);		       
		    query.setParameter("id", id);
		       
		     if (query.getResultList().size() > 0)
		    	 user = query.getResultList().get(0);
		
		     if (user != null)
		     {
		         Hibernate.initialize(user.getUserProfiles());
		     }
		     
		     session.getTransaction().commit();
	    }
		catch (Exception e)
		{
			logger.error(e.getMessage());
			session.getTransaction().rollback();
		}
            	
        return user;
    }
    
    /**
     * Find user according username.
     * 
     * @param username of the User to get from DB.
     * @return User object corresponding to username
     */
	@SuppressWarnings("unchecked")
    @Override
    public User findByUsername(String username)
    {
        User user = null;
    	      
    	SessionFactory factory = HibernateUtils.getSessionFactory();		 
	    Session session = factory.getCurrentSession();
    	
    	try
	    {
			session.getTransaction().begin();
			
			String sql = "Select u from " + User.class.getName() + " u "
		               	+ " Where u.username = :username";
		    
			Query<User> query = session.createQuery(sql);		       
		    query.setParameter("username", username);
		       
		    if (query.getResultList().size() > 0)
		    	user = query.getResultList().get(0);
				    
		    if(user != null)
	        {
	            Hibernate.initialize(user.getUserProfiles());
	        }
		    
		    session.getTransaction().commit();
	    }
		catch (Exception e)
		{
			logger.error("Username : {} failed to find. Error: " + e.getMessage(), username);
			session.getTransaction().rollback();
		}
		                   
        return user;
    }
    
    
    @SuppressWarnings("unchecked")
    public List<User> findAllUsers()
    {
    	List<User> users = null;
    	
    	SessionFactory factory = HibernateUtils.getSessionFactory();		 
	    Session session = factory.getCurrentSession();
    	
    	try
    	{
    		session.getTransaction().begin();

	        String sql = "Select u from " + User.class.getName() + " u order by u.username asc";
	 
	        // Create Query object.
	        Query<User> query = session.createQuery(sql);	 
	        // Execute query.
	        users = query.getResultList();
	        session.getTransaction().commit();
	        	        	        	
    	}
        catch (Exception e)
	    {
        	logger.error(e.getMessage());
	    	session.getTransaction().rollback();
	    }
         
        return users;
    }
    
    @Override
    public void save(User user)
    {
    	SessionFactory factory = HibernateUtils.getSessionFactory();		 
	    Session session = factory.getCurrentSession();
	    	           
    	try
    	{
    		session.getTransaction().begin();   		
    		session.persist(user);    		
    		session.getTransaction().commit();
    	}
    	catch (Exception e)
	    {
	    	logger.error("UserDaoImp.save() error: {}", e.getMessage());
	    	session.getTransaction().rollback();
	    }
    }
    
    @Override
	public void update(User user)
	{
    	SessionFactory factory = HibernateUtils.getSessionFactory();		 
	    Session session = factory.getCurrentSession();
	    	           
    	try
    	{
    		session.getTransaction().begin();    		
    		session.saveOrUpdate(user);   		
    		session.getTransaction().commit();
    	}
    	catch (Exception e)
	    {
	    	logger.error("UserDaoImp.save() error: {}", e.getMessage());
	    	session.getTransaction().rollback();
	    }		
	}
    
 
    @Override
    public void deleteByUserId(Integer userId)
    {        
    	SessionFactory factory = HibernateUtils.getSessionFactory();		 
	    Session session = factory.getCurrentSession();
 
    	try
    	{
    		session.getTransaction().begin();
	    	
	    	Object persistentInstance = session.load(User.class, userId); // ocekava se id
	    	if (persistentInstance != null)
	    	{
	    		session.delete(persistentInstance);
	    		session.getTransaction().commit();
	    	}
    	}
    	catch (Exception e)
	    {
    		logger.error(e.getMessage());
	    	session.getTransaction().rollback();
	    }        
    }
	
	
}
