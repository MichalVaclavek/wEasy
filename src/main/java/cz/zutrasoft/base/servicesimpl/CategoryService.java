/**
 * 
 */
package cz.zutrasoft.base.servicesimpl;

import java.util.List;

import cz.zutrasoft.base.services.ICategoryService;
import cz.zutrasoft.database.dao.ICategoryDao;
import cz.zutrasoft.database.daoimpl.CategoryDaoImpl;
import cz.zutrasoft.database.model.Category;
import cz.zutrasoft.database.model.CategoryWithArticles;

/**
 * Singleton implementation of the I_CategoryService interface
 * 
 * @author Michal Václavek
 *
 */
public class CategoryService implements ICategoryService
{
	private ICategoryDao categoryDao = new CategoryDaoImpl();
	
	private static class SingletonHolder
	{
        private static final CategoryService SINGLE_INSTANCE = new CategoryService();
    }
	
	/**
	 * @return one instance of {@code CategoryService}
	 */
	public static CategoryService getInstance()
	{				
		return SingletonHolder.SINGLE_INSTANCE;			
	}
	
	private CategoryService()
	{}
	
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
