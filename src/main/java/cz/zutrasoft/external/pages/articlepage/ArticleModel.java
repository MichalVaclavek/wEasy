package cz.zutrasoft.external.pages.articlepage;

import org.apache.wicket.model.IModel;

import cz.zutrasoft.base.services.ArticleService;
import cz.zutrasoft.base.servicesimpl.ArticleServiceImpl;
import cz.zutrasoft.database.daoimpl.ArticleDaoImpl;

/**
 * Model containing article.
 * The model needs id of the article to fetch it from datasource.
 * @author User
 *
 */
public class ArticleModel implements IModel<String>
{

	private static final long serialVersionUID = -1514779344427109259L;
	//private static ArticleDaoImpl dao = new ArticleDaoImpl();
	private static ArticleService articleService = new ArticleServiceImpl();
	private int articleId;
	
	public ArticleModel(int articleId)
	{
		this.articleId = articleId;
	}

	@Override
	public void detach() {}

	@Override
	public String getObject()
	{
		return articleService.getArticleById(articleId).getText();
	}

	@Override
	public void setObject(String object) {}

}
