package cz.zutrasoft.internal.menus;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;

import cz.zutrasoft.internal.pages.createcategory.CreateCategoryPage;
import cz.zutrasoft.internal.pages.createdirectory.CreateDirectoryPage;
import cz.zutrasoft.internal.pages.editwithmodal.EditPage;
import cz.zutrasoft.internal.pages.imageupload.ImageUploadPage;
import cz.zutrasoft.internal.pages.listallarticles.ListAllArticlesPage;
import cz.zutrasoft.internal.pages.listallimages.ListAllImagesPage;

public class AdministrationMenu extends Panel 
{

	private static final long serialVersionUID = 2362601721898307535L;

	public AdministrationMenu(String id)
	{
		super(id);
		
		add(new Link("edit")
		{
			@Override
			public void onClick()
			{
				setResponsePage(EditPage.class);
			}
		});
		
		add(new Link("listAllArticles")
		{
			@Override
			public void onClick()
			{
				setResponsePage(ListAllArticlesPage.class);
			}
		});
		
		add(new Link("imageUpload")
		{
			@Override
			public void onClick()
			{
				setResponsePage(ImageUploadPage.class);
			}
		});
		
		add(new Link("listAllImages")
		{
			@Override
			public void onClick()
			{
				setResponsePage(ListAllImagesPage.class);
			}
		});
		
		add(new Link("createCategory")
		{
			@Override
			public void onClick()
			{
				setResponsePage(CreateCategoryPage.class);
			}
		});
		
		add(new Link("createDirectory")
		{
			@Override
			public void onClick()
			{
				setResponsePage(CreateDirectoryPage.class);
			}
		});
	}
	
}
