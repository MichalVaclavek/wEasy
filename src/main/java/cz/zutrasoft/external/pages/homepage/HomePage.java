package cz.zutrasoft.external.pages.homepage;

import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;

import cz.zutrasoft.base.services.ArticleService;
import cz.zutrasoft.base.services.CategoryService;
import cz.zutrasoft.base.servicesimpl.ArticleServiceImpl;
import cz.zutrasoft.base.servicesimpl.CategoryServiceImpl;
import cz.zutrasoft.database.daoimpl.CategoryDaoImpl;
import cz.zutrasoft.database.model.Article;
import cz.zutrasoft.database.model.Category;
//import cz.zutrasoft.database.daoimpl.DaoImpl;
import cz.zutrasoft.database.model.CategoryWithArticles;
import cz.zutrasoft.external.pages.ExternalBasePage;
import cz.zutrasoft.external.pages.articlepage.ArticleModel;
import cz.zutrasoft.external.panels.BasePanel;
import cz.zutrasoft.external.panels.BasePanelModel;
import cz.zutrasoft.external.panels.CategoryPanelModel;
import cz.zutrasoft.internal.pages.editwithmodal.EditPage;
import cz.zutrasoft.external.panels.CategoryPanel;

public class HomePage extends ExternalBasePage
{	
	private static CategoryService categoryService = new CategoryServiceImpl();
	
	private static ArticleService articleService = new ArticleServiceImpl();
		
	/*
	public HomePage()
	{
		this(new PageParameters());
	}
	*/
	public HomePage(final PageParameters pp)
	{						
		super();
				
		// creates repeating view
		RepeatingView rv = new RepeatingView("basePanelRepeater");
		
		boolean logout  = true;
		
		if (!pp.isEmpty())
		{
			StringValue sv = pp.get("logout");
			if (sv != null)
				logout = sv.toBoolean();
		}
		
		if (pp.isEmpty() || logout)
		{
			List<CategoryWithArticles> categoriesWithArticles = categoryService.getAllCategoriesWithArticles();

			// for each category there will be one panel, each panel has its own model that contains list of articles
			for (final CategoryWithArticles cwa : categoriesWithArticles)
			{
				rv.add(new BasePanel(rv.newChildId(), new BasePanelModel(cwa.getArticles()))
				{
					@Override
					protected String getPanelTitle()
					{
						return cwa.getCategory().getName();
					}
				});
			}
		}
		else if (pp.get("categoryId") != null) // Show only articles of selected category
		{
			StringValue sv = pp.get("categoryId");
			final int categoryId = Integer.parseInt(sv.toString());			
			List<Article> articlesFromCategory = articleService.getAllArticlesInCategory(categoryId);
			
			Panel basePanelSingle = new BasePanel(rv.newChildId(), new BasePanelModel(articlesFromCategory))
			{
				@Override
				protected String getPanelTitle()
				{
					return categoryService.getCategoryById(categoryId).getName();
				}
			};
			
			basePanelSingle.add(new AttributeModifier("class", "basePanelSingle"));
			
			rv.add(basePanelSingle);		
			
		}		
		
		add(rv);		

	}
	
	@Override
	protected String getTitle()
	{
		return getString("homepage.title");
	}


}
