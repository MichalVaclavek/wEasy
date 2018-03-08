package cz.zutrasoft.database.dao;

import java.util.List;

import cz.zutrasoft.database.model.Article;
import cz.zutrasoft.database.model.Comment;

public interface ICommentDao
{
	public void saveComment(Comment comment);
	public List<Comment> getAllCommentsForArticle(Article a);
	public List<Comment> getAllCommentsFromUser(int userID);
	public List<Comment> getAllComments();
	public Comment getById(int id);
	
	public void deleteComment(Comment comment);
	public List<Comment> getAllCommentsForArticleId(int artId);
}
