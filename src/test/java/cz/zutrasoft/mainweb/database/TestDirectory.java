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

import cz.zutrasoft.base.services.CategoryService;
import cz.zutrasoft.base.services.DirectoryService;
import cz.zutrasoft.base.servicesimpl.CategoryServiceImpl;
import cz.zutrasoft.base.servicesimpl.DirectoryServiceImpl;
import cz.zutrasoft.database.dao.ICategoryDao;
import cz.zutrasoft.database.dao.IDirectoryDao;
import cz.zutrasoft.database.daoimpl.CategoryDaoImpl;
import cz.zutrasoft.database.daoimpl.DirectoryDaoImpl;
import cz.zutrasoft.database.model.Category;
import cz.zutrasoft.database.model.Directory;

/**
 * Otestuje vsechny metody dulezite pro praci s {@code Directory} tj. jak metody v DAO vrstve {@code cz.zutrasoft.database.daoimpl},
 * tak i metody v Service vrsve {@code cz.zutrasoft.base.servicesimpl}
 * 
 * @author Michal Václavek
 *
 */
public class TestDirectory
{

	private static IDirectoryDao directoryDao;
	
	private static DirectoryService directoryService;
	private static CategoryService categoryService;
	
	private static Category categoryForDirectory;
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		//categoryService = new CategoryServiceImpl();
		categoryService = CategoryServiceImpl.getInstance();
		//directoryService = new DirectoryServiceImpl();
		directoryService = DirectoryServiceImpl.getInstance();
		directoryDao = new DirectoryDaoImpl();
		
		categoryForDirectory = new Category("Test_Category_For_Directory");		
		categoryService.saveCategory(categoryForDirectory);
		
		Directory directory = new Directory();       
        directory.setName("TestDir1");                 
        directory.setCategory(categoryForDirectory);
        
        directoryService.saveDirectory(directory);
        
        Directory directory2 = new Directory();       
        directory2.setName("TestDir2");                 
        directory2.setCategory(categoryForDirectory);
        
        directoryService.saveDirectory(directory2);
		
	}
	
	@AfterClass
	public static void clearAfterClass() throws Exception
	{
		directoryService.getAllDirectories().forEach(directoryService::deleteDirectory);
		categoryService.deleteCategory(categoryForDirectory);
	}
	
		
    
    @Test
	public void test_Get_AllDirectories_Service()
	{  	
    	// získání všech Directories z databáze
        List<Directory> directories = directoryService.getAllDirectories();
        assertNotNull(directories);
        assertTrue(directories.size() > 0);              
	}
    
    /* ===== CREATE ======= READ ========= DELETE ================ */

		
	//@Parameter
    //public String knownCategoryNameDAO = "Veverky";
	@Parameter
    public String newDirectoryNameDAO = "ImagesDirInVeverkyDAO";	

	
	/**
	 * Provede test 3 základních operací s DB pomocí DAO vrstvy, Create, Read, Delete (Update i Directory není potřeba)
	 */
	@Test
	public void test_CRD_Directory_DAO()
	{
		// ------ CREATE ----------- //
		int numberOfDirectoriesBeforeCreate = directoryDao.getAllDirectories().size();    
    	
    	//ICategoryDao categoryDao = new CategoryDaoImpl();
        //Category category = categoryDao.getCategoryByName(knownCategoryNameDAO);
        
        // Vytvoreni a ulozeni noveho adresare, napr. pro fotky ?
        Directory directory = new Directory();       
        directory.setName(newDirectoryNameDAO);                 
        directory.setCategory(categoryForDirectory);
        
        directoryDao.saveDirectory(directory);
               
        // Pocet vsech Directory se musi zvysit o 1
        assertTrue(directoryDao.getAllDirectories().size() == (numberOfDirectoriesBeforeCreate + 1)); 
        
        // 	------ READ ----------- //
    	
        Directory directoryRead = directoryDao.getDirectoryFromCategory(newDirectoryNameDAO, categoryForDirectory);
        
        assertNotNull(directoryRead);
        
        assertTrue(directoryRead.getName().equalsIgnoreCase(newDirectoryNameDAO));
        assertTrue(directoryRead.getCategory().getName().equalsIgnoreCase(categoryForDirectory.getName())); 
        
        //  ------ DELETE ----------- //
        //int numberOfDirectoriesBeforeDelete = directoryDao.getAllDirectoriesForCategory(categoryForDirectory).size();
        
        directoryDao.delete(newDirectoryNameDAO, categoryForDirectory);
        
        // Pocet vsech Direcory se snizil o 1
        assertTrue(directoryDao.getAllDirectoriesForCategory(categoryForDirectory).size() == numberOfDirectoriesBeforeCreate); 
        
	}
	
	@Parameter
    public String newDirectoryNameService = "ImagesDirService";
   
    @Rule
    public ExpectedException exception = ExpectedException.none();
	
	/**
	 * Provede test 3 základních operací s DB pomocí Service vrstvy, Create, Read, Delete (Update i Directory není potřeba)
	 * 
	 * Taky otestuje, ze Directory se stejnym jmenem v Category nemuye byt votvoreno
	 */
	@Test
	public void test_CRD_Directory_Service()
	{
		// CREATE
		int numberOfDirectoriesBeforeCreate = directoryService.getAllDirectories().size();       
      	
    	//CategoryService categoryService = new CategoryServiceImpl();
        //Category category = categoryService.getCategoryByName(knownCategoryNameForService);
        
        // Vytvoreni a ulozeni noveho adresare, napr. pro fotky ?
        Directory directory = new Directory();       
        directory.setName(newDirectoryNameService);                 
        directory.setCategory(categoryForDirectory);
        
        directoryService.saveDirectory(directory);
        
        // Pocet vsech Directory se musi zvysit o 1
        assertTrue(directoryService.getAllDirectories().size() == (numberOfDirectoriesBeforeCreate + 1));
        
        // SAVE SAME DIRECTORY AGAIN - THROWS EXCEPTION        
        Directory directory2 = new Directory();       
        directory2.setName(newDirectoryNameService);                 
        directory2.setCategory(categoryForDirectory);
        
        exception.expect(javax.persistence.PersistenceException.class);
        directoryService.saveDirectory(directory2);
        
        // Previous attemtp to save same Directory failed, number of Directories unchanged
        assertTrue(directoryService.getAllDirectories().size() == (numberOfDirectoriesBeforeCreate + 1));
                       
        // READ
        Directory directoryRead = directoryService.getOneDirectoryFromCategory(newDirectoryNameService, categoryForDirectory);
        
        assertNotNull(directoryRead);
        
        assertTrue(directoryRead.getName().equalsIgnoreCase(newDirectoryNameService));
        assertTrue(directoryRead.getCategory().getName().equalsIgnoreCase(categoryForDirectory.getName())); 
                
        // DELETE       
        directoryService.deleteDirectory(newDirectoryNameService, categoryForDirectory);        
        // Pocet vsech Direcory se snizil o 1
        assertTrue(directoryService.getAllDirectoriesForCategory(categoryForDirectory).size() == numberOfDirectoriesBeforeCreate);  
	}
		
	
	
}
