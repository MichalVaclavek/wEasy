package cz.zutrasoft.base.services;

import java.util.List;

import cz.zutrasoft.database.model.Directory;
import cz.zutrasoft.database.model.Image;

public interface ImageService
{
	public List<Image> getAllImages();
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
