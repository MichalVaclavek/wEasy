package cz.zutrasoft.base;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.wicket.Session;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.link.ResourceLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.request.resource.SharedResourceReference;

import cz.zutrasoft.base.services.CategoryService;
import cz.zutrasoft.base.servicesimpl.CategoryServiceImpl;
import cz.zutrasoft.database.daoimpl.TrackingDaoImpl;
import cz.zutrasoft.database.model.Category;
import cz.zutrasoft.database.model.TrackInfo;
import cz.zutrasoft.external.menus.TopMenuPanel;
import cz.zutrasoft.external.pages.articlepage.ArticleModel;
import cz.zutrasoft.external.panels.CategoryPanel;
import cz.zutrasoft.external.panels.CategoryPanelModel;

/**
 * Base class for all pages.
 * 
 * @author vitfo
 */
public abstract class BasePage extends WebPage
{

	private static final long serialVersionUID = 4809590366914866734L;
	
	private static CategoryService categoryService = new CategoryServiceImpl();

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
		
		// add top menu panel
		add(new TopMenuPanel("topMenu"));
				
		//Label categoryLabel = new Label("categoriesLabel", new Model(getString("categories.label")));		
		//add(categoryLabel);

		List<Category> categories = categoryService.getAllCategories();
		
		//add(new CategoryPanel("categoriesList", new CategoryPanelModel(categories)));
		
		
		add(new CategoryPanel("categoriesList", new CategoryPanelModel(categories))		
		{		
			@Override
			protected String getPanelTitleKey()
			{
				//return(getString("categoriesPanel.title"));
				//return("categoriesPanel.title");
				return("categories.label");
				//return(getString("categories.label"));
				//return("");
			}										
		});
		
		
		// Odkazy pro zmenu lokalizace english nebo cs
        // v html kodu <a href="#" wicket:id="en">anglicky</a>
        add(new Link<Void>("en")
        {
            @Override
            public void onClick()
            {
                Session.get().setLocale(Locale.ENGLISH);
                //setTitleModelObject(); // Nastavi spravny zdroj dat modelu, pri kliknuti na lokalizacni odkaz
            }
         });

         add(new Link<Void>("cs")
         {
             @Override
             public void onClick()
             {
                 Session.get().setLocale(new Locale("cs"));
                 //getTitle(); // // Nastavi spravny zdroj dat modelu, pri kliknuti na lokalizacni odkaz
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
	 * Tracks users activity. The activity is saved to the database.
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

		TrackingDaoImpl trackingDao = new TrackingDaoImpl();
		trackingDao.save(info);
	}
	
}
