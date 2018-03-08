/**
 * 
 */
package cz.zutrasoft.mainweb.database;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runners.Parameterized.Parameter;

import cz.zutrasoft.base.services.CategoryService;
import cz.zutrasoft.base.servicesimpl.CategoryServiceImpl;
import cz.zutrasoft.database.dao.ICategoryDao;
import cz.zutrasoft.database.daoimpl.CategoryDaoImpl;
import cz.zutrasoft.database.model.Category;
import cz.zutrasoft.database.model.CategoryWithArticles;

/**
 * Otestuje vsechny metody dulezite pro praci s Category tj. jak metody v DAO vrstve {@code cz.zutrasoft.database.daoimpl},
 * tak i metody v Service vrsve {@code cz.zutrasoft.base.servicesimpl}
 * 
 * @author Michal Václavek
 *
 */
public class TestCategory
{	
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
	}


	private ICategoryDao categoryDao;
	
	private CategoryService categoryService;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		categoryDao = new CategoryDaoImpl();
		categoryService = new CategoryServiceImpl();
	}

	@Parameter
    public String newCategoryNameForDao = "Veverky5";
	

	@Test
    public void testCreateNewCategoryDao()
	{	       				
		// Vytvoreni a ulozeni nove kategorie clanku
		//List<Category> categories = categoryService.getAllCategories();
		       
        int numberOfCategoriesBeforeCreate = categoryDao.getAllCategories().size();   
        
        Category category = new Category();       
        category.setName(newCategoryNameForDao);  
        
        categoryDao.saveCategory(category);        
        
        assertTrue(categoryDao.getAllCategories().size() == (numberOfCategoriesBeforeCreate + 1)); 
        
	}
	
	@Parameter
    public String newCategoryNameForService = "Ježeček5";
	
	
	@Test
    public void testCreateNewCategoryService()
	{	       				
		// Vytvoreni a ulozeni nove kategorie clanku
		
		int numberOfCategoriesBeforeCreate = categoryService.getAllCategories().size(); 
		
        Category category = new Category();       
        category.setName(newCategoryNameForService);  
        
        categoryService.saveCategory(category);                
        
        assertTrue(categoryService.getAllCategories().size() == (numberOfCategoriesBeforeCreate + 1)); 
        
        Category categoryGet = categoryService.getCategoryByName(newCategoryNameForService);
        
        assertTrue(categoryGet.getName().equals(category.getName()));
	}
	
	@Test
	public void testListAllCategoriesService()
	{
		// získání všech Categories z databáze
        List<Category> categories = categoryService.getAllCategories();
        assertNotNull(categories);
        assertTrue(categories.size() > 0);  
	}
	
	@Test
	public void testListAllCategoriesDao()
	{
		// získání všech Categories z databáze
        List<Category> categories = categoryDao.getAllCategories();
        assertNotNull(categories);
        assertTrue(categories.size() > 0);       
	}
	
	
	@Rule
    public ExpectedException exception = ExpectedException.none();
	
	@Parameter
    public String oldCategoryNameToCreateAgain = "Ježeček2";
	
	/**
	 * Otestuje, ze nejde vytvorit nova kategorie se jmenem ktere je uz pouzito
	 * Mela by byt vyhozena vyjimka typu javax.persistence.PersistenceException.class
	 */
	@Test
	public void testNewCategoryCannotBeCreated_NameNotUnique()
	{
		exception.expect(javax.persistence.PersistenceException.class);
		
		Category category = new Category();       
        category.setName(oldCategoryNameToCreateAgain);  
        
        categoryService.saveCategory(category);                    
	}
	
	@Parameter
    public String newCategoryNameString_For_DAO = "Ježeček6";
	
	/**
	 * Test method for {@link cz.zutrasoft.database.daoimpl.CategoryDaoImpl#saveCategory(java.lang.String)}.
	 */
	@Test
	public void testSaveCategoryString()
	{ 
		int numberOfCategoriesBeforeCreate = categoryService.getAllCategories().size(); 
		
        categoryDao.saveCategory(newCategoryNameString_For_DAO);
        
        assertTrue(categoryService.getAllCategories().size() == (numberOfCategoriesBeforeCreate + 1));         
	}


	@Parameter
    public String oldCategoryNameToDelete = "Veverky4";
	
	/**
	 * Test method for {@link cz.zutrasoft.database.daoimpl.CategoryDaoImpl#delete(cz.zutrasoft.database.model.Category)}.
	 */
	@Test
	public void testDeleteCategoryService()
	{
		// Smazani kategorii
		List<Category> categories = categoryService.getAllCategories();
		
        Category categorDel = categoryService.getCategoryByName(oldCategoryNameToDelete);
        
        int numberOfCategoriesBeforeDelete = categories.size();
        categoryService.deleteCategory(categorDel);
        
        // Pocet vsech clanku se snizil o 1
        assertTrue(categoryService.getAllCategories().size() == (numberOfCategoriesBeforeDelete - 1));  
        
	}


	/**
	 * Test method for {@link cz.zutrasoft.database.daoimpl.CategoryDaoImpl#getAllCategoriesWithArticles()}.
	 */
	@Test
	public void testGetAllCategoriesWithArticles()
	{
		// získání všech objektu CategoryWithArticles z DB pomoci DAO
        List<CategoryWithArticles> categories = categoryDao.getAllCategoriesWithArticles();
        assertNotNull(categories);
        assertTrue(categories.size() > 0); 
	}

}
