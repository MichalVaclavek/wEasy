/**
 * 
 */
package cz.zutrasoft.base.servicesimpl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import cz.zutrasoft.base.services.CommentService;
import cz.zutrasoft.database.dao.IArticleDao;
import cz.zutrasoft.database.dao.ICommentDao;
import cz.zutrasoft.database.daoimpl.ArticleDaoImpl;
import cz.zutrasoft.database.daoimpl.CommentDaoImpl;
import cz.zutrasoft.database.model.Article;
import cz.zutrasoft.database.model.Comment;
import cz.zutrasoft.database.model.User;

/**
 * @author Michal
 *
 */
public class CommentServiceImpl implements CommentService
{
	private static ICommentDao comentsDao = new CommentDaoImpl();
	
	
	private static class SingletonHolder
	{
        private static final CommentServiceImpl SINGLE_INSTANCE = new CommentServiceImpl();
    }
	
	/**
	 * @return singleton instance of the CommentServiceImpl
	 */
	public static CommentServiceImpl getInstance()
	{				
		return SingletonHolder.SINGLE_INSTANCE;			
	}
	
	private CommentServiceImpl()
	{}
	
	/* (non-Javadoc)
	 * @see cz.zutrasoft.base.services.CommentService#saveComment(cz.zutrasoft.database.model.Comment)
	 */
	@Override
	public Comment saveTextAsComment(String commentText, int userID, int articleID)
	{
		// Vytvoření nového komentáře k ulozeni
        Comment comment = new Comment();
        comment.setCreated(new Timestamp(new Date().getTime()));
        comment.setText(commentText);
        comment.setUserId(userID);
              
        IArticleDao articleDao = new ArticleDaoImpl();		
		// Ziskani clanku k ulozeni komentare
        Article article = articleDao.getArticleById(articleID);                
        comment.setArticle(article);
       
        comentsDao.saveComment(comment);
        
        return comment;
	}


	/* (non-Javadoc)
	 * @see cz.zutrasoft.base.services.CommentService#getById(int)
	 */
	@Override
	public Comment getById(int id)
	{
		return comentsDao.getById(id);
	}
	

	@Override
	public List<Comment> getAllCommentsForArticleId(int artId)
	{
		return comentsDao.getAllCommentsForArticleId(artId);
	}


	/* (non-Javadoc)
	 * @see cz.zutrasoft.base.services.CommentService#getAllCommentsFromUser(int)
	 */
	@Override
	public List<Comment> getAllCommentsFromUser(int userID)
	{
		return comentsDao.getAllCommentsFromUser(userID);
	}


	/* (non-Javadoc)
	 * @see cz.zutrasoft.base.services.CommentService#getAllCommentsFromUser(cz.zutrasoft.database.model.User)
	 */
	@Override
	public List<Comment> getAllCommentsFromUser(User user)
	{
		return comentsDao.getAllCommentsFromUser(user.getId());
	}


	/* (non-Javadoc)
	 * @see cz.zutrasoft.base.services.CommentService#getAllComments()
	 */
	@Override
	public List<Comment> getAllComments()
	{
		return comentsDao.getAllComments();
	}


	/* (non-Javadoc)
	 * @see cz.zutrasoft.base.services.CommentService#deleteComment(cz.zutrasoft.database.model.Comment)
	 */
	@Override
	public void deleteComment(Comment comment)
	{
		comentsDao.deleteComment(comment);
	}


}
