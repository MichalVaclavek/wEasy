package cz.zutrasoft.external.panels;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import cz.zutrasoft.database.model.Article;
import cz.zutrasoft.database.model.Category;
import cz.zutrasoft.external.pages.articlepage.ArticlePage;
import cz.zutrasoft.external.pages.homepage.HomePage;

/**
 * Panel that contains link to CategoryPanel
 * 
 * @author User
 *
 */
public abstract class CategoryLinkPanel extends Panel
{
	
	public CategoryLinkPanel(String id, final IModel<Category> model)
	{
		super(id, model);
		
		Link link = new Link("link")
		{
			@Override
			public void onClick()
			{
				// create page parameter so the correct Category name will be sent to HomePage and will be loaded when clicked on link
				PageParameters pp = new PageParameters();
				pp.set("categoryId", model.getObject().getId());
				//pp.set(0, model.getObject().getId());
				//pp.set(0, model.getObject().getName());
				//TODO zde se musi zobrazit HomePage, ale se seznamem Articles v Category resp.
				// mela by stacit BasePage, ktera ale ma nactenu jen jednu Category a jeji clanky
				//setResponsePage(HomePage.class, pp);
				//setResponsePage(HomePage.class);
				this.setResponsePage(HomePage.class, pp);
			}
		};
		
		add(link.add(new Label("linkLabel", new Model(model.getObject().getName()))));
	}

}
