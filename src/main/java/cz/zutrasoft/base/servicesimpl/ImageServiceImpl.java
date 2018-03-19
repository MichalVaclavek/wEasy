/**
 * 
 */
package cz.zutrasoft.base.servicesimpl;

import java.util.List;

import cz.zutrasoft.base.services.ImageService;
import cz.zutrasoft.database.dao.ImageDao;
import cz.zutrasoft.database.daoimpl.ImageDaoImpl;
import cz.zutrasoft.database.model.Directory;
import cz.zutrasoft.database.model.Image;

/**
 * @author Michal
 *
 */
public class ImageServiceImpl implements ImageService
{

	private ImageDao imageDao = new ImageDaoImpl();
		
	private static class SingletonHolder
	{
        private static final ImageServiceImpl SINGLE_INSTANCE = new ImageServiceImpl();
    }
	
	/**
	 * @return singleton instance of the ImageServiceImpl
	 */
	public static ImageServiceImpl getInstance()
	{				
		return SingletonHolder.SINGLE_INSTANCE;			
	}
	
	private ImageServiceImpl()
	{}
	
	/* (non-Javadoc)
	 * @see cz.zutrasoft.base.services.ImageService#getAllImages()
	 */
	@Override
	public List<Image> getAllImages()
	{
		return imageDao.getAllImages();
	}


	/* (non-Javadoc)
	 * @see cz.zutrasoft.base.services.ImageService#getAllImagesInDirectory(cz.zutrasoft.database.model.Directory)
	 */
	@Override
	public List<Image> getAllImagesInDirectory(Directory directory)
	{
		return imageDao.getAllImagesInDirectory(directory);
	}


	/* (non-Javadoc)
	 * @see cz.zutrasoft.base.services.ImageService#saveImageFile(cz.zutrasoft.database.model.Image)
	 */
	@Override
	public void saveImageFile(Image uploadedImageFile)
	{
		imageDao.saveImageFile(uploadedImageFile);
	}


	/* (non-Javadoc)
	 * @see cz.zutrasoft.base.services.ImageService#getImageById(int)
	 */
	@Override
	public Image getImageById(int id)
	{
		return imageDao.getImageById(id);
	}


	/* (non-Javadoc)
	 * @see cz.zutrasoft.base.services.ImageService#deleteImageById(int)
	 */
	@Override
	public void deleteImageById(int id)
	{
		imageDao.deleteImageById(id);
	}

}
