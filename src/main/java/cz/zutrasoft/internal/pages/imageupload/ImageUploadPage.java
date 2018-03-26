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

import cz.zutrasoft.base.services.ICategoryService;
import cz.zutrasoft.base.services.IDirectoryService;
import cz.zutrasoft.base.services.IImageService;
import cz.zutrasoft.base.servicesimpl.CategoryService;
import cz.zutrasoft.base.servicesimpl.DirectoryService;
import cz.zutrasoft.base.servicesimpl.ImageService;
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
	
	@SuppressWarnings({"serial"})
	public ImageUploadPage()
	{
		final FeedbackPanel feedback = new FeedbackPanel("feedback");
		feedback.setOutputMarkupId(true);
		add(feedback);
		
		Form<Object> form = new Form<Object>("form")
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
							IImageService imageService = ImageService.getInstance();
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
				ICategoryService categoryService = CategoryService.getInstance();
				return categoryService.getAllCategories();
			}
		};
		// shows Category "name" property
		ChoiceRenderer<Category> choiceRenderer = new ChoiceRenderer<Category>("name");
		categoryDDCH = new DropDownChoice<Category>("categories", new PropertyModel<Category>(this, "selectedCategory"), categoryModel, choiceRenderer);
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
				IDirectoryService  dirService = DirectoryService.getInstance();
				if (selectedCategory == null)
				{
					return dirService.getAllDirectories();
				} else
				{
					return dirService.getAllDirectoriesForCategory(selectedCategory);
				}
			}
		};
		// shows Directory "name" property
		ChoiceRenderer<Directory> directoryChoiceRenderer = new ChoiceRenderer<Directory>("name");
		directoryDDCH = new DropDownChoice<Directory>("directories", new PropertyModel<Directory>(this, "selectedDirectory"), directoryModel, directoryChoiceRenderer);
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
