package cz.zutrasoft.database.dao;

import java.util.List;
import java.util.Set;

import cz.zutrasoft.database.model.Article;
import cz.zutrasoft.database.model.Category;
import cz.zutrasoft.database.model.CategoryWithArticles;

public interface ICategoryDao
{
	public void saveCategory(String name);
	public void saveCategory(Category category);
	public List<Category> getAllCategories();
	
	public Category getCategoryByName(String name);
	public Category getCategoryById(long id);
	public void delete(Category categorDel);
		
	public List<CategoryWithArticles> getAllCategoriesWithArticles();
}
