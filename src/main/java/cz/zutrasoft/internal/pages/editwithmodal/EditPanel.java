package cz.zutrasoft.internal.pages.editwithmodal;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.ByteArrayResource;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;

import cz.zutrasoft.base.services.ArticleService;
import cz.zutrasoft.base.services.CategoryService;
import cz.zutrasoft.base.services.DirectoryService;
import cz.zutrasoft.base.services.ImageService;
import cz.zutrasoft.base.servicesimpl.ArticleServiceImpl;
import cz.zutrasoft.base.servicesimpl.CategoryServiceImpl;
import cz.zutrasoft.base.servicesimpl.DirectoryServiceImpl;
import cz.zutrasoft.base.servicesimpl.ImageServiceImpl;
import cz.zutrasoft.database.daoimpl.ArticleDaoImpl;
import cz.zutrasoft.database.daoimpl.CategoryDaoImpl;
import cz.zutrasoft.database.daoimpl.DirectoryDaoImpl;
import cz.zutrasoft.database.daoimpl.ImageDaoImpl;
import cz.zutrasoft.database.model.Article;
import cz.zutrasoft.database.model.Category;
import cz.zutrasoft.database.model.Directory;
import cz.zutrasoft.external.pages.homepage.HomePage;

public class EditPanel extends Panel
{
	
	private static final long serialVersionUID = 3972321151431630835L;
	
	private boolean isModalVisible = false;
	private Category selectedCategory;
	private Directory selectedDirectory;
	
	private String textFromEdit;
	private String textFromSend;
	
	private FeedbackPanel feedback;
	private DropDownChoice<Category> categoriesDDCH;
	private DropDownChoice<Directory> directoriesDDCH;
	private TextArea<String> textEdit;
	private TextArea<String> textSend;
	private Article article;
	
	private WebMarkupContainer modal;

	public EditPanel(String id)
	{
		super(id);
		init();
	}
	
	public EditPanel(String id, Article article)
	{
		super(id);
		this.article = article;
		selectedCategory = article.getCategory();
		textFromEdit = article.getText();
		init();
	}
		
	private void init()
	{	
		// add feedback to the page
		feedback = new FeedbackPanel("feedback");
		feedback.setOutputMarkupId(true);
		add(feedback);
		
		Form form = new Form("form");
		form.setOutputMarkupId(true);
		form.add(new AjaxFormSubmitBehavior(form, "submit")
		{
			@Override
			protected void onSubmit(AjaxRequestTarget target)
			{
				target.appendJavaScript("textEditor.modifyTextArea();");
				saveText();
				setResponsePage(HomePage.class);
			}
			
			@Override
			protected void onError(AjaxRequestTarget target)
			{
				target.add(feedback);
			}
		});
		
		add(form);
		
		// model for categories DropDownChoice
		IModel<List<Category>> categoriesModel = new AbstractReadOnlyModel<List<Category>>()
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
		
		// Adds drop down choice to select category -> add it to the model to get just images from this category
		// the default value (null) for selection is specified in .properties file String dropdownchoice_id.null=default value
		categoriesDDCH = new DropDownChoice<>("categories", new PropertyModel<Category>(this, "selectedCategory"), categoriesModel, new CategoriesRenderer());
		categoriesDDCH.add(new OnChangeAjaxBehavior()
		{
			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				target.add(directoriesDDCH);
			}
		});
		categoriesDDCH.setRequired(true);
		categoriesDDCH.setOutputMarkupId(true);
		form.add(categoriesDDCH);
		
		IModel<List<Directory>> directoriesModel = new AbstractReadOnlyModel<List<Directory>>()
		{
			@Override
			public List<Directory> getObject()
			{
				//DirectoryDaoImpl dao = new DirectoryDaoImpl();
				//DirectoryService dirService = new DirectoryServiceImpl();
				DirectoryService  dirService = DirectoryServiceImpl.getInstance();
				if (selectedCategory != null)
				{
					return dirService.getAllDirectoriesForCategory(selectedCategory);
				} else
				{
					return dirService.getAllDirectories();
				}
			}
		};
		
