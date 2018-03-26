package cz.zutrasoft.database.model;

import java.io.Serializable;
import java.util.List;

/**
 * Helping class to hold relation between {@link Category} and it's {@link Article}s.
 * 
 * @author Michal Václavek
 * @author vitfo - original atributes
 *
 */
public class CategoryWithArticles implements Serializable
{
	private static final long serialVersionUID = 3060015771689540302L;
	
	private Category category;
	
	private List<Article> articles;
	
	
	public CategoryWithArticles() { }
	
	public Category getCategory()
	{
		return category;
	}
	public void setCategory(Category category)
	{
		this.category = category;
	}
	
	public List<Article> getArticles()
	{
		return articles;
	}
	public void setArticles(List<Article> articles)
	{
		this.articles = articles;
	}
	
	
}
