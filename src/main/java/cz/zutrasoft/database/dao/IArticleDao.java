package cz.zutrasoft.database.dao;

import java.util.List;

import cz.zutrasoft.database.model.Article;
import cz.zutrasoft.database.model.Category;

public interface IArticleDao
{
	/**
	 * Save Article into DB and return saved Article.
	 * 
	 * @param newArticle to save
	 * @return saved Article
	 */
	public Article saveArticle(Article newArticle);
	
	/**
	 * Updates Article in DB.
	 * 
	 * @param article to be updated in DB.
	 */
	public void updateArticle(Article article);
	
	public Article getArticleById(int id);
	
	public List<Article> getAllArticles();
	/**
	 * Gets all Articles in given {@link Category}.
	 * 
	 * @param category of the Articles to get
	 * @return all Articles in given Category
	 */
	public List<Article> getAllArticlesInCategory(Category category);
	public List<Article> getAllArticlesInCategory(long categoryId);
		
	public void deleteArticle(Article article);	
}