		directoriesDDCH = new DropDownChoice<>("directories", new PropertyModel<Directory>(this, "selectedDirectory"), directoriesModel, new DirectoriesRenderer());
		directoriesDDCH.add(new OnChangeAjaxBehavior()
		{
			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				target.add(modal);
			}
		});
		directoriesDDCH.setOutputMarkupId(true);
		form.add(directoriesDDCH);
		
		// add textarea (it is required)
		textEdit = new TextArea("textEdit", new PropertyModel<String>(this, "textFromEdit"));
		textEdit.setRequired(true);
		textEdit.setOutputMarkupId(true);
		form.add(textEdit);
		
		// add textarea that contains the text with correct tags (not the fake ones)
		textSend = new TextArea("textSend", new PropertyModel<String>(this, "textFromSend"));
		form.add(textSend);
		
		// model that contains images
		IModel<List<cz.zutrasoft.database.model.Image>> imagesModel = new AbstractReadOnlyModel<List<cz.zutrasoft.database.model.Image>>()
		{
			@Override
			public List<cz.zutrasoft.database.model.Image> getObject()
			{
				List<cz.zutrasoft.database.model.Image> images = new ArrayList<>();
				//ImageDaoImpl dao = new ImageDaoImpl();
				//ImageService imageService = new ImageServiceImpl();
				ImageService imageService = ImageServiceImpl.getInstance();
				if (selectedDirectory != null)
				{
					images = imageService.getAllImagesInDirectory(selectedDirectory);
				} 
				return images;
			}
		};
		
		// modal window -> it is WebMarkupContainer
		modal = new WebMarkupContainer("modal")
		{
			protected void onConfigure()
			{
				setVisible(isModalVisible);
			};
		};
		add(modal);
		
		modal.add(new AjaxLink("closeLink")
		{
			@Override
			public void onClick(AjaxRequestTarget target)
			{
				target.add(modal);
				isModalVisible = false;				
			}
		});
		
		// add all images to container -> it uses ListView repeater
		ListView<cz.zutrasoft.database.model.Image> lv = new ListView<cz.zutrasoft.database.model.Image>("lv", imagesModel)
		{
			private final ResourceReference ref = new ImageResourceReference();

			@Override
			protected void populateItem(final ListItem<cz.zutrasoft.database.model.Image> item)
			{
				final cz.zutrasoft.database.model.Image image = item.getModelObject();
				// add link
				AjaxLink imageLink = new AjaxLink("imageLink")
				{
					@Override
					public void onClick(AjaxRequestTarget target)
					{
						PageParameters pp = new PageParameters();
						pp.set("imageId", image.getId());
						CharSequence urlForImage = getRequestCycle().urlFor(ref, pp);
						
						String src = urlForImage.toString();
						String javaScript = "textEditor.insertImage('" + src + "?');";
						target.appendJavaScript(javaScript);
						
						// add modal (WebMarkupContainer) to the target and hide it
						target.add(modal);
						isModalVisible = false;
					}
				};
				item.add(imageLink);
				
				// add image to the link
				imageLink.add(new Image("image", new ByteArrayResource(null, image.getBytes())));
				// add image name to the link
				imageLink.add(new Label("imageLabel", new Model(image.getFileName())));
			}
		};
		modal.add(lv);
		modal.setOutputMarkupPlaceholderTag(true);
		
		form.add(new AjaxLink("showImageModalLink")
		{
			@Override
			public void onClick(AjaxRequestTarget target)
			{
				isModalVisible = !isModalVisible;
				target.add(modal);
			}
		});
		
		form.add(new Button("submitButton"));
	}
	
	// renderer for DropDownChoice
	private class CategoriesRenderer implements IChoiceRenderer<Category> 
	{
		@Override
		public Object getDisplayValue(Category object)
		{
			return object.getName();
		}

		@Override
		public String getIdValue(Category object, int index)
		{
			return String.valueOf(index);
		}
		
		@Override
		public Category getObject(String arg0, IModel<? extends List<? extends Category>> arg1)
		{
			return arg1.getObject().get(Integer.valueOf(arg0));
		}
				
	}
	
	private class DirectoriesRenderer implements IChoiceRenderer<Directory>
	{		
		@Override
		public Object getDisplayValue(Directory object)
		{
			return object.getName();
		}

		@Override
		public String getIdValue(Directory object, int index)
		{
			return String.valueOf(index);
		}

		@Override
		public Directory getObject(String arg0, IModel<? extends List<? extends Directory>> arg1)
		{
			return arg1.getObject().get(Integer.valueOf(arg0));
		}
	}

	public Category getSelectedCategory()
	{
		return selectedCategory;
	}

	public void setSelectedCategory(Category selectedCategory)
	{
		this.selectedCategory = selectedCategory;
	}
	
	public String getText()
	{
		return textFromEdit;
	}

	public void setText(String text)
	{
		this.textFromEdit = text;
	}
	
	private void saveText()
	{
		//ArticleService articleService = new ArticleServiceImpl();
		ArticleService articleService = ArticleServiceImpl.getInstance();
		if (article != null) // Jde o novy clanek
		{
			article.setCategory(selectedCategory);
			article.setText(textFromSend);
			articleService.updateArticle(article);
		} else 
		{ // jde o editaci stavajiciho clanku z DB
			articleService.saveTextAsArticle(textFromSend, selectedCategory);
		}
				
	}
	
	@Override
	public void renderHead(IHeaderResponse response)
	{
		super.renderHead(response);
		
		PackageResourceReference ref = new CssResourceReference(EditPage.class, "edit.css");
		HeaderItem item = CssHeaderItem.forReference(ref);
		response.render(item);
		
		// when editing (text is loaded from database to textarea) replaces tags for the fake ones.
		response.render(OnDomReadyHeaderItem.forScript("textEditor.modifyArea();"));
	}
	
}
