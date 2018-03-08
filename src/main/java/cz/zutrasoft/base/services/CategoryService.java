/**
 * 
 */
package cz.zutrasoft.base.services;

import java.util.List;

import cz.zutrasoft.database.model.Category;
import cz.zutrasoft.database.model.CategoryWithArticles;

/**
 * @author Michal
 *
 */
public interface CategoryService
{
	public void saveCategory(String name);
	public void saveCategory(Category category);
	
	public List<Category> getAllCategories();	
	public List<CategoryWithArticles> getAllCategoriesWithArticles();
	
	public Category getCategoryByName(String name);
	public Category getCategoryById(long id);
	
	public void deleteCategory(Category categorDel);
	
}
