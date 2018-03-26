package cz.zutrasoft.base;

import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.markup.html.WebPage;

import cz.zutrasoft.external.pages.articlepage.ArticlePage;
import cz.zutrasoft.external.pages.homepage.HomePage;
import cz.zutrasoft.external.pages.loginpage.LoginPage;
import cz.zutrasoft.internal.pages.editwithmodal.EditPage;
import cz.zutrasoft.internal.pages.editwithmodal.ImageResourceReference;

/**
 * Application object for your web application.
 * 
 * The app. can be run with maven build function using goal jetty:run
 * Then http://localhost:8080 is the web link to start app.
 * 
 * @see cz.zutrasoft.Start#main(String[]) ??
 */
public class WicketApplication extends AuthenticatedWebApplication
{
	/**
	 * @see org.apache.wicket.Application#getHomePage()
	 */
	@Override
	public Class<? extends WebPage> getHomePage()
	{
		return HomePage.class;
	}

	/**
	 * @see org.apache.wicket.Application#init()
	 */
	@Override
	public void init()
	{
		super.init();
		
		// Mounting images. When calling url/webapp_name/images/id the appropriate image (image with id) will be served.
		mountResource("/images/${imageId}", new ImageResourceReference());
		
		mountPage("/article", ArticlePage.class);
		mountPackage("/edit", EditPage.class);
	}

	@Override
	protected Class<? extends AbstractAuthenticatedWebSession> getWebSessionClass()
	{
		return BasicAutorizationAndAuthenticationSession.class;
	}

	@Override
	protected Class<? extends WebPage> getSignInPageClass()
	{
		return LoginPage.class;
	}
	
	// DEPLOYMENT mode on
	@Override
	public RuntimeConfigurationType getConfigurationType()
	{
		return RuntimeConfigurationType.DEPLOYMENT;
	}		
	
	
}
