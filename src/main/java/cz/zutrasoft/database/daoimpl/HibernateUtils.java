package cz.zutrasoft.database.daoimpl;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utilities to open connection to DB using Hibernate.
 *
 * @author Michal Václavek
 *
 */
public class HibernateUtils
{
	static final Logger logger = LoggerFactory.getLogger(HibernateUtils.class);
	
    private static final SessionFactory sessionFactory = buildSessionFactory();
 
    // Hibernate 5: Opening connection to DB.
    private static SessionFactory buildSessionFactory()
    {
        try
        {
            // Create the ServiceRegistry from hibernate.cfg.xml
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()//
                    .configure("hibernate.cfg.xml").build();
 
            // Create a metadata sources using the specified service registry.
            Metadata metadata = new MetadataSources(serviceRegistry).getMetadataBuilder().build();
 
            return metadata.getSessionFactoryBuilder().build();
        } catch (Throwable ex)
        {
        	logger.error("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
 
    public static SessionFactory getSessionFactory()
    {
        return sessionFactory;
    }
 
    public static void shutdown()
    {
        getSessionFactory().close();
    }

    
}
