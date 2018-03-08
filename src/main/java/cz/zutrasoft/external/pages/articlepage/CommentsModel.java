package cz.zutrasoft.external.pages.articlepage;

import java.util.List;

import org.apache.wicket.model.IModel;

import cz.zutrasoft.base.services.CommentService;
import cz.zutrasoft.base.servicesimpl.CommentServiceImpl;
import cz.zutrasoft.database.daoimpl.CommentDaoImpl;
import cz.zutrasoft.database.model.Comment;

/**
 * Model containing comments for the article.
 * The model needs id of the article.
 * 
 * @author zutrasoft
 */
public class CommentsModel implements IModel<List<Comment>>
{

	private static final long serialVersionUID = -1514779344427172259L;
	//private static CommentDaoImpl dao = new CommentDaoImpl();
	
	private static CommentService commentService = new CommentServiceImpl();
	
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
		//return dao.getAllCommentsForArticle(articleId);
		return commentService.getAllCommentsForArticleId(articleId);
	}

	@Override
	public void setObject(List<Comment> object) {}

}
