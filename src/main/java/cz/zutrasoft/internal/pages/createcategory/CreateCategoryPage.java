package cz.zutrasoft.internal.pages.createcategory;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;

import cz.zutrasoft.base.services.CategoryService;
import cz.zutrasoft.base.servicesimpl.CategoryServiceImpl;
import cz.zutrasoft.database.daoimpl.CategoryDaoImpl;
import cz.zutrasoft.internal.pages.InternalBasePage;
import cz.zutrasoft.internal.pages.editwithmodal.EditPage;

public class CreateCategoryPage extends InternalBasePage
{
	private String category;
	
	public CreateCategoryPage()
	{
		FeedbackPanel feedback = new FeedbackPanel("feedback");
		add(feedback);
		
		Form form = new Form("form")
		{
			@Override
			protected void onSubmit()
			{
				//CategoryDaoImpl dao = new CategoryDaoImpl();
				//CategoryService categorService = new CategoryServiceImpl();
				CategoryService categoryService = CategoryServiceImpl.getInstance();
				categoryService.saveCategory(category);
				setResponsePage(EditPage.class);
			}
		};
		add(form);
		
		TextField categoryTF = new TextField("category", new PropertyModel(this, "category"));
		categoryTF.setRequired(true);
		form.add(categoryTF);
		
		Button submitBT = new Button("submit");
		form.add(submitBT);
	}
	
	@Override
	protected IModel<String> getSubTitle()
	{
		return new ResourceModel("submenu.createCategoryPage");
	}
	
}
