package cz.zutrasoft.external.pages.contactpage;

import org.apache.wicket.Session;
import org.apache.wicket.markup.html.form.Form;

import cz.zutrasoft.base.BasePage;
import cz.zutrasoft.base.BasicAutorizationAndAuthenticationSession;

/**
 * Page with Form to allow sent a message to the web admin - so called Contact message
 * 
 * @author Michal Václavek
 */
public class ContactPage extends BasePage
{
	private static final long serialVersionUID = -3099704410092725502L;

	@SuppressWarnings("rawtypes")
	public ContactPage()
    {   	
		String userName = "";
		String email = "";
		BasicAutorizationAndAuthenticationSession session = (BasicAutorizationAndAuthenticationSession)Session.get();

		if (session.isSignedIn())
		{
			userName = session.getUsername();
			email = session.getEmail();
		}
				
 		// Form for editing contact message.
 		Form form = new ContactForm("contactForm", userName, email);		
 		form.setOutputMarkupId(true); 

        add(form);
    }

	@Override
	protected String getTitle()
	{
		return getString("contactPage.title");
	}
    
    
}
