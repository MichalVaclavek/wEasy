/**
 * 
 */
package cz.zutrasoft.base.services;

import java.util.List;

import cz.zutrasoft.database.model.Category;
import cz.zutrasoft.database.model.CategoryWithArticles;

/**
 * Basic service methods to work with Category (of Articles).
 * 
 * @author Michal Václavek
 */
public interface ICategoryService
{
	/**
	 * Saves new {@link Category} name.
	 * 
	 * @param name of Category
	 */
	public void saveCategory(String name);
	
	/**
	 * Saves {@link Category} instance.
	 * 
	 * @param category {@link Category} to be saved
	 */
	public void saveCategory(Category category);
	
	/**
	 * Gets list of all already saved Categories.
	 * 
	 * @return list of all already saved Categories.
	 */
	public List<Category> getAllCategories();
	/**
	 * Gets list of {@link CategoryWithArticles} objects. Made of all already saved Directories and their Categories.  
	 * @return list of CategoryWithArticles objects made of all already saved Directories and their Categories. 
	 */
	public List<CategoryWithArticles> getAllCategoriesWithArticles();
	
	/**
	 * Gets {@link Category} instance of the given name.
	 * 
	 * @param name of {@link Category}
	 * @return {@link Category} instance of the given name.
	 */
	public Category getCategoryByName(String name);
	public Category getCategoryById(long id);
	
	/**
	 * Deletes given {@link Category} instance from repository (DB).
	 * @param categorDel {@link Category} to be deleted.
	 */
	public void deleteCategory(Category categorDel);
	
}
