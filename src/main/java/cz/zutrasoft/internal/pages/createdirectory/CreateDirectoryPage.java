package cz.zutrasoft.internal.pages.createdirectory;

import java.util.List;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;

import cz.zutrasoft.base.services.CategoryService;
import cz.zutrasoft.base.services.DirectoryService;
import cz.zutrasoft.base.servicesimpl.CategoryServiceImpl;
import cz.zutrasoft.base.servicesimpl.DirectoryServiceImpl;
import cz.zutrasoft.database.daoimpl.CategoryDaoImpl;
import cz.zutrasoft.database.daoimpl.DirectoryDaoImpl;
import cz.zutrasoft.database.model.Category;
import cz.zutrasoft.internal.pages.InternalBasePage;
import cz.zutrasoft.internal.pages.editwithmodal.EditPage;

public class CreateDirectoryPage extends InternalBasePage
{	
	private static final long serialVersionUID = 1088502493140163118L;
	
	private String directoryName;
	private DropDownChoice<Category> categoryDDCH;
	private Category selectedCategory = null;
	
	public CreateDirectoryPage()
	{
		FeedbackPanel feedback = new FeedbackPanel("feedback");
		add(feedback);
		
		Form form = new Form("form")
		{
			@Override
			protected void onSubmit()
			{
				//DirectoryService  directoryService = new DirectoryServiceImpl();
				DirectoryService  directoryService = DirectoryServiceImpl.getInstance();
				directoryService.saveDirectory(directoryName, (selectedCategory != null) ? selectedCategory : null);

				setResponsePage(EditPage.class);
			}
		};
		add(form);
		
		IModel<List<Category>> categoryModel = new AbstractReadOnlyModel<List<Category>>()
		{
			@Override
			public List<Category> getObject()
			{
				//CategoryDaoImpl dao = new CategoryDaoImpl();
				//CategoryService categorService = new CategoryServiceImpl();
				CategoryService categoryService = CategoryServiceImpl.getInstance();
				return categoryService.getAllCategories();
			}
		};
		
		// shows Category "name" property
		ChoiceRenderer choiceRenderer = new ChoiceRenderer("name");
		categoryDDCH = new DropDownChoice<Category>("select", new PropertyModel(this, "selectedCategory"), categoryModel, choiceRenderer);
		form.add(categoryDDCH);
		
		//TextField<String> directoryTF = new TextField<String>("directory", new PropertyModel<String>(this, "directory"));
		TextField directoryTF = new TextField("directory", new PropertyModel(this, "directoryName"));
		directoryTF.setRequired(true);
		form.add(directoryTF);
		
		Button submitBT = new Button("submit");
		form.add(submitBT);
	}
	
	@Override
	protected IModel<String> getSubTitle()
	{
		return new ResourceModel("submenu.createDirectoryPage");
	}
	
}
