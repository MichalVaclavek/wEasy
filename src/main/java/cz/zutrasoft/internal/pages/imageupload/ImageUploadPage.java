package cz.zutrasoft.internal.pages.imageupload;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.MultiFileUploadField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;

import cz.zutrasoft.base.services.CategoryService;
import cz.zutrasoft.base.services.DirectoryService;
import cz.zutrasoft.base.services.ImageService;
import cz.zutrasoft.base.servicesimpl.CategoryServiceImpl;
import cz.zutrasoft.base.servicesimpl.DirectoryServiceImpl;
import cz.zutrasoft.base.servicesimpl.ImageServiceImpl;
import cz.zutrasoft.database.daoimpl.CategoryDaoImpl;
import cz.zutrasoft.database.daoimpl.DirectoryDaoImpl;
import cz.zutrasoft.database.daoimpl.ImageDaoImpl;
import cz.zutrasoft.database.model.Category;
import cz.zutrasoft.database.model.Directory;
import cz.zutrasoft.database.model.Image;
import cz.zutrasoft.internal.pages.InternalBasePage;

public class ImageUploadPage extends InternalBasePage
{	
	private static final long serialVersionUID = -3590469525322776351L;
	
	private MultiFileUploadField multiFileUploadField;
	private DropDownChoice<Category> categoryDDCH;
	private DropDownChoice<Directory> directoryDDCH;
	private AjaxButton submitBT;
	private String[] fileExtensions = {"png", "jpg", "jpeg", "gif"};
	private Category selectedCategory = null;
	private Directory selectedDirectory = null;
	
	private List<FileUpload> uploads;
	
	public ImageUploadPage()
	{
		final FeedbackPanel feedback = new FeedbackPanel("feedback");
		feedback.setOutputMarkupId(true);
		add(feedback);
		
		Form form = new Form("form")
		{
			@Override
			protected void onSubmit()
			{
				if (uploads != null)
				{
					for (FileUpload fu : uploads)
					{
						String fileExtension = fu.getClientFileName().substring(fu.getClientFileName().lastIndexOf(".") + 1);
						if (!Arrays.asList(fileExtensions).contains(fileExtension.toLowerCase()))
						{
							error("Unrecognized file extension. Supported types: " + Arrays.toString(fileExtensions));
						} else 
						{
							Image uploadedImagefile = new Image(selectedDirectory, fu);
							//ImageDaoImpl dao = new ImageDaoImpl();
							ImageService imageService = new ImageServiceImpl();
							imageService.saveImageFile(uploadedImagefile);
						}
					}
				}
			}
		};
		form.setMultiPart(true);
		
		IModel<List<Category>> categoryModel = new AbstractReadOnlyModel<List<Category>>()
		{
			@Override
			public List<Category> getObject()
			{
				//CategoryDaoImpl dao = new CategoryDaoImpl();
				CategoryService caterService = new CategoryServiceImpl();
				return caterService.getAllCategories();
			}
		};
		// shows Category "name" property
		ChoiceRenderer choiceRenderer = new ChoiceRenderer("name");
		categoryDDCH = new DropDownChoice("categories", new PropertyModel(this, "selectedCategory"), categoryModel, choiceRenderer);
		categoryDDCH.add(new OnChangeAjaxBehavior()
		{
			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				target.add(directoryDDCH);
			}
		});
		form.add(categoryDDCH);
		
		IModel<List<Directory>> directoryModel = new AbstractReadOnlyModel<List<Directory>>()
		{
			@Override
			public List<Directory> getObject()
			{
				//DirectoryDaoImpl dao = new DirectoryDaoImpl();
				DirectoryService dirService = new DirectoryServiceImpl();
				if (selectedCategory == null)
				{
					return dirService.getAllDirectories();
				} else
				{
					return dirService.getAllDirectoriesForCategory(selectedCategory);
				}
			}
		};
		// shows Category "name" property
		ChoiceRenderer directoryChoiceRenderer = new ChoiceRenderer("name");
		directoryDDCH = new DropDownChoice("directories", new PropertyModel(this, "selectedDirectory"), directoryModel, directoryChoiceRenderer);
		directoryDDCH.setRequired(true);
		directoryDDCH.setOutputMarkupId(true);
		form.add(directoryDDCH);
		
		multiFileUploadField = new MultiFileUploadField("multiUpload", new PropertyModel<List<FileUpload>>(this, "uploads"), 10);
		form.add(multiFileUploadField);
		
		submitBT = new AjaxButton("submit", form)
		{
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				target.add(form);
				target.add(feedback);
			}
			
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form)
			{
				target.add(form);
				target.add(feedback);
			}
		};
		
		form.add(submitBT);
		add(form);
	}
	
	@Override
	protected IModel<String> getSubTitle()
	{
		return new ResourceModel("submenu.imageUploadPage");
	}
	
}
