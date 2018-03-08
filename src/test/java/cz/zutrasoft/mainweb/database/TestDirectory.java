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

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
	}

	private IDirectoryDao directoryDao;
	
	private DirectoryService directoryService;
	

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		directoryDao = new DirectoryDaoImpl();
		directoryService = new DirectoryServiceImpl();
	}
	
		
    /* ========================== GET FIRST FOUND DIRECTORY =================================== */
    
	@Parameter
    public String oldDirectoryNameToGet = "ObrázkyVeverky";	
	@Parameter
    public String oldCategoryNameToGet = "Veverky";

      
    @Test
	public void test_Get_FirstDirectory_DAO()
	{  	
        Directory directory = directoryDao.getFirstDirectoryByName(oldDirectoryNameToGet);        
        assertNotNull(directory);       
        assertTrue(directory.getName().equalsIgnoreCase(oldDirectoryNameToGet));                 
	}
    
    @Test
	public void test_Get_AllDirectories_DAO()
	{  	
    	// získání všech Directories z databáze
        List<Directory> directories = directoryService.getAllDirectories();
        assertNotNull(directories);
        assertTrue(directories.size() > 0);              
	}
    
    
    /* ================= DELETE FIRST FOUND DIRECTORY ========================== */
    
    @Parameter
    public String oldDirectoryName_To_Delete_DAO = "Obrázky_Pejsci3";	
    	
	@Test
	public void test_Delete_First_Directory_DAO()
	{     
        Directory directoryDel = directoryDao.getFirstDirectoryByName(oldDirectoryName_To_Delete_DAO);
        
        List<Directory> directories = directoryDao.getAllDirectories();
        
        int numberOfDirectoriesBeforeDelete = directories.size();
        directoryDao.delete(directoryDel);
        
        // Pocet vsech Directory se snizil o 1
        assertTrue(directoryDao.getAllDirectories().size() == (numberOfDirectoriesBeforeDelete - 1));      
	}

	
	@Parameter
    public String oldDirectoryName_To_Delete_Service = "Obrázky_Pejsci4";	
	
	@Test
	public void test_Delete_First_Directory_Service()
	{
		Directory directoryDel = directoryService.getDirectoryByName(oldDirectoryName_To_Delete_Service);
        
        List<Directory> directories = directoryService.getAllDirectories();
        
        int numberOfDirectoriesBeforeDelete = directories.size();
        directoryService.deleteDirectory(directoryDel);
        
        // Pocet vsech Direcory se snizil o 1
        assertTrue(directoryService.getAllDirectories().size() == (numberOfDirectoriesBeforeDelete - 1));  
	}
		
	@Parameter
    public String knownCategoryNameDAO = "Veverky";
	@Parameter
    public String newDirectoryNameDAO = "ObrázkyDirInVeverkyDAO";	

	
	/**
	 * Provede test 3 základních operací s DB pomocí DAO vrstvy, Create, Read, Delete (Update i Directory není potřeba)
	 */
	@Test
	public void test_CRD_Directory_DAO()
	{
		// ------ CREATE ----------- //
		
		int numberOfDirectoriesBeforeCreate = directoryDao.getAllDirectories().size();    
    	
    	ICategoryDao categoryDao = new CategoryDaoImpl();
        Category category = categoryDao.getCategoryByName(knownCategoryNameDAO);
        
        // Vytvoreni a ulozeni noveho adresare, napr. pro fotky ?
        Directory directory = new Directory();       
        directory.setName(newDirectoryNameDAO);                 
        directory.setCategory(category);
        
        directoryDao.saveDirectory(directory);
               
        // Pocet vsech Directory se musi zvysit o 1
        assertTrue(directoryDao.getAllDirectories().size() == (numberOfDirectoriesBeforeCreate + 1)); 
        
        // 	------ READ ----------- //
    	
        Directory directoryRead = directoryDao.getDirectoryFromCategory(newDirectoryNameDAO, category);
        
        assertNotNull(directoryRead);
        
        assertTrue(directoryRead.getName().equalsIgnoreCase(newDirectoryNameDAO));
        assertTrue(directoryRead.getCategory().getName().equalsIgnoreCase(knownCategoryNameDAO)); 
        
        //  ------ DELETE ----------- //
        int numberOfDirectoriesBeforeDelete = directoryDao.getAllDirectoriesForCategory(category).size();
        
        directoryDao.delete(newDirectoryNameDAO, category);
        
        // Pocet vsech Direcory se snizil o 1
        assertTrue(directoryDao.getAllDirectoriesForCategory(category).size() == (numberOfDirectoriesBeforeDelete - 1)); 
        
	}
	
	@Parameter
    public String newDirectoryNameService = "ObrázkyDirInJežečekService";
    @Parameter
    public String knownCategoryNameForService = "Ježeček";
    
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
		int numberOfDirectoriesBeforeCreate = directoryDao.getAllDirectories().size();       
      	
    	CategoryService categoryService = new CategoryServiceImpl();
        Category category = categoryService.getCategoryByName(knownCategoryNameForService);
        
        // Vytvoreni a ulozeni noveho adresare, napr. pro fotky ?
        Directory directory = new Directory();       
        directory.setName(newDirectoryNameService);                 
        directory.setCategory(category);
        
        directoryService.saveDirectory(directory);
        
        // CREATE SAME DIRECTORY AGAIN - THROWS EXCEPTION
        exception.expect(javax.persistence.PersistenceException.class);
        directoryService.saveDirectory(directory);
        
        // Pocet vsech Directory se musi zvysit o 1
        assertTrue(directoryService.getAllDirectories().size() == (numberOfDirectoriesBeforeCreate + 1));
        
        // READ
        Directory directoryRead = directoryService.getOneDirectoryFromCategory(newDirectoryNameService, category);
        
        assertNotNull(directoryRead);
        
        assertTrue(directoryRead.getName().equalsIgnoreCase(newDirectoryNameService));
        assertTrue(directoryRead.getCategory().getName().equalsIgnoreCase(knownCategoryNameForService)); 
        
        
        // DELETE
        int numberOfDirectoriesBeforeDelete = directoryService.getAllDirectoriesForCategory(category).size();
        
        directoryService.deleteDirectory(newDirectoryNameService, category);
        
        // Pocet vsech Direcory se snizil o 1
        assertTrue(directoryService.getAllDirectoriesForCategory(category).size() == (numberOfDirectoriesBeforeDelete - 1));  
	}
		
	
	
}
