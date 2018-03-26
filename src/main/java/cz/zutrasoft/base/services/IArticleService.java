/**
 * 
 */
package cz.zutrasoft.base.services;

import java.util.List;

import cz.zutrasoft.database.model.Article;
import cz.zutrasoft.database.model.Category;

/**
 * Basic service methods to work with Articles.
 * 
 * @author Michal Václavek
 */
public interface IArticleService
{
	/**
	 * Save text of Article entered by user.
	 * 
	 * @param text of the Article
	 * @param category {@link Category} of the article
	 * @return saved {@link Article} instance object
	 */
	public Article saveTextAsArticle(String text, Category category);
	
	/**
	 * Updates {@link Article} instance in the repository, usualy in DB.
	 * 
	 * @param article - {@link Article} to be updated
	 */
	public void updateArticle(Article article);
	
	/**
	 * Gets all saved Articles
	 * @return all all saved Articles
	 */
	public List<Article> getAllArticles();
	
	/**
	 * Gets all articles belonging to specified {@link Category}
	 * 
	 * @param categoryId id of the {@link Category}
	 * @return all articles belonging to specified {@link Category}
	 */
	public List<Article> getAllArticlesInCategory(long categoryId);
	
	/**
	 * Gets one Article according it's id
	 * 
	 * @param id of the {@link Article}
	 * @return {@link Article} of the given {@code id} instance 
	 */
	public Article getArticleById(int id);
	
	/**
	 * Deletes one {@link Article} from repository, usualy from DB.
	 * 
	 * @param article instance to be deleted
	 */
	public void deleteArticle(Article article);

}
