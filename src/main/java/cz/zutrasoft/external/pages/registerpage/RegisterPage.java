package cz.zutrasoft.external.pages.registerpage;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
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
 * Page to register a new User or to update data of the logged-in User.
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
	
	private User loggedInUser;
	private String loggedInUserName;
		
	private AjaxSubmitLink submit;
	
	@SuppressWarnings({ "serial" })
	public RegisterPage(final PageParameters pp) // Zde se ocekava username pro pripad, ze jde o editaci stavajiciho usera
	{
		IUserService userService = UserService.getInstance();
		
		loggedInUser = null;
					
		if (pp != null)
        {
			loggedInUserName = pp.get(0).toString();
			loggedInUser = userService.findByUsername(loggedInUserName);
        	
        	if (loggedInUser != null)
        	{
        		username = loggedInUser.getUsername();
        		firstName = loggedInUser.getFirstName();
        		lastName = loggedInUser.getLastName();
        		email = loggedInUser.getEmail();       		
        	}
        }
								
		final CustomMessageFP feedbackErr = new CustomMessageFP("errorFeedback", new ExactErrorLevelFilter(FeedbackMessage.ERROR));

		feedbackErr.setOutputMarkupId(true);
		add(feedbackErr);
			
		final StatelessForm<Void> form = new StatelessForm<Void>("registerForm")
		{						
			@Override
			protected void onSubmit()
			{
				IUserService userService = UserService.getInstance();
				
				User loggedInUser = null;
				
				if ((loggedInUserName != null) && loggedInUserName.equals(username)) 	// already logged-in user to update it's data?																													
				{
					loggedInUser = userService.findByUsername(username); // get logged-in user instance
					
					loggedInUser.setEmail(email);
					if ((password != null))
						loggedInUser.setPassword(password);
					
					loggedInUser.setFirstName(firstName);					
					loggedInUser.setLastName(lastName);	
					
					userService.updateUser(loggedInUser);
					
					setResponsePage(HomePage.class); // Go to HomePage after successful user data update
				} else 					
				{
					// new user to be created. username has been already checked for duplicity within this Form 
					User user = new User();
					
					user.setUsername(username);
					user.setEmail(email);
					user.setPassword(password);
					user.setFirstName(firstName);
					user.setLastName(lastName);	
											
					if (userService.saveUser(user))	// Save a new User
					{
						AuthenticatedWebSession.get().signIn(username, password);
						setResponsePage(HomePage.class); // go to HomePage after successful registration
					}
					else
						setResponsePage(LoginPage.class);	
				}										 
			}						
		};
		
		form.setDefaultModel(new CompoundPropertyModel<RegisterPage>(this));
		add(form);
		
		FormComponent<String> userNameTF = new TextField<String>("username").setRequired(true);
		form.add(userNameTF);
		
		// Signed-in user is to be updated - username is excluded from update
		if (loggedInUser != null)
    	{
			userNameTF.setEnabled(false);
    	} else // new user is to be registered, username must be validated if it is not already used
    		userNameTF.add(new UserNameValidator());
		
		form.add(new TextField<String>("firstName").setRequired(false));
		form.add(new TextField<String>("lastName").setRequired(false));
		
		TextField<String> emailTF = new TextField<String>("email");
		emailTF.add(EmailAddressValidator.getInstance());
		emailTF.setRequired(true); // Email required
		form.add(emailTF);
				
		// Passwords Text fields
		PasswordTextField pswdField = new PasswordTextField("password");
		PasswordTextField pswdCheckField = new PasswordTextField("passwordCheck");
		
		// In case logged-in user is to be updated, passwords are not required
		// If passwords are not entered, it is avaluated as "keep current password"
		if (loggedInUser != null)
    	{
			pswdField.setRequired(false);
			pswdCheckField.setRequired(false);
    	}
		
		form.add(pswdField);
		form.add(pswdCheckField);
		
		form.add(new EqualPasswordInputValidator(pswdField, pswdCheckField));
			
		submit = new AjaxSubmitLink("submit", form)
		{
			/*
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form)
			{																							
				super.onSubmit(target, form);				
			}
			*/
						
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
