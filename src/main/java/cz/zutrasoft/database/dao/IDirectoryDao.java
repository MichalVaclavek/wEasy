package cz.zutrasoft.database.dao;

import java.util.List;

import cz.zutrasoft.database.model.Category;
import cz.zutrasoft.database.model.Directory;

public interface IDirectoryDao
{	
	public Directory getFirstDirectoryByName(String name);
	public Directory getDirectoryFromCategory(String directoryName, Category category);
	
	public List<Directory> getAllDirectories();
	public List<Directory> getAllDirectoriesForCategory(long categoryId);
	public List<Directory> getAllDirectoriesForCategory(Category category);
	
	//public Directory getDirectory(String directoryName);
	//void saveDirectory(String directoryName);
	public void saveDirectory(Directory directory);
	
	public void delete(Directory directoryDel);
	//public void delete(Directory directoryDel, Category category);
	public void delete(String directoryName, Category category);

}
