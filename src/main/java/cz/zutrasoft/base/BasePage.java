package cz.zutrasoft.base;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.wicket.Session;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebRequest;

import cz.zutrasoft.base.services.CategoryService;
import cz.zutrasoft.base.servicesimpl.CategoryServiceImpl;
import cz.zutrasoft.base.servicesimpl.TrackingInfoServiceImpl;
import cz.zutrasoft.database.model.Category;
import cz.zutrasoft.database.model.TrackInfo;
import cz.zutrasoft.external.menus.TopMenuPanel;
import cz.zutrasoft.external.panels.CategoryPanel;
import cz.zutrasoft.external.panels.CategoryPanelModel;

/**
 * Base class for all pages.
 * 
 * @author vitfo
 * @author Michal Václavek
 */
public abstract class BasePage extends WebPage
{
	private static final long serialVersionUID = 4809590366914866734L;
	
	private static CategoryService categoryService = CategoryServiceImpl.getInstance();

	public BasePage()
	{
		init();
	}

	public BasePage(IModel model)
	{
		super(model);
		init();
	}

	/**
	 * Method for initializing BasePage. 
	 * This method does not override org.apache.wicket.Page#init().
	 */
	private void init()
	{
		// Sets the page title
		add(new Label("title", getTitle()));
		
		// Calls method to save data about user.
		// If you do not want to track the activity, comment the line.
		trackUser();
		
		// Add top menu panel
		TopMenuPanel topMenuPanel = new TopMenuPanel("topMenu");
		
		// Model for displaying username of the logged-in user
		Model<String> strMdl = new Model<>();
					
		// Label with logged-in user info into TopMenu Panel
		final Label logedInUser = new Label("username", strMdl);
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
				// Display the username
				strMdl.setObject(getString("form.loggedinUser") + userName);
				logedInUser.setVisible(true);
			}
		}
		
		topMenuPanel.add(logedInUser);
		
		add(topMenuPanel);
				
		// List of all article's Categories
		List<Category> categories = categoryService.getAllCategories();
			
		add(new CategoryPanel("categoriesList", new CategoryPanelModel(categories))		
		{		
			@Override
			protected String getPanelTitleKey()
			{
				return("categories.label");
			}										
		});
		
		
		// Links to select localization between english nebo cs
        add(new Link<Void>("en")
        {
            @Override
            public void onClick()
            {
                Session.get().setLocale(Locale.ENGLISH);
            }
         });

         add(new Link<Void>("cs")
         {
             @Override
             public void onClick()
             {
                 Session.get().setLocale(new Locale("cs"));
            }
         });
		
	}

	/**
	 * Gets the title of the page.
	 * 
	 * @return - title
	 */
	protected abstract String getTitle();

	/**
	 * Tracks user's activity. The activity is saved to the database.
	 */
	protected void trackUser()
	{
		String ip, url, session;

		WebRequest req = (WebRequest) RequestCycle.get().getRequest();
		HttpServletRequest httpReq = (HttpServletRequest) req.getContainerRequest();
		ip = httpReq.getRemoteHost();
		url = httpReq.getRequestURL().toString();
		session = httpReq.getRequestedSessionId();

		TrackInfo info = new TrackInfo(ip, url, session);

		TrackingInfoServiceImpl trackingService = TrackingInfoServiceImpl.getInstance();
		
		trackingService.save(info);
	}
	
}
