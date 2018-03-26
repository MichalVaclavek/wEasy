package cz.zutrasoft.external.panels;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import cz.zutrasoft.database.model.Category;
import cz.zutrasoft.external.pages.homepage.HomePage;

/**
 * Panel that contains link to CategoryPanel
 * 
 * @author Michal Václavek
 */
public abstract class CategoryLinkPanel extends Panel
{
	private static final long serialVersionUID = -4055894462340298680L;

	@SuppressWarnings({ "serial" })
	public CategoryLinkPanel(String id, final IModel<Category> model)
	{
		super(id, model);
		
		Link<Object> link = new Link<Object>("link")
		{
			@Override
			public void onClick()
			{
				// Create page parameter so the correct Category id will be sent to HomePage to load and display respective Articles when clicked on link
				PageParameters pp = new PageParameters();
				pp.set("categoryId", model.getObject().getId());				
				this.setResponsePage(HomePage.class, pp);
			}
		};
		
		add(link.add(new Label("linkLabel", new Model<String>(model.getObject().getName()))));
	}

}
