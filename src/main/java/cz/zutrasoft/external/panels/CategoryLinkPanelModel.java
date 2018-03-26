package cz.zutrasoft.external.panels;

import org.apache.wicket.model.IModel;

import cz.zutrasoft.database.model.Category;

public class CategoryLinkPanelModel implements IModel<Category>
{
	private static final long serialVersionUID = 7667463351383476220L;

	private Category category;
	
	public CategoryLinkPanelModel(Category category) 
	{
		this.category = category;
	}

	@Override
	public void detach() {}

	@Override
	public Category getObject()
	{
		return category;
	}

	@Override
	public void setObject(Category object) {}

	
}
