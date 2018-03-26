package cz.zutrasoft.internal.pages.createcategory;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;

import cz.zutrasoft.base.services.ICategoryService;
import cz.zutrasoft.base.servicesimpl.CategoryService;
import cz.zutrasoft.database.model.Category;
import cz.zutrasoft.internal.pages.InternalBasePage;
import cz.zutrasoft.internal.pages.editwithmodal.EditPage;


public class CreateCategoryPage extends InternalBasePage
{
	private static final long serialVersionUID = 2423421019647782123L;
	
	private String category;
	
	@SuppressWarnings({"serial" })
	public CreateCategoryPage()
	{
		FeedbackPanel feedback = new FeedbackPanel("feedback");
		add(feedback);
		
		Form<Object> form = new Form<Object>("form")
		{
			@Override
			protected void onSubmit()
			{
				ICategoryService categoryService = CategoryService.getInstance();
				categoryService.saveCategory(category);
				setResponsePage(EditPage.class);
			}
		};
		add(form);
		
		TextField<String> categoryTF = new TextField<String>("category", new PropertyModel<String>(this, "category"));
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
