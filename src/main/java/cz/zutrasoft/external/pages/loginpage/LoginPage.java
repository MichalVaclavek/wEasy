package cz.zutrasoft.external.pages.loginpage;

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.util.string.Strings;

import cz.zutrasoft.base.CustomMessageFP;
import cz.zutrasoft.base.ExactErrorLevelFilter;
import cz.zutrasoft.external.pages.ExternalBasePage;
import cz.zutrasoft.external.pages.homepage.HomePage;
import cz.zutrasoft.external.pages.registerpage.RegisterPage;

/**
 * Page to log-in a user to the application.
 * 
 * @author vitfo
 * @author Michal Václavek
 */
public class LoginPage extends ExternalBasePage
{
	private static final long serialVersionUID = 5097704449915649278L;
	
	private String username;
	private String password;
	
	@SuppressWarnings({ "serial" })
	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		
		add(new Link<Object>("register")
		{
			@Override
			public void onClick()
			{
				setResponsePage(RegisterPage.class);
			}
		});
		
		final CustomMessageFP feedback = new CustomMessageFP("feedback",  new ExactErrorLevelFilter(FeedbackMessage.ERROR));

		add(feedback);
		
		StatelessForm<Void> form = new StatelessForm<Void>("loginForm")
		{
			@Override
			protected void onSubmit()
			{
				if (Strings.isEmpty(username))
				{
					return;
				}
				boolean authResult = AuthenticatedWebSession.get().signIn(username, password);
				if (authResult)
				{
					setResponsePage(HomePage.class);
				} else
				{
					error(getString("form.login.failed"));
				}
			}
		};
		form.setDefaultModel(new CompoundPropertyModel<LoginPage>(this));
		add(form);
		
		form.add(new TextField<String>("username").setRequired(true));
		form.add(new PasswordTextField("password"));
		
		SubmitLink submit = new SubmitLink("submit", form);
		form.add(submit);
	}

	@Override
	protected String getTitle()
	{
		return getString("loginpage.title");
	}

}
