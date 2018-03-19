package cz.zutrasoft.database.dao;

import java.util.List;

import cz.zutrasoft.database.model.Article;
import cz.zutrasoft.database.model.Category;
import cz.zutrasoft.database.model.Comment;

public interface IArticleDao
{
	public void updateArticle(Article article);
	public List<Article> getAllArticles();
	public List<Article> getAllArticlesInCategory(Category category);
	public List<Article> getAllArticlesInCategory(long categoryId);
	public Article getArticleById(int id);
	//public void saveArticle(Article newArticle);
	public Article saveArticle(Article newArticle);
	
	public void deleteArticle(Article article);	
}
