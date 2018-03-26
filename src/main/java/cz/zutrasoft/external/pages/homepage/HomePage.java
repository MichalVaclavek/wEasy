package cz.zutrasoft.external.pages.homepage;

import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;

import cz.zutrasoft.base.services.IArticleService;
import cz.zutrasoft.base.services.ICategoryService;
import cz.zutrasoft.base.servicesimpl.ArticleService;
import cz.zutrasoft.base.servicesimpl.CategoryService;
import cz.zutrasoft.database.model.Article;
import cz.zutrasoft.database.model.CategoryWithArticles;
import cz.zutrasoft.external.pages.ExternalBasePage;
import cz.zutrasoft.external.panels.BasePanel;
import cz.zutrasoft.external.panels.BasePanelModel;



public class HomePage extends ExternalBasePage
{	
	private static final long serialVersionUID = 511896147294955276L;

	private static ICategoryService categoryService = CategoryService.getInstance();	
	private static IArticleService articleService = ArticleService.getInstance();
		
	@SuppressWarnings("serial")
	public HomePage(final PageParameters pp)
	{						
		super();
				
		// Creates repeating view
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

			// For each category there will be one panel, each panel has its own model that contains list of articles
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
		else if (pp.get("categoryId") != null) // Show only Articles of selected Category
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
