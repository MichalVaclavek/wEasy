package cz.zutrasoft.database.dao;

import java.util.List;

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
	
	/**
	 * Gets all {@link CategoryWithArticles} objects (to show in UI).
	 * 
	 * @return all {@link CategoryWithArticles} objects
	 */
	public List<CategoryWithArticles> getAllCategoriesWithArticles();
}
