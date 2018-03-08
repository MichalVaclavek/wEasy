package cz.zutrasoft.database.model;

import java.io.Serializable;
import java.util.List;

/**
 * Pomocná třída k uchování vazby mezi {@link Category} a články {@link Article} v této Category
 * 
 * @author Michal Václavek - přidání Hibernate, JPA anotací a rozchození příslušných DAO a Service tříd
 * @author vitfo - původní návrh atributů
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
