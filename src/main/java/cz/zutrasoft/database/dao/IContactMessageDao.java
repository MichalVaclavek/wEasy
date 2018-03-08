package cz.zutrasoft.database.dao;

import java.util.List;

import cz.zutrasoft.database.model.ContactMessage;

public interface IContactMessageDao
{
	public void saveContactMessage(ContactMessage message);
	//public ContactMessage findByEmail(String email);
	public List<ContactMessage> getAllFromUser(String userName);
    public List<ContactMessage> getAllMessages();
}
