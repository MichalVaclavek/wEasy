package cz.zutrasoft.external.pages.articlepage;

import org.apache.wicket.model.IModel;

import cz.zutrasoft.base.services.IArticleService;
import cz.zutrasoft.base.servicesimpl.ArticleService;

/**
 * Model containing article.
 * The model needs id of the article to fetch it from datasource.
 * @author vitfo
 * @author Michal Václavek
 *
 */
public class ArticleModel implements IModel<String>
{
	private static final long serialVersionUID = -1514779344427109259L;
	
	private static IArticleService articleService = ArticleService.getInstance();
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
