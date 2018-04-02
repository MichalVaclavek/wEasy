/**
 * 
 */
package cz.zutrasoft.mainweb.database;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runners.Parameterized.Parameter;

import cz.zutrasoft.base.services.ICategoryService;
import cz.zutrasoft.base.servicesimpl.CategoryService;
import cz.zutrasoft.database.dao.ICategoryDao;
import cz.zutrasoft.database.daoimpl.CategoryDaoImpl;
import cz.zutrasoft.database.model.Category;
import cz.zutrasoft.database.model.CategoryWithArticles;

/**
 * Tests all methods important for working with {@link cz.zutrasoft.database.model.Category} - both methods in DAO level {@code cz.zutrasoft.database.daoimpl},
 * and Service level {@code cz.zutrasoft.base.servicesimpl} are tested.
 * 
 * @author Michal Václavek
 *
 */
public class TestCategory
{	
	
	private static ICategoryDao categoryDao;
	
	private static ICategoryService categoryService;
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{ 
		categoryDao = new CategoryDaoImpl();
		categoryService = CategoryService.getInstance();
		
		Category categ = new Category("Test_Category1");		
		categoryService.saveCategory(categ);
		Category categ2 = new Category("Test_Category2");		
		categoryService.saveCategory(categ2);
	}
	
	@AfterClass
	public static void clearAfterClass() throws Exception
	{
		categoryService.getAllCategories().forEach(categoryService::deleteCategory);
	}

	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
	}

	@Parameter
    public String newCategoryNameForDao = "Veverky5";
	

	@Test
    public void test_Create_New_Category_DAO()
	{	       				
		int numberOfCategoriesBeforeCreate = categoryDao.getAllCategories().size();   
        
        Category category = new Category();       
        category.setName(newCategoryNameForDao);  
        
        categoryDao.saveCategory(category);                
        assertTrue(categoryDao.getAllCategories().size() == (numberOfCategoriesBeforeCreate + 1));         
	}
	
	@Parameter
    public String newCategoryNameForService = "Ježeček5";
	
	
	@Test
    public void test_Create_And_Delete_Category_Service()
	{	       				
		// Create and save new Category		
		int numberOfCategoriesBeforeCreate = categoryService.getAllCategories().size(); 
		
        Category category = new Category();       
        category.setName(newCategoryNameForService);        
        categoryService.saveCategory(category);                        
        assertTrue(categoryService.getAllCategories().size() == (numberOfCategoriesBeforeCreate + 1)); 
        
        Category categoryGet = categoryService.getCategoryByName(newCategoryNameForService);        
        assertTrue(categoryGet.getName().equals(category.getName()));
        
        // Delete Category
        Category categorDel = categoryService.getCategoryByName(newCategoryNameForService);          
        categoryService.deleteCategory(categorDel);
        // Number of Categories decreased by 1
        assertTrue(categoryService.getAllCategories().size() == numberOfCategoriesBeforeCreate);  
	}
	
	@Test
	public void test_List_All_Categories_Service()
	{
        List<Category> categories = categoryService.getAllCategories();
        assertNotNull(categories);
        assertTrue(categories.size() > 0);  
	}
	
	@Test
	public void test_List_All_Categories_DAO()
	{
        List<Category> categories = categoryDao.getAllCategories();
        assertNotNull(categories);
        assertTrue(categories.size() > 0);       
	}
	
	
	@Rule
    public ExpectedException exception = ExpectedException.none();
	
	@Parameter
    public String categoryNameToCreateAgain = "Jezecek2";
	
	/**
	 * Tests that new Category cannot be saved if it's name is already used for another Category.
	 * Exception javax.persistence.PersistenceException.class should be thrown.
	 */
	@Test
	public void test_New_Category_Cannot_Be_Created_NameNotUnique()
	{
		exception.expect(javax.persistence.PersistenceException.class);
		
		Category category = new Category();       
        category.setName(categoryNameToCreateAgain);        
        categoryService.saveCategory(category);    
        
        // New Category with the same name
        Category category2 = new Category();       
        category2.setName(categoryNameToCreateAgain);
        // Here the exception is thrown
        categoryService.saveCategory(category2); 
	}
	
	@Parameter
    public String newCategoryNameString_For_DAO = "Ježeček6";
	
	/**
	 * Test method for {@link cz.zutrasoft.database.daoimpl.CategoryDaoImpl#saveCategory(java.lang.String)}.
	 */
	@Test
	public void test_Save_Category_String()
	{ 
		int numberOfCategoriesBeforeCreate = categoryService.getAllCategories().size(); 		
        categoryDao.saveCategory(newCategoryNameString_For_DAO);        
        assertTrue(categoryService.getAllCategories().size() == (numberOfCategoriesBeforeCreate + 1));         
	}

	/**
	 * Test method for {@link cz.zutrasoft.database.daoimpl.CategoryDaoImpl#getAllCategoriesWithArticles()}.
	 */
	@Test
	public void test_Get_All_Categories_WithArticles()
	{
        List<CategoryWithArticles> categories = categoryDao.getAllCategoriesWithArticles();
        assertNotNull(categories);
        assertTrue(categories.size() > 0); 
	}

}
