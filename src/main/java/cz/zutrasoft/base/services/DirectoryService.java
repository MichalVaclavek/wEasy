/**
 * 
 */
package cz.zutrasoft.base.services;

import java.util.List;

import cz.zutrasoft.database.model.Category;
import cz.zutrasoft.database.model.Directory;

/**
 * @author Michal Václavek
 *
 */
public interface DirectoryService
{
	public Directory getDirectoryByName(String name);
	public Directory getOneDirectoryFromCategory(String directoryName, Category category);
	
	public List<Directory> getAllDirectories();
	public List<Directory> getAllDirectoriesForCategory(Category category);
		
	public void deleteDirectory(Directory directoryDel);
	public void deleteDirectory(String directoryName, Category category);
	
	public void saveDirectory(Directory directory);
	
	public void saveDirectory(String directoryName, String categoryName);
	//public void saveDirectory(String directoryName, Long categoryId);
	public void saveDirectory(String directoryName, Category category);
	
}
