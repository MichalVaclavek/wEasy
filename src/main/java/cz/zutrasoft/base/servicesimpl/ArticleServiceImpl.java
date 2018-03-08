/**
 * 
 */
package cz.zutrasoft.base.servicesimpl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import cz.zutrasoft.base.services.ArticleService;
import cz.zutrasoft.database.dao.IArticleDao;
import cz.zutrasoft.database.dao.IUserDao;
import cz.zutrasoft.database.daoimpl.ArticleDaoImpl;
import cz.zutrasoft.database.daoimpl.HibernateUtils;
import cz.zutrasoft.database.daoimpl.UserDaoImpl;
import cz.zutrasoft.database.model.Article;
import cz.zutrasoft.database.model.Category;
import cz.zutrasoft.database.model.Comment;

/**
 * @author Michal
 *
 */
public class ArticleServiceImpl implements ArticleService
{

	private IArticleDao articleDao = new ArticleDaoImpl();
	
	/* (non-Javadoc)
	 * @see cz.zutrasoft.base.services.ArticleService#saveTextAsArticle(java.lang.String, cz.zutrasoft.database.model.Category)
	 */
	@Override
	public void saveTextAsArticle(String text, Category category)
	{
		if (text == null)
    	{
    		throw new IllegalArgumentException("Argument [text] cannot be null.");
		}
		if (category == null)
		{
			throw new IllegalArgumentException("Argument [category] cannot be null.");
		}
			/*	 
    	// Upravy pred ulozenim. Header nesmi byt prazdny
		String header = "Header?";
		// Hledani html tagu <h1> </h1> apod.
		if (text.contains("<"))
		{
			header = text.substring(0, text.indexOf("</"));
			header = header.substring(header.indexOf(">") + 1);
		}
		else 
			if (text.contains("&lt;"))
			{
				header = text.substring(0, text.indexOf("&lt;/"));
				header = header.substring(header.indexOf("&gt;") + 1);
			}
		
		if (header.length() > 100)
		{
			header = header.substring(0, 100);
		}
		*/
		
		String header = getHeadeFromSource(text);
		Article newArticle = new Article(new Timestamp(new Date().getTime()), category, header, text);
		
		articleDao.saveArticle(newArticle); 
	}
	
	/**
	 * Pomocná metoda ...
	 * Gets text of Article from the text edited by user within client form
	 * @return
	 */
	private String getHeadeFromSource(String text)
	{
		if (text == null)
    	{
    		throw new IllegalArgumentException("Argument [text] cannot be null.");
		}		
				 
    	// Upravy pred ulozenim. Header nesmi byt prazdny
		String header = "Header?";
		// Hledani html tagu <h1> </h1> apod.
		if (text.contains("<"))
		{
			header = text.substring(0, text.indexOf("</"));
			header = header.substring(header.indexOf(">") + 1);
		}
		else 
			if (text.contains("&lt;"))
			{
				header = text.substring(0, text.indexOf("&lt;/"));
				header = header.substring(header.indexOf("&gt;") + 1);
			}
		
		if (header.length() > 100)
		{
			header = header.substring(0, 100);
		}
		
		return header;
	}

	/* (non-Javadoc)
	 * @see cz.zutrasoft.base.services.ArticleService#updateArticle(cz.zutrasoft.database.model.Article)
	 */
	@Override
	public void updateArticle(Article article)
	{
		article.setHeader(getHeadeFromSource(article.getText()));
		articleDao.updateArticle(article);
	}

	/* (non-Javadoc)
	 * @see cz.zutrasoft.base.services.ArticleService#getAllArticles()
	 */
	@Override
	public List<Article> getAllArticles()
	{
		return articleDao.getAllArticles();
	}

	/* (non-Javadoc)
	 * @see cz.zutrasoft.base.services.ArticleService#getArticle(int)
	 */
	@Override
	public Article getArticleById(int id)
	{
		return articleDao.getArticleById(id);
	}

	@Override
	public List<Article> getAllArticlesInCategory(long categoryId)
	{
		return articleDao.getAllArticlesInCategory(categoryId);
	}

	@Override
	public void deleteArticle(Article article)
	{
		articleDao.deleteArticle(article);		
	}
	

}
