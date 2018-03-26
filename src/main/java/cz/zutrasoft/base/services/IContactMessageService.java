package cz.zutrasoft.base.services;

import java.util.List;

import cz.zutrasoft.database.model.ContactMessage;

/**
 * Basic service methods to work with Contact messages sent by user.
 * 
 * @author Michal Václavek
 */
public interface IContactMessageService
{
	/**
	 * Basic method for saving message text as ContactMessage object.<br>
	 * 
	 * Name and email address of the author are ussualy enterd Contact message Form.
	 * 
	 * @param authorName - author (user) name of the message
	 * @param authorEmail - author's e-mail address
	 * @param message - text of the message to be saved
	 */
	public void saveContactMessage(String authorName, String authorEmail, String message);

	/**
	 * Gets list of all Contact messages from User of given username.
	 * 
	 * @param authorName name of author the Contact messages are required from.
	 * @return list of all Contact messages (instances of {@link ContactMessage} ) from author of the given authorName
	 */
	public List<ContactMessage> getAllFromUser(String authorName);
	
    public List<ContactMessage> getAllMessages();
}
