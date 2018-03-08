package cz.zutrasoft.base.services;

import java.util.List;

import cz.zutrasoft.database.model.Article;
import cz.zutrasoft.database.model.Comment;
import cz.zutrasoft.database.model.User;

public interface CommentService
{
	public void saveTextAsComment(String comment, int userID, int articleID);
	public Comment getById(int id);
	
	public List<Comment> getAllCommentsForArticleId(int artId);	
	public List<Comment> getAllCommentsFromUser(int userID);
	public List<Comment> getAllCommentsFromUser(User user);
	public List<Comment> getAllComments();
		
	public void deleteComment(Comment comment);
}
