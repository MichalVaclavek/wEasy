package cz.zutrasoft.external.pages.articlepage;

import java.util.List;

import org.apache.wicket.model.IModel;

import cz.zutrasoft.base.services.ICommentService;
import cz.zutrasoft.base.servicesimpl.CommentService;
import cz.zutrasoft.database.model.Comment;

/**
 * Model containing comments for the article.
 * The model needs id of the article.
 * 
 * @author vitfo
 * @author Michal Václavek
 */
public class CommentsModel implements IModel<List<Comment>>
{
	private static final long serialVersionUID = -1514779344427172259L;

	private static ICommentService commentService = CommentService.getInstance();
	
	private int articleId;
	
	public CommentsModel(int articleId)
	{
		this.articleId = articleId;
	}

	@Override
	public void detach() {}

	@Override
	public List<Comment> getObject()
	{
		return commentService.getAllCommentsForArticleId(articleId);
	}

	@Override
	public void setObject(List<Comment> object) {}

}
