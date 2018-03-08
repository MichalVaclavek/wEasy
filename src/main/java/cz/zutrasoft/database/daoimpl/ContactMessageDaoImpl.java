package cz.zutrasoft.database.daoimpl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.zutrasoft.database.dao.IContactMessageDao;
import cz.zutrasoft.database.model.Comment;
import cz.zutrasoft.database.model.ContactMessage;

public class ContactMessageDaoImpl implements IContactMessageDao
{
	static final Logger logger = LoggerFactory.getLogger(ContactMessageDaoImpl.class);
	
	@Override
	public void saveContactMessage(ContactMessage message)
	{
		SessionFactory factory = HibernateUtils.getSessionFactory();		 
		Session session = factory.getCurrentSession();
       
		try
		{
           session.getTransaction().begin();
 
           session.persist(message);

           session.getTransaction().commit();
		} catch (Exception e)
		{
			logger.error("Error saving contact message: {}", e);
			session.getTransaction().rollback();
		}
	}

	@Override
	public List<ContactMessage> getAllMessages()
	{
		List<ContactMessage> messages = new ArrayList<>();
        
		SessionFactory factory = HibernateUtils.getSessionFactory();
		 
		Session session = factory.getCurrentSession();
 
		try
		{                               
           session.getTransaction().begin();
 	        	 	    
           CriteriaQuery<ContactMessage> cq = session.getCriteriaBuilder().createQuery(ContactMessage.class);
           cq.from(ContactMessage.class);

           messages = (List<ContactMessage>)session.createQuery(cq).getResultList();
           
           session.getTransaction().commit();
		} catch (Exception e)
		{
    	   logger.error("Error retrieving contact messages: {}", e);
           session.getTransaction().rollback();
		}
		
        return messages;
	}

	
	@Override
	public List<ContactMessage> getAllFromUser(String userName)
	{
		List<ContactMessage> contactMessages = new ArrayList<>();
        
		SessionFactory factory = HibernateUtils.getSessionFactory();		 
	    Session session = factory.getCurrentSession();
	 
	    try
	    {           	                    
	        session.getTransaction().begin();
	 	        	 	    	           
	        CriteriaBuilder cb = session.getCriteriaBuilder();
	        CriteriaQuery<ContactMessage> cq = cb.createQuery(ContactMessage.class);
	        Root<ContactMessage> cms = cq.from(ContactMessage.class);

	        cq.select(cms).where(cb.equal(cms.get("userName"), userName));
	        
	        contactMessages = (List<ContactMessage>)session.createQuery(cq).getResultList();
	 	        
	         session.getTransaction().commit();
	     } catch (Exception e)
	     {
	    	 logger.error("Error retrieving contact messages for user: " + userName + " Exception: " + e);
	         session.getTransaction().rollback();
	     }
		
        return contactMessages;
	}
	
}
