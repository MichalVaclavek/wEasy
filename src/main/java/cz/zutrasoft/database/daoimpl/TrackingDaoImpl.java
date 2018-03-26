package cz.zutrasoft.database.daoimpl;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.zutrasoft.database.dao.ITrackingDao;
import cz.zutrasoft.database.model.TrackInfo;

public class TrackingDaoImpl implements ITrackingDao
{
	static final Logger logger = LoggerFactory.getLogger(TrackingDaoImpl.class);
	
	@Override
	public void save(TrackInfo info)
	{
		SessionFactory factory = HibernateUtils.getSessionFactory();		 
	    Session session = factory.getCurrentSession();
	       
	    try
	    {
	        session.getTransaction().begin();	 
	        session.persist(info);
	  	            
	        session.getTransaction().commit(); 
	     } 
	     catch (Exception e)
	     {
	    	 logger.error("Error saving comment into DB. Exception: {}", e.getMessage());
	         session.getTransaction().rollback();
	     }	 		
	}
	
	
}
