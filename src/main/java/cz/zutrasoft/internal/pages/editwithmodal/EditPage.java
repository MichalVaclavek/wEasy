package cz.zutrasoft.internal.pages.editwithmodal;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;

import cz.zutrasoft.base.services.ArticleService;
import cz.zutrasoft.base.servicesimpl.ArticleServiceImpl;
import cz.zutrasoft.database.daoimpl.ArticleDaoImpl;
//import cz.zutrasoft.database.daoimpl.DaoImpl;
import cz.zutrasoft.database.model.Article;
import cz.zutrasoft.internal.pages.InternalBasePage;

public class EditPage extends InternalBasePage
{
	
	private static final long serialVersionUID = -5368136852515244170L;

	/**
	 * Creates {@link EditPage} with no values.
	 * It is used to create new {@link Article}.
	 */
	public EditPage()
	{
		add(new EditPanel("editPanel"));
	}
	
	/**
	 * Tries to get and parse page parameter and create {@link EditPanel} with article default values.
	 * It is used to edit already created {@link Article}.
	 * 
	 * @param params page parameter with {@link Article} id.
	 */
	public EditPage(PageParameters params)
	{
		StringValue id= params.get(0);
		try 
		{
			//ArticleService articleService = new ArticleServiceImpl();
			ArticleService articleService = ArticleServiceImpl.getInstance();
			Article article = articleService.getArticleById(Integer.parseInt(id.toString()));
			add(new EditPanel("editPanel", article));
		}
		catch (Exception e) {
			add(new EditPanel("editPanel"));
		}
	}

	@Override
	protected IModel<String> getSubTitle()
	{
		return new ResourceModel("submenu.editPage");
	}
	
}

