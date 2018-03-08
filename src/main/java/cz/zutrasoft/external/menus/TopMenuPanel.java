package cz.zutrasoft.external.menus;

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import cz.zutrasoft.base.BasicAutorizationAndAuthenticationSession;
import cz.zutrasoft.external.pages.contactpage.ContactPage;
import cz.zutrasoft.external.pages.homepage.HomePage;
import cz.zutrasoft.external.pages.loginpage.LoginPage;
import cz.zutrasoft.internal.pages.editwithmodal.EditPage;

/**
 * Displays main menu option links. Depends on a user is logged-in or not.
 *  
 * @author vitfo 
 * @author Michal Václavek
 *
 */
public class TopMenuPanel extends Panel
{

	public TopMenuPanel(String id)
	{
		super(id);

		add(new BookmarkablePageLink("homepage", HomePage.class));
		
		Link loginLink = new Link("login")
		{
			@Override
			public void onClick()
			{
				setResponsePage(LoginPage.class);
			}

			@Override
			protected void onConfigure()
			{
				super.onConfigure();
				setVisible(!AuthenticatedWebSession.get().isSignedIn());
			}
		};
		add(loginLink);
		
		// Administration link.
		Link administrationLink = new Link("administration")
		{
			@Override
			public void onClick()
			{
				setResponsePage(EditPage.class);
			}

			@Override
			protected void onConfigure()
			{
				super.onConfigure();
				setVisible(AuthenticatedWebSession.get().getRoles().hasRole(Roles.ADMIN));
			}
		};
		add(administrationLink);

		// Log out link.
		Link logoutLink = new Link("logout")
		{
			@Override
			public void onClick()
			{
				AuthenticatedWebSession.get().invalidate();
								
				PageParameters pp = new PageParameters();
				pp.set("logout", "true");
				
				// Nevim proc, ale setResponsePage(HomePage.class); nefunguje, vubec se nezavola konstruktor HomePage
				// Musi se volat s PageParameters, ktere nesmi byt prazdne!
				setResponsePage(HomePage.class, pp);
			}

			@Override
			protected void onConfigure()
			{
				super.onConfigure();
				setVisible(AuthenticatedWebSession.get().isSignedIn());
			}
		};
		add(logoutLink);
		
		
		// Contact message link.
		Link contactLink = new Link("contact")
		{
			@Override
			public void onClick()
			{								
				setResponsePage(ContactPage.class);
			}
		
		};
		add(contactLink);
				
		// Model for displaying username of the logged-in user
		Model<String> strMdl = new Model<>();
					
		Label logedInUser = new Label("username", strMdl);
		logedInUser.setVisible(false);
		logedInUser.setOutputMarkupId(true);
		add(logedInUser);
		
		BasicAutorizationAndAuthenticationSession session = null;
		if (AuthenticatedWebSession.get() instanceof BasicAutorizationAndAuthenticationSession)
		{
			session = (BasicAutorizationAndAuthenticationSession) AuthenticatedWebSession.get();
		}
		
		if (session != null)
		{
			if (session.isSignedIn())
			{
				String userName = session.getUsername();
				strMdl.setObject(getString("form.loggedinUser") + userName);
				logedInUser.setVisible(true);
			}
		}				
	}
	

}
