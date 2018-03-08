/**
 * 
 */
package cz.zutrasoft.base.servicesimpl;

import java.util.List;

import cz.zutrasoft.base.services.CategoryService;
import cz.zutrasoft.database.dao.ICategoryDao;
import cz.zutrasoft.database.daoimpl.CategoryDaoImpl;
import cz.zutrasoft.database.model.Category;
import cz.zutrasoft.database.model.CategoryWithArticles;

/**
 * @author Michal
 *
 */
public class CategoryServiceImpl implements CategoryService
{
	private ICategoryDao categoryDao = new CategoryDaoImpl();
	
	/* (non-Javadoc)
	 * @see cz.zutrasoft.base.services.CategoryService#saveCategory(java.lang.String)
	 */
	@Override
	public void saveCategory(String name)
	{
		Category category = new Category();       
        category.setName(name);  
        
        categoryDao.saveCategory(category);
	}

	
	/* (non-Javadoc)
	 * @see cz.zutrasoft.base.services.CategoryService#saveCategory(cz.zutrasoft.database.model.Category)
	 */
	@Override
	public void saveCategory(Category category)
	{
		categoryDao.saveCategory(category);
	}

	
	/* (non-Javadoc)
	 * @see cz.zutrasoft.base.services.CategoryService#getAllCategories()
	 */
	@Override
	public List<Category> getAllCategories()
	{
		return categoryDao.getAllCategories();
	}

	
	/* (non-Javadoc)
	 * @see cz.zutrasoft.base.services.CategoryService#getCategoryByName(java.lang.String)
	 */
	@Override
	public Category getCategoryByName(String name)
	{
		return categoryDao.getCategoryByName(name);
	}


	/* (non-Javadoc)
	 * @see cz.zutrasoft.base.services.CategoryService#getCategoryById(long)
	 */
	@Override
	public Category getCategoryById(long id)
	{
		return categoryDao.getCategoryById(id);
	}


	/* (non-Javadoc)
	 * @see cz.zutrasoft.base.services.CategoryService#deleteCategory(cz.zutrasoft.database.model.Category)
	 */
	@Override
	public void deleteCategory(Category categoryDel)
	{
		categoryDao.delete(categoryDel);
	}


	@Override
	public List<CategoryWithArticles> getAllCategoriesWithArticles()
	{
		return categoryDao.getAllCategoriesWithArticles();
	}

	
}
