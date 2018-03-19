package cz.zutrasoft.base.servicesimpl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import cz.zutrasoft.base.services.IContactMessageService;
import cz.zutrasoft.database.dao.IContactMessageDao;
import cz.zutrasoft.database.daoimpl.ContactMessageDaoImpl;
import cz.zutrasoft.database.model.ContactMessage;

public class ContactMessageServiceImpl implements IContactMessageService
{
	private static IContactMessageDao cmDao = new ContactMessageDaoImpl();
	
	private static class SingletonHolder
	{
        private static final ContactMessageServiceImpl SINGLE_INSTANCE = new ContactMessageServiceImpl();
    }
	
	/**
	 * @return singleton instance of the ContactMessageServiceImpl
	 */
	public static ContactMessageServiceImpl getInstance()
	{				
		return SingletonHolder.SINGLE_INSTANCE;			
	}
	
	private ContactMessageServiceImpl()
	{}
	
	@Override
	public void saveContactMessage(String userName, String userEmail, String message)
	{
		// Vytvoření nového komentáře k ulozeni
		ContactMessage cMessage = new ContactMessage();
		
		cMessage.setUserName(userName);
		cMessage.setEmail(userEmail);
		cMessage.setCreatedTime(new Timestamp(new Date().getTime()));
		cMessage.setTextOfMessage(message);
		             
        cmDao.saveContactMessage(cMessage);
	}


	@Override
	public List<ContactMessage> getAllFromUser(String userName)
	{
		return cmDao.getAllFromUser(userName);
	}


	@Override
	public List<ContactMessage> getAllMessages()
	{
		return cmDao.getAllMessages();
	}

}
