package cz.zutrasoft.database.daoimpl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.zutrasoft.database.dao.IUserProfileDao;
import cz.zutrasoft.database.model.UserProfile;
 
 
/**
* Prevzato z projektu SpringMVCSecureLogin. Jde o vyzuziti Hibernate pro ukladani UserProfile tridy
* do DB. Bez vyuziti Springu.
* 
* @author Michal Václavek
*/  
//@Repository("userProfileDao") // if Spring is used
public class UserProfileDaoImpl implements IUserProfileDao
{
	static final Logger logger = LoggerFactory.getLogger(UserProfileDaoImpl .class);
	
	@SuppressWarnings("unchecked")
    public UserProfile findById(int id)
    {
    	UserProfile up = null;
    	
    	SessionFactory factory = HibernateUtils.getSessionFactory();		 
	    Session session = factory.getCurrentSession();
    	
    	try
	    {
    		session.getTransaction().begin();
			
			String sql = "Select up from " + UserProfile.class.getName() + " up "
		               + " Where up.id = :id";
		    
			Query<UserProfile> query = session.createQuery(sql);	       
		    query.setParameter("id", id);
		       
		     if (query.getResultList().size() > 0)
		    	 up = query.getResultList().get(0);
		
		     session.getTransaction().commit();
	    }
		catch (Exception e)
		{
			logger.error(e.getMessage());
			session.getTransaction().rollback();
		}
    	
    	return up;
    }
 
    @SuppressWarnings("unchecked")
    public UserProfile findByType(String type)
    {
    	UserProfile up = null;
    	
    	SessionFactory factory = HibernateUtils.getSessionFactory();		 
	    Session session = factory.getCurrentSession();
    	
    	try
	    {
    		session.getTransaction().begin();
			
			String sql = "Select up from " + UserProfile.class.getName() + " up "
		               	+ " Where up.type = :type";
		    
			Query<UserProfile> query = session.createQuery(sql);		       
		    query.setParameter("type", type);
		       
		     if (query.getResultList().size() > 0)
		    	 up = query.getResultList().get(0);
		
		     session.getTransaction().commit();
	    }
		catch (Exception e)
		{
			logger.error(e.getMessage());
			session.getTransaction().rollback();
		}
    	
    	return up;
    }
     
    @SuppressWarnings("unchecked")
    public List<UserProfile> findAll()
    {
    	List<UserProfile> userProfiles = null;
    	
    	SessionFactory factory = HibernateUtils.getSessionFactory();		 
	    Session session = factory.getCurrentSession();
   	
    	try
    	{
    		session.getTransaction().begin();
		       
	        // Create an HQL statement, query the object.
	        String hql = "Select up from " + UserProfile.class.getName() + " up order by up.type asc";
	 
	        // Create Query object.
	        Query<UserProfile> query = session.createQuery(hql); 
	        // Execute query.
	        userProfiles = query.getResultList();
	        session.getTransaction().commit();
    	}
        catch (Exception e)
	    {
        	logger.error(e.getMessage());
	    	session.getTransaction().rollback();
	    }
         
        return userProfiles;
    }

    
}