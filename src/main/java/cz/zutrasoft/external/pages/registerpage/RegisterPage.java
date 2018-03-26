package cz.zutrasoft.external.pages.registerpage;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.validation.validator.EmailAddressValidator;

import cz.zutrasoft.base.CustomMessageFP;
import cz.zutrasoft.base.ExactErrorLevelFilter;
import cz.zutrasoft.base.services.IUserService;
import cz.zutrasoft.base.servicesimpl.UserService;
import cz.zutrasoft.database.model.User;
import cz.zutrasoft.external.pages.ExternalBasePage;
import cz.zutrasoft.external.pages.homepage.HomePage;
import cz.zutrasoft.external.pages.loginpage.LoginPage;

/**
 * Page to register a new User
 * 
 * @author vitfo
 * @author Michal Václavek
 *
 */
public class RegisterPage extends ExternalBasePage
{	
	private static final long serialVersionUID = 4587259321137833297L;
	
	private String username;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private String passwordCheck;
	
	private AjaxSubmitLink submit;
	
	@SuppressWarnings({ "serial" })
	public RegisterPage()
	{
        final CustomMessageFP feedbackErr = new CustomMessageFP("errorFeedback", new ExactErrorLevelFilter(FeedbackMessage.ERROR));

		feedbackErr.setOutputMarkupId(true);
		add(feedbackErr);
			
		final StatelessForm<Void> form = new StatelessForm<Void>("registerForm")
		{
			@Override
			protected void onSubmit()
			{
				User user = new User();
				user.setUsername(username);
				user.setEmail(email);
				user.setPassword(password);
				user.setFirstName(firstName);
				user.setLastName(lastName);
								
				IUserService userService = UserService.getInstance();
				
				if (userService.saveUser(user))	
				{
					AuthenticatedWebSession.get().signIn(username, password);
					setResponsePage(HomePage.class); // Go to HomePage after successful registration
				}
				else
					setResponsePage(LoginPage.class);					
			}
		};
		
		form.setDefaultModel(new CompoundPropertyModel<RegisterPage>(this));
		add(form);
		
		form.add(new TextField<String>("username").setRequired(true));
		form.add(new TextField<String>("firstName").setRequired(false));
		form.add(new TextField<String>("lastName").setRequired(false));
		
		TextField<String> emailTF = new TextField<String>("email");
		emailTF.add(EmailAddressValidator.getInstance());
		emailTF.setRequired(true); // Email required
		form.add(emailTF);
		
		form.add(new PasswordTextField("password"));
		form.add(new PasswordTextField("passwordCheck"));
			
		submit = new AjaxSubmitLink("submit", form)
		{
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				if (password != null &&
					passwordCheck != null &&
					password.equals(passwordCheck))
				{				
					super.onSubmit(target, form);

				} else
				{
					error(getString("form.notEquals"));
					target.add(feedbackErr);
				}
			}
						
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form)
			{
				super.onError(target, form);
				target.add(feedbackErr);
			}
		};
					
		submit.setOutputMarkupId(true);
		form.add(submit);
	}

	@Override
	protected String getTitle()
	{
		return getString("registerpage.title");
	}
	
	
}
