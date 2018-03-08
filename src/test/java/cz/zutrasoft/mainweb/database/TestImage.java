package cz.zutrasoft.mainweb.database;

import static org.junit.Assert.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runners.Parameterized.Parameter;

import cz.zutrasoft.base.services.DirectoryService;
import cz.zutrasoft.base.services.ImageService;
import cz.zutrasoft.base.servicesimpl.DirectoryServiceImpl;
import cz.zutrasoft.base.servicesimpl.ImageServiceImpl;
import cz.zutrasoft.database.dao.IDirectoryDao;
import cz.zutrasoft.database.dao.ImageDao;
import cz.zutrasoft.database.daoimpl.DirectoryDaoImpl;
import cz.zutrasoft.database.daoimpl.ImageDaoImpl;
import cz.zutrasoft.database.model.Directory;
import cz.zutrasoft.database.model.Image;

/**
 * Otestuje vsechny metody dulezite pro praci s {@code Image} tj. jak metody v DAO vrstve {@code cz.zutrasoft.database.daoimpl},
 * tak i metody v Service vrsve {@code cz.zutrasoft.base.servicesimpl}
 * 
 * @author Michal Václavek
 *
 */
public class TestImage
{

	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{}

	private ImageDao imageDao;
	private ImageService imageService;

	@Before
	public void setUp() throws Exception
	{
		imageDao = new ImageDaoImpl();
		imageService = new ImageServiceImpl();
	}

	@Parameter
    public String knownDirectoryName = "Pejsci";
	
	@Parameter
    public String imageFileNameToSaveToDB = "M:\\Download\\Vydry\\vydra-2.jpg";
	
	@Test
    public void testSaveImageFromFileDao()
	{		
		// Nacteni vsech images v Direcoty
        IDirectoryDao directoryDao = new DirectoryDaoImpl();
	    Directory dir = directoryDao.getFirstDirectoryByName(knownDirectoryName);  
		// Nacteni obrazku ze souboru a ulozeni do DB
	    // V real aplikaci se pouzije konstruktor new Image(dir, FileUpload uploadedImageFile);
		Path fileLocation = Paths.get(imageFileNameToSaveToDB);
        Image image = new Image(dir, fileLocation);
                              
        // uložení obrázku resp.  do DB      
        imageDao.saveImageFile(image);
                       
        Image i = imageDao.getImageById(image.getId());
        // Kontrola jestli se ulozil uložil
        assertNotNull(i);        
         
        // Nacteni vsech images v Directory    
        List<Image> imagesDir = imageDao.getAllImagesInDirectory(dir);
        assertNotNull(imagesDir);
        assertTrue(imagesDir.size() > 0);           
                   
	}
    
	@Test
    public void loadAllImagesFromDirectoryService()
    {
    	DirectoryService directoryService = new DirectoryServiceImpl();
	    Directory dir = directoryService.getDirectoryByName(knownDirectoryName);  
    	// Nacteni vsech images v Directory    
        List<Image> imagesDir = imageService.getAllImagesInDirectory(dir);
        assertNotNull(imagesDir);
        assertTrue(imagesDir.size() > 0);   
    }
    
    @Test
    public void loadAllImagesDAO()
    {
    	// Nacteni vsech images
        List<Image> allImages = imageDao.getAllImages();
        assertNotNull(allImages);
        assertTrue(allImages.size() > 0);   
    }
	
    @Test
    public void testDeleteImageDAO()
	{
        // Smazani 1 obrazku          
        Image image2 = imageDao.getImageById(1);
        
        int numberOfImagesBeforeDelete = imageDao.getAllImages().size();
        imageDao.deleteImageById(image2.getId());

        assertTrue(imageDao.getAllImages().size() == (numberOfImagesBeforeDelete - 1));  
        
	}
    
    @Test
    public void testDeleteImageService()
	{
    	// Smazani 1 obrazku          
        Image image3 = imageService.getImageById(2);
        
        int numberOfImagesBeforeDelete = imageService.getAllImages().size();
        imageService.deleteImageById(image3.getId());

        assertTrue(imageService.getAllImages().size() == (numberOfImagesBeforeDelete - 1));  
	}

}
