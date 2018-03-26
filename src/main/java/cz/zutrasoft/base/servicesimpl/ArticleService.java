/**
 * 
 */
package cz.zutrasoft.base.servicesimpl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import cz.zutrasoft.base.services.IArticleService;
import cz.zutrasoft.database.dao.IArticleDao;
import cz.zutrasoft.database.daoimpl.ArticleDaoImpl;
import cz.zutrasoft.database.model.Article;
import cz.zutrasoft.database.model.Category;

/**
 * Singleton implementation of the I_ArticleService interface.
 * 
 * @author Michal Václavek
 */
public class ArticleService implements IArticleService
{
	private IArticleDao articleDao = new ArticleDaoImpl();
	
	private static class SingletonHolder
	{
        private static final ArticleService SINGLE_INSTANCE = new ArticleService();
    }
	
	/**
	 * @return singleton instance of the ArticleService
	 */
	public static ArticleService getInstance()
	{				
		return SingletonHolder.SINGLE_INSTANCE;			
	}
	
	private ArticleService()
	{}
	
	/* (non-Javadoc)
	 * @see cz.zutrasoft.base.services.ArticleService#saveTextAsArticle(java.lang.String, cz.zutrasoft.database.model.Category)
	 */
	@Override
	public Article saveTextAsArticle(String text, Category category)
	{
		if (text == null)
    	{
    		throw new IllegalArgumentException("Argument [text] cannot be null.");
		}
		if (category == null)
		{
			throw new IllegalArgumentException("Argument [category] cannot be null.");
		}
				
		String header = getHeadeFromSource(text);
		Article newArticle = new Article(new Timestamp(new Date().getTime()), category, header, text);
		
		articleDao.saveArticle(newArticle); 
		
		return newArticle;
	}
	
	/**
	 * Gets header from the Article's text.
	 * 
	 * @param text of the Article conteining header
	 * @return header of the {@link Article} derived from it's text
	 */
	private String getHeadeFromSource(String text)
	{
		if (text == null)
    	{
    		throw new IllegalArgumentException("Argument [text] cannot be null.");
		}		
				 
    	// Header must not be empty
		String header = "Header?";
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
