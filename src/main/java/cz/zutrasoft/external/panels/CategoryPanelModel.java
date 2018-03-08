package cz.zutrasoft.external.panels;

import java.util.List;

import org.apache.wicket.model.IModel;

import cz.zutrasoft.database.model.Article;
import cz.zutrasoft.database.model.Category;

public class CategoryPanelModel implements IModel<List<Category>>
{
	
	private static final long serialVersionUID = 7667463351386476200L;
	
	private List<Category> categories;
	
	public CategoryPanelModel(List<Category> categories)
	{
		this.categories = categories;
	}

	@Override
	public void detach() {}

	@Override
	public List<Category> getObject()
	{
		return categories;
	}

	@Override
	public void setObject(List<Category> object) {}
	
}
