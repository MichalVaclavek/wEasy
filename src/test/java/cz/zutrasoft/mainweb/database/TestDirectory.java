/**
 * 
 */
package cz.zutrasoft.mainweb.database;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runners.Parameterized.Parameter;

import cz.zutrasoft.base.services.ICategoryService;
import cz.zutrasoft.base.services.IDirectoryService;
import cz.zutrasoft.base.servicesimpl.CategoryService;
import cz.zutrasoft.base.servicesimpl.DirectoryService;
import cz.zutrasoft.database.dao.IDirectoryDao;
import cz.zutrasoft.database.daoimpl.DirectoryDaoImpl;
import cz.zutrasoft.database.model.Category;
import cz.zutrasoft.database.model.Directory;

/**
 * Tests all methods important for working with {@link cz.zutrasoft.database.model.Directory} - both methods in DAO level {@code cz.zutrasoft.database.daoimpl},
 * and Service level {@code cz.zutrasoft.base.servicesimpl} are tested.
 * 
 * @author Michal Václavek
 *
 */
public class TestDirectory
{
	private static IDirectoryDao directoryDao;
	
	private static IDirectoryService directoryService;
	private static ICategoryService categoryService;
	
	private static Category categoryForDirectory;
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		categoryService = CategoryService.getInstance();
		directoryService = DirectoryService.getInstance();
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
        List<Directory> directories = directoryService.getAllDirectories();
        assertNotNull(directories);
        assertTrue(directories.size() > 0);              
	}
    
    /* ===== CREATE ======= READ ========= DELETE ================ */
		
	@Parameter
    public String newDirectoryNameDAO = "ImagesDirInVeverkyDAO";	
	
	/**
	 * Performs 3 basic DB operations, Create, Read, Delete using DAO level methods.
	 */
	@Test
	public void test_CRD_Directory_DAO()
	{
		// ------ CREATE ----------- //
		int numberOfDirectoriesBeforeCreate = directoryDao.getAllDirectories().size();    
        
        // Create and save new Directory
        Directory directory = new Directory();       
        directory.setName(newDirectoryNameDAO);                 
        directory.setCategory(categoryForDirectory);
        
        directoryDao.saveDirectory(directory);
               
        // Number of Directories increased by 1
        assertTrue(directoryDao.getAllDirectories().size() == (numberOfDirectoriesBeforeCreate + 1)); 
        
        // 	------ READ ----------- //    	
        Directory directoryRead = directoryDao.getDirectoryFromCategory(newDirectoryNameDAO, categoryForDirectory);
        
        assertNotNull(directoryRead);        
        assertTrue(directoryRead.getName().equalsIgnoreCase(newDirectoryNameDAO));
        assertTrue(directoryRead.getCategory().getName().equalsIgnoreCase(categoryForDirectory.getName())); 
        
        //  ------ DELETE ----------- //
        directoryDao.delete(newDirectoryNameDAO, categoryForDirectory);
        
        // Number of Directories decreased by 1
        assertTrue(directoryDao.getAllDirectoriesForCategory(categoryForDirectory).size() == numberOfDirectoriesBeforeCreate);         
	}
	
	@Parameter
    public String newDirectoryNameService = "ImagesDirService";
   
    @Rule
    public ExpectedException exception = ExpectedException.none();
	
	/**
	 * Performs 3 basic DB operations, Create, Read, Delete using Service level methods.
	 * Also tests that a new {@code Directory} with already used name within {@code Category} cannot be created.
	 */
	@Test
	public void test_CRD_Directory_Service()
	{
		// CREATE
		int numberOfDirectoriesBeforeCreate = directoryService.getAllDirectories().size();       
      	     
        Directory directory = new Directory();       
        directory.setName(newDirectoryNameService);                 
        directory.setCategory(categoryForDirectory);
        
        directoryService.saveDirectory(directory);
        
        // Number of Directories increased by 1
        assertTrue(directoryService.getAllDirectories().size() == (numberOfDirectoriesBeforeCreate + 1));
        
        // SAVE SAME DIRECTORY AGAIN - THROWS EXCEPTION        
        Directory directory2 = new Directory();       
        directory2.setName(newDirectoryNameService);                 
        directory2.setCategory(categoryForDirectory);
        
        exception.expect(javax.persistence.PersistenceException.class);
        directoryService.saveDirectory(directory2);
        
        // Previous attempt to save same Directory failed, number of Directories unchanged
        assertTrue(directoryService.getAllDirectories().size() == (numberOfDirectoriesBeforeCreate + 1));
                       
        // READ
        Directory directoryRead = directoryService.getOneDirectoryFromCategory(newDirectoryNameService, categoryForDirectory);
        
        assertNotNull(directoryRead);       
        assertTrue(directoryRead.getName().equalsIgnoreCase(newDirectoryNameService));
        assertTrue(directoryRead.getCategory().getName().equalsIgnoreCase(categoryForDirectory.getName())); 
                
        // DELETE       
        directoryService.deleteDirectory(newDirectoryNameService, categoryForDirectory);        
        // Number of Directories decreased by 1
        assertTrue(directoryService.getAllDirectoriesForCategory(categoryForDirectory).size() == numberOfDirectoriesBeforeCreate);  
	}
			
	
}
