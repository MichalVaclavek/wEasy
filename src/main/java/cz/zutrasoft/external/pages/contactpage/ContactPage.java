package cz.zutrasoft.external.pages.contactpage;

import org.apache.wicket.Session;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;

import cz.zutrasoft.base.BasePage;
import cz.zutrasoft.base.BasicAutorizationAndAuthenticationSession;
import cz.zutrasoft.base.services.CommentService;
import cz.zutrasoft.base.servicesimpl.CommentServiceImpl;


public class ContactPage extends BasePage
{
    //private WebMarkupContainer formContainer;
    
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
				
 		// Form for adding contact mesage.
 		Form form = new ContactForm("contactForm", userName, email);		
 		form.setOutputMarkupId(true); // ??

        add(form);
    }

	@Override
	protected String getTitle()
	{
		return getString("contactPage.title");
	}
    
    
}
