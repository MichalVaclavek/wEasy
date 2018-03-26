package cz.zutrasoft.external.panels;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

import cz.zutrasoft.database.model.Category;

/**
 * Panel that contains links to all Categories.
 * 
 * @author Michal Václavek
 */
public abstract class CategoryPanel extends Panel
{
	private static final long serialVersionUID = 3513797842860694427L;

	@SuppressWarnings("serial")
	public CategoryPanel(String id, IModel<List<Category>> model)
	{
		super(id, model);

		add(new Label("panelTitle", new ResourceModel(getPanelTitleKey())));
		
		// create panel for each link to Category
		RepeatingView rv = new RepeatingView("categoryLinkPanelRepeater");
		for (final Category ar : model.getObject())
		{
			rv.add(new CategoryLinkPanel(rv.newChildId(), new CategoryLinkPanelModel(ar)) {});
		};
		
		add(rv);
	}
	
	/**
	 * Gets resource key of the title of the panel.
	 * @return title of the panel
	 */
	protected abstract String getPanelTitleKey();
	
}
