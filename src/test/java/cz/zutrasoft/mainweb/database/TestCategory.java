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
 * Otestuje vsechny metody dulezite pro praci s Category tj. jak metody v DAO vrstve {@code cz.zutrasoft.database.daoimpl},
 * tak i metody v Service vrsve {@code cz.zutrasoft.base.servicesimpl}
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
		//categoryService = new CategoryServiceImpl();
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
		//categoryDao = new CategoryDaoImpl();
		//categoryService = new CategoryServiceImpl();
	}

	@Parameter
    public String newCategoryNameForDao = "Veverky5";
	

	@Test
    public void test_Create_New_Category_DAO()
	{	       				
		// Vytvoreni a ulozeni nove kategorie clanku
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
		// Vytvoreni a ulozeni nove kategorie clanku		
		int numberOfCategoriesBeforeCreate = categoryService.getAllCategories().size(); 
		
        Category category = new Category();       
        category.setName(newCategoryNameForService);  
        
        categoryService.saveCategory(category);                
        
        assertTrue(categoryService.getAllCategories().size() == (numberOfCategoriesBeforeCreate + 1)); 
        
        Category categoryGet = categoryService.getCategoryByName(newCategoryNameForService);
        
        assertTrue(categoryGet.getName().equals(category.getName()));
        
     // Smazani kategorii
     	//List<Category> categories = categoryService.getAllCategories();
     		
        Category categorDel = categoryService.getCategoryByName(newCategoryNameForService);
             
        //int numberOfCategoriesBeforeDelete = categories.size();
        categoryService.deleteCategory(categorDel);
             
        // Pocet vsech clanku se snizil o 1
        assertTrue(categoryService.getAllCategories().size() == numberOfCategoriesBeforeCreate);  
	}
	
	@Test
	public void test_List_All_Categories_Service()
	{
		// získání všech Categories z databáze
        List<Category> categories = categoryService.getAllCategories();
        assertNotNull(categories);
        assertTrue(categories.size() > 0);  
	}
	
	@Test
	public void test_List_All_Categories_DAO()
	{
		// získání všech Categories z databáze
        List<Category> categories = categoryDao.getAllCategories();
        assertNotNull(categories);
        assertTrue(categories.size() > 0);       
	}
	
	
	@Rule
    public ExpectedException exception = ExpectedException.none();
	
	@Parameter
    public String categoryNameToCreateAgain = "Ježeček2";
	
	/**
	 * Otestuje, ze nejde vytvorit nova kategorie se jmenem ktere je uz pouzito
	 * Mela by byt vyhozena vyjimka typu javax.persistence.PersistenceException.class
	 */
	@Test
	public void testNewCategoryCannotBeCreated_NameNotUnique()
	{
		exception.expect(javax.persistence.PersistenceException.class);
		
		Category category = new Category();       
        category.setName(categoryNameToCreateAgain);  
        
        categoryService.saveCategory(category);    
        
        Category category2 = new Category();       
        category2.setName(categoryNameToCreateAgain);  
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
		// získání všech objektu CategoryWithArticles z DB pomoci DAO
        List<CategoryWithArticles> categories = categoryDao.getAllCategoriesWithArticles();
        assertNotNull(categories);
        assertTrue(categories.size() > 0); 
	}

}
