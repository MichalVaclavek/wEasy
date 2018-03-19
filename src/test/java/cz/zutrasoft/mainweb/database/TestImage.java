package cz.zutrasoft.mainweb.database;

import static org.junit.Assert.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runners.Parameterized.Parameter;

import cz.zutrasoft.base.services.CategoryService;
import cz.zutrasoft.base.services.DirectoryService;
import cz.zutrasoft.base.services.ImageService;
import cz.zutrasoft.base.servicesimpl.CategoryServiceImpl;
import cz.zutrasoft.base.servicesimpl.DirectoryServiceImpl;
import cz.zutrasoft.base.servicesimpl.ImageServiceImpl;
import cz.zutrasoft.database.dao.IDirectoryDao;
import cz.zutrasoft.database.dao.ImageDao;
import cz.zutrasoft.database.daoimpl.DirectoryDaoImpl;
import cz.zutrasoft.database.daoimpl.ImageDaoImpl;
import cz.zutrasoft.database.model.Category;
import cz.zutrasoft.database.model.Directory;
import cz.zutrasoft.database.model.Image;

/**
 * Tests methods working with {@code Image} i.e. methods for saving and deleting the Image.
 * Because the upload cannot be tested simply locally, the local image file is saved into DB instead
 * 
 * @author Michal Václavek
 *
 */
public class TestImage
{

	@Parameter
    private static String imageTestDirectoryName = "TestDir";
	@Parameter
    private static String imageTestCategoryName = "TestCategory";
	
	@Parameter
    public static String imageFileNameToSaveToDB = "M:\\Download\\Vydry\\vydra-2.jpg";
	//public String imageFileNameToSaveToDB = "\\your\\local\\path\\image.jpg";

	private static DirectoryService directoryService;
	private static Directory imageDir;
	
	private static CategoryService categoryService;
	private static Category testCategory;
	
	private static ImageService imageService;
	
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		// Create Directory for Image
		//directoryService = new DirectoryServiceImpl();
		directoryService = DirectoryServiceImpl.getInstance();
		//categoryService = new CategoryServiceImpl();
		categoryService = CategoryServiceImpl.getInstance();
		//imageService = new ImageServiceImpl();
		imageService = ImageServiceImpl.getInstance();
		
		testCategory = new Category();       
		testCategory.setName(imageTestCategoryName);  
        
        categoryService.saveCategory(testCategory);  
    	//testCategory = categoryService.getCategoryByName(imageTestCategoryName);
        
        // Vytvoreni a ulozeni noveho adresare, napr. pro fotky ?
        Directory imageDir = new Directory();       
        imageDir.setName(imageTestDirectoryName);                 
        imageDir.setCategory(testCategory);
        
        directoryService.saveDirectory(imageDir);
        
        // Saving Few Images for methods loading all Images
        Path fileLocation = Paths.get(imageFileNameToSaveToDB);
        Image image = new Image(imageDir, fileLocation);   
        imageService.saveImageFile(image);
        
        Image image2 = new Image(imageDir, fileLocation);
        imageService.saveImageFile(image2);
        
	}

	
	@AfterClass
	public static void clearAfter() throws Exception
	{
		// Delete Directory for Image
		directoryService.deleteDirectory(imageDir);
		// Delete Category for Directory
		categoryService.deleteCategory(testCategory);
		
		imageService.getAllImages().forEach(i -> imageService.deleteImageById(i.getId()));
	}
	

	private ImageDao imageDao;
	//private static ImageService imageService;

	@Before
	public void setUp() throws Exception
	{
		imageDao = new ImageDaoImpl();
		imageDir = directoryService.getDirectoryByName(imageTestDirectoryName);
		//imageService = new ImageServiceImpl();				
	}
	
	
	/**
	 * Tests saving and deleting one Image into DB using DAO
	 */
	@Test
    public void test_Save_And_Delete_Image_From_File_DAO()
	{				
	    //Directory dir = directoryDao.getFirstDirectoryByName(knownDirectoryName);  
		// Nacteni obrazku ze souboru a ulozeni do DB
	    // V real aplikaci se pouzije konstruktor new Image(dir, FileUpload uploadedImageFile);
		Path fileLocation = Paths.get(imageFileNameToSaveToDB);
        Image image = new Image(imageDir, fileLocation);
          
        int numberOfImagesBeforeCreate = imageDao.getAllImages().size();
        // CREATE/SAVE Image     
        imageDao.saveImageFile(image);
        assertTrue(imageDao.getAllImages().size() == (numberOfImagesBeforeCreate + 1)); 
        
        // READ Image
        Image i = imageDao.getImageById(image.getId());
        // Kontrola jestli se ulozil uložil
        assertNotNull(i);     

        // DELETE Image
        int numberOfImagesBeforeDelete = imageDao.getAllImages().size();
        imageDao.deleteImageById(i.getId());

        assertTrue(imageDao.getAllImages().size() == (numberOfImagesBeforeDelete - 1));                     
	}
    
	@Test
    public void load_All_Images_From_Directory_Service()
    {
    	//DirectoryService directoryService = new DirectoryServiceImpl();
	    //Directory dir = directoryService.getDirectoryByName(imageDir.getName());  
    	// Load all Images in Directory   
        List<Image> imagesInDir = imageService.getAllImagesInDirectory(imageDir);
        assertNotNull(imagesInDir);
        assertTrue(imagesInDir.size() > 0);   
    }
    
    @Test
    public void load_All_Images_DAO()
    {
    	// Load all Images in Directory   
        List<Image> allImages = imageDao.getAllImages();
        assertNotNull(allImages);
        assertTrue(allImages.size() > 0);   
    }
	
    

}
