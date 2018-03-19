/**
 * 
 */
package cz.zutrasoft.base.services;

import java.util.List;

import cz.zutrasoft.database.model.Article;
import cz.zutrasoft.database.model.Category;
import cz.zutrasoft.database.model.Comment;

/**
 * @author Michal
 *
 */
public interface ArticleService
{
	public Article saveTextAsArticle(String text, Category category);
	public void updateArticle(Article article);
	
	public List<Article> getAllArticles();
	public List<Article> getAllArticlesInCategory(long categoryId);
	public Article getArticleById(int id);
	
	public void deleteArticle(Article article);

}
