package cz.zutrasoft.base.services;

import java.util.List;

import cz.zutrasoft.database.model.ContactMessage;

public interface IContactMessageService
{
	/**
	 * Zakladni metoda pro vytvoreni oBjektu zpravy v kontaktu.<br>
	 * 
	 * userName a email je zadan bud ve formulari, pokud neni uzivatel prihlasen,
	 * jinak se doplni z dat prihlaseneho uzivatele.
	 * 
	 * @param userName
	 * @param userEmail
	 * @param message
	 */
	public void saveContactMessage(String userName, String userEmail, String message);

	public List<ContactMessage> getAllFromUser(String userName);
    public List<ContactMessage> getAllMessages();
}
