package cz.zutrasoft.external.pages.articlepage;


import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;

import cz.zutrasoft.base.BasicAutorizationAndAuthenticationSession;
import cz.zutrasoft.base.services.ICommentService;
import cz.zutrasoft.base.servicesimpl.CommentService;
import cz.zutrasoft.database.model.Comment;
import cz.zutrasoft.external.pages.ExternalBasePage;

/**
 * Page in which appropriate article is shown including comments to this article.
 * The article showed depends on page parameters.
 * 
 * @author vitfo
 * @author Michal Václavek
 */
public class ArticlePage extends ExternalBasePage
{
	private static final long serialVersionUID = -1729677244556988098L;
	
	private WebMarkupContainer commentsContainer;
	private WebMarkupContainer formContainer;
	
	private String commentText;
	
	@SuppressWarnings({ "serial"})
	public ArticlePage(PageParameters pp)
	{		
		// Get parameter and parse it to the int (article id).
		StringValue sv = pp.get(0);
		final int articleId = Integer.parseInt(sv.toString());
		
		// Create label with article text based on article id.
		Label articleText = new Label("articleText", new ArticleModel(articleId));
		// Label text contains html tags -> do not escape them.
		articleText.setEscapeModelStrings(false);
		add(articleText);
		
		// Container that contains form for adding comments.
		formContainer = new WebMarkupContainer("formContainer")
		{
			@Override
			protected void onConfigure()
			{
				// The container (form) should be visible just for loggeg users (roles user and admin).
				setEnabled(AuthenticatedWebSession.get().getRoles().hasRole(Roles.USER));
			}
		};
		// The container will be updated by AJAX so it needs output markup id.
		formContainer.setOutputMarkupId(true);
		add(formContainer);
		
		// Form for adding comments.
		Form<Object> form = new Form<Object>("form")
		{
			@Override
			protected void onSubmit()
			{
				super.onSubmit();
				
				// Getting our implementation of a AuthenticatedWebSession that contains some usefull properties (user id).
				BasicAutorizationAndAuthenticationSession session = (BasicAutorizationAndAuthenticationSession)Session.get();
											
				ICommentService commentService = CommentService.getInstance();
				commentService.saveTextAsComment(commentText, session.getUserId(), articleId);
								
				commentText = "";
			}
			
		};
		form.setOutputMarkupId(true);
		formContainer.add(form);
		
		form.add(new TextArea<String>("comment", new PropertyModel<String>(this, "commentText")).setRequired(true));
		form.add(new AjaxSubmitLink("submit", form)
		{
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				target.add(formContainer);
				target.add(commentsContainer);
			}
		});
		
		commentsContainer = new WebMarkupContainer("commentsContainer");
		commentsContainer.setOutputMarkupId(true);
		add(commentsContainer);
		
		// Repeater that gets all comments for this article and renders them.
		ListView<Comment> repeater = new ListView<Comment>("repeater", new CommentsModel(articleId))
		{
			@Override
			protected void populateItem(ListItem<Comment> item)
			{
				Comment c = item.getModelObject();
				item.add(new Label("text", c.getText()));
				item.add(new Label("created", c.getCreated()));
			}
		};
		commentsContainer.add(repeater);
	}

	@Override
	protected String getTitle()
	{
		return getString("articlepage.title");
	}
	
}
