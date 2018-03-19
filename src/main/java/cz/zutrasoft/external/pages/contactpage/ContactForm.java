package cz.zutrasoft.external.pages.contactpage;

import java.sql.Timestamp;
import java.util.Date;

import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.EmailAddressValidator;

import cz.zutrasoft.base.CustomMessageFP;
import cz.zutrasoft.base.ExactErrorLevelFilter;
import cz.zutrasoft.base.services.IContactMessageService;
import cz.zutrasoft.base.servicesimpl.CommentServiceImpl;
import cz.zutrasoft.base.servicesimpl.ContactMessageServiceImpl;
import cz.zutrasoft.external.pages.homepage.HomePage;

/**
 * Basic contact message form.
 * 
 * @author Michal Václavek
 *
 */
public class ContactForm extends Form
{
	private String authorName;
	private String emailAddr;
    private String contactMessage;
    
    private final CustomMessageFP feedbackSuccess;
    private final CustomMessageFP feedbackErr;
    private final TextArea<String> messageTextArea;
    
    private Model<String> userNameMdl = new Model<>();
    private Model<String> emailMdl = new Model<>();
    
    //private static IContactMessageService contactMessageService = new ContactMessageServiceImpl();
    private static IContactMessageService contactMessageService = ContactMessageServiceImpl.getInstance();

    public ContactForm(String id, String name, String email)
    {
        super(id);
        
        setDefaultModel(new CompoundPropertyModel(this));
           
        // Predvyplnit userName a email, pokud jsou neprazdne   
        if (!name.isEmpty())
        	authorName = name;
        
        if (!email.isEmpty())
        	emailAddr = email;
        
        feedbackSuccess = new CustomMessageFP("successFeedback", new ExactErrorLevelFilter(FeedbackMessage.INFO));

		feedbackSuccess.setOutputMarkupId(true);
		feedbackSuccess.setOutputMarkupPlaceholderTag(true);		
		add(feedbackSuccess);
		
        feedbackErr = new CustomMessageFP("errorFeedback", new ExactErrorLevelFilter(FeedbackMessage.ERROR));

		feedbackErr.setOutputMarkupId(true);
		feedbackErr.setOutputMarkupPlaceholderTag(true);
		add(feedbackErr);
        				
        userNameMdl.setObject(authorName);
        add(new TextField<String>("name", userNameMdl).setRequired(true));
        
        emailMdl.setObject(emailAddr);
            
        // Email address text field
        TextField<String> emailTF = new TextField<String>("email", emailMdl);
		emailTF.add(EmailAddressValidator.getInstance());
		emailTF.setRequired(true);

		add(emailTF);
		
		// Message text field		 
		messageTextArea = (TextArea<String>) new TextArea<>("message", new PropertyModel<String>(this, "contactMessage")).setRequired(true);
		messageTextArea.setOutputMarkupPlaceholderTag(true);
		
		add(messageTextArea);
		
 		add(new AjaxSubmitLink("submit", this)
		{
			@Override
			protected void onSubmit(final AjaxRequestTarget target, Form<?> form)
			{								
				super.onSubmit(target, form);	
				feedbackSuccess.setVisible(true);
				this.info(getString("form.messageSentSuccess")); 
				target.add(feedbackSuccess);
				messageTextArea.clearInput();
				target.add(messageTextArea);
				
				Session.get().getFeedbackMessages().clear();
				//target.add(feedbackSuccess);
				target.add(feedbackErr);													
			}
						
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form)
			{
				super.onError(target, form);
				target.add(feedbackErr);
				
				Session.get().getFeedbackMessages().clear();
				target.add(feedbackSuccess);
				//target.add(feedbackErr);
			}						
		});
 		
    }

    @Override
    protected void onSubmit()
    {    	
		super.onSubmit();									
		
		contactMessageService.saveContactMessage(userNameMdl.getObject(), emailMdl.getObject(), contactMessage);
		contactMessage = "";					
    }
    
    
}
