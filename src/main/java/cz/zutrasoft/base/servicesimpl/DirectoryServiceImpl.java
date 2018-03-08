/**
 * 
 */
package cz.zutrasoft.base.servicesimpl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.zutrasoft.base.services.DirectoryService;
import cz.zutrasoft.database.dao.ICategoryDao;
import cz.zutrasoft.database.dao.IDirectoryDao;
import cz.zutrasoft.database.daoimpl.CategoryDaoImpl;
import cz.zutrasoft.database.daoimpl.DirectoryDaoImpl;
import cz.zutrasoft.database.model.Category;
import cz.zutrasoft.database.model.Directory;

/**
 * @author Michal Václavek
 *
 */
public class DirectoryServiceImpl implements DirectoryService
{
	static final Logger logger = LoggerFactory.getLogger(DirectoryServiceImpl.class);
	
	private IDirectoryDao directoryDao = new DirectoryDaoImpl();
	
	private ICategoryDao categoryDao = new CategoryDaoImpl();

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
		
        // Vytvoreni a ulozeni noveho adresare pro fotky
        Directory directory = new Directory();       
        directory.setName(directoryName);                 
        directory.setCategory(category);
        
        directoryDao.saveDirectory(directory);
	}
	
	//@Override
	/*
	private void saveDirectory(String directoryName, Long categoryId)
	{
		Category category = null;
		if (categoryId != null)
			category = categoryDao.getCategoryById(categoryId);
		
        // Vytvoreni a ulozeni noveho adresare pro fotky 
        Directory directory = new Directory();       
        directory.setName(directoryName);                 
        directory.setCategory(category);
        
        directoryDao.saveDirectory(directory);
	}
	*/
	

	@Override
	public void saveDirectory(String directoryName, Category category)
	{
		//saveDirectory(directoryName, category.getId());
		// Vytvoreni a ulozeni noveho adresare pro fotky 
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
