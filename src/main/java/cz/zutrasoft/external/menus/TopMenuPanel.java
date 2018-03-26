package cz.zutrasoft.external.menus;

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import cz.zutrasoft.external.pages.contactpage.ContactPage;
import cz.zutrasoft.external.pages.homepage.HomePage;
import cz.zutrasoft.external.pages.loginpage.LoginPage;
import cz.zutrasoft.internal.pages.editwithmodal.EditPage;

/**
 * Displays main menu option links. Depends on if user is logged-in or not.
 *  
 * @author vitfo 
 * @author Michal Václavek
 */
public class TopMenuPanel extends Panel
{
	private static final long serialVersionUID = -5551067497749988801L;

	@SuppressWarnings({"serial"})
	public TopMenuPanel(String id)
	{
		super(id);
		this.
		add(new BookmarkablePageLink<Object>("homepage", HomePage.class));
		
		Link<?> loginLink = new Link<Object>("login")
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
		Link<?> administrationLink = new Link<Object>("administration")
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
		Link<?> logoutLink = new Link<Object>("logout")
		{
			@Override
			public void onClick()
			{
				AuthenticatedWebSession.get().invalidate();
								
				PageParameters pp = new PageParameters();
				pp.set("logout", "true");
				
				// setResponsePage(HomePage.class); could be used, but it did not work
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
		Link<?> contactLink = new Link<Object>("contact")
		{
			@Override
			public void onClick()
			{								
				setResponsePage(ContactPage.class);
			}
		
		};
		add(contactLink);						
	}
			

}
