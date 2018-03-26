/**
 * 
 */
package cz.zutrasoft.base.services;

import java.util.List;

import cz.zutrasoft.database.model.Category;
import cz.zutrasoft.database.model.Directory;

/**
 * Basic service methods to work with Directories (of Images)
 * (Every {@link Directory} must be assigned to one {@link Category} )
 * 
 * @author Michal Václavek
 */
public interface IDirectoryService
{
	/**
	 * Saves {@link Directory} object into DB.
	 * 
	 * @param directory to be saved. Must have Category assigned.
	 */
	public void saveDirectory(Directory directory);
	
	/**
	 * Saves Directory name under respective Category. Directory name must be unique within given Category.
	 * 
	 * @param directoryName name of the {@link Directory}
	 * @param categoryName name of the {@link Category} which the {@link Directory} belongs to
	 */
	public void saveDirectory(String directoryName, String categoryName);
	public void saveDirectory(String directoryName, Category category);
	
	/**
	 * Gets {@link Directory} object of the given name. If it is not unique, the first found Directory of
	 * this name is returned.
	 * 
	 * @param name of the Directory who's instance is required.
	 * @return {@link Directory} object of the given name
	 */
	public Directory getDirectoryByName(String name);
	
	/**
	 * Gets {@link Directory} object of the given name in the given Category.
	 * 
	 * @param directoryName name of the Directory who's instance is required.
	 * @param category {@link Category} instance of the required Directory
	 * @return {@link Directory} object of the given name in the given Category
	 */
	public Directory getOneDirectoryFromCategory(String directoryName, Category category);
	
	/**
	 * Gets list of all Directories within given {@link Category} instance.
	 * 
	 * @param category instance of the {@link Category} for which Directories are required.
	 * @return list of Directories within given {@link Category} instance
	 */
	public List<Directory> getAllDirectoriesForCategory(Category category);
		
	public List<Directory> getAllDirectories();
	
	/**
	 * Deletes given {@link Directory} from repository/DB.
	 * 
	 * @param directoryDel {@link Directory} instance to be deleted from repository.
	 */
	public void deleteDirectory(Directory directoryDel);
	public void deleteDirectory(String directoryName, Category category);
			
}
