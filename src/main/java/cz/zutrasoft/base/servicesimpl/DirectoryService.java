/**
 * 
 */
package cz.zutrasoft.base.servicesimpl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.zutrasoft.base.services.IDirectoryService;
import cz.zutrasoft.database.dao.ICategoryDao;
import cz.zutrasoft.database.dao.IDirectoryDao;
import cz.zutrasoft.database.daoimpl.CategoryDaoImpl;
import cz.zutrasoft.database.daoimpl.DirectoryDaoImpl;
import cz.zutrasoft.database.model.Category;
import cz.zutrasoft.database.model.Directory;

/**
 * Singleton implementation of the I_DirectoryService interface.
 * 
 * @author Michal Václavek
 */
public class DirectoryService implements IDirectoryService
{
	static final Logger logger = LoggerFactory.getLogger(DirectoryService.class);
	
	private IDirectoryDao directoryDao = new DirectoryDaoImpl();	
	// To get Categories functions
	private ICategoryDao categoryDao = new CategoryDaoImpl();
	
	
		private static class SingletonHolder
		{
			private static final DirectoryService SINGLE_INSTANCE = new DirectoryService();
		}
	
	/**
	* @return singleton instance of the {@code DirectoryService}
	*/
	public static DirectoryService getInstance()
	{				
		return SingletonHolder.SINGLE_INSTANCE;			
	}
	
	private DirectoryService()
	{}

	/* (non-Javadoc)
	 * @see cz.zutrasoft.base.services.DirectoryService#getDirectoryByName(java.lang.String)
	 */
	@Override
	public Directory getDirectoryByName(String name)
	{
		return directoryDao.getFirstDirectoryByName(name);
	}


	/* (non-Javadoc)
	 * @see cz.zutrasoft.base.services.DirectoryService#getAllDirectories()
	 */
	@Override
	public List<Directory> getAllDirectories()
	{
		return directoryDao.getAllDirectories();
	}


	/* (non-Javadoc)
	 * @see cz.zutrasoft.base.services.DirectoryService#getAllDirectoriesForCategory(cz.zutrasoft.database.model.Category)
	 */
	@Override
	public List<Directory> getAllDirectoriesForCategory(Category category)
	{
		return directoryDao.getAllDirectoriesForCategory(category);
	}

	/**
	 * Pripad, kdy je Direcory kompletni, tj. vcetne Category
	 * @param directory
	 */
	@Override
	public void saveDirectory(Directory directory)
	{      
        if (directory.getCategory() != null)
        	directoryDao.saveDirectory(directory);
        else
        	logger.error("Directory cannot be saved, missing related Category");
	}
	
	/* (non-Javadoc)
	 * @see cz.zutrasoft.base.services.DirectoryService#saveDirectory(java.lang.String)
	 */	
	@Override
	public void saveDirectory(String directoryName, String categoryName)
	{
		Category category = categoryDao.getCategoryByName(categoryName);		
		saveDirectory(directoryName, category);		
	}
	
	@Override
	public void saveDirectory(String directoryName, Category category)
	{
		// Creates and save new Directory
        Directory directory = new Directory();       
        directory.setName(directoryName);                 
        directory.setCategory(category);
        
        directoryDao.saveDirectory(directory);
	}


	/* (non-Javadoc)
	 * @see cz.zutrasoft.base.services.DirectoryService#deleteById(cz.zutrasoft.database.model.Directory)
	 */
	@Override
	public void deleteDirectory(Directory directoryDel)
	{
		directoryDao.delete(directoryDel);
	}


	@Override
	public Directory getOneDirectoryFromCategory(String directoryName, Category category)
	{
		return directoryDao.getDirectoryFromCategory(directoryName, category);
	}


	@Override
	public void deleteDirectory(String directoryName, Category category)
	{
		directoryDao.delete(directoryName, category);		
	}


}
