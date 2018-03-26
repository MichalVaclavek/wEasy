package cz.zutrasoft.base.services;

import java.util.List;

import cz.zutrasoft.database.model.Directory;
import cz.zutrasoft.database.model.Image;

/**
 * Basic service methods to work with Images.
 * 
 * @author Michal Václavek
 */
public interface IImageService
{
	public List<Image> getAllImages();
	/**
	 * Get list of Images belonging to specified {@link Directory}
	 * @param directory {@code Directory} object the returned Images belongs to
	 * @return list of Images belonging to specified {@link Directory}
	 */
	public List<Image> getAllImagesInDirectory(Directory directory);
	
	/**
	 * Saves {@link Image}
	 * 
	 * @param uploadedImageFile - image to be saved
	 */
	public void saveImageFile(Image uploadedImageFile);
	
	/**
	 * Gets {@link Image}.
	 * 
	 * @param id - id of the image
	 * @return image
	 */
	public Image getImageById(int id);
	
	/**
	 * Deletes {@link Image}.
	 * 
	 * @param id - id of the image
	 */
	public void deleteImageById(int id);
	
}
