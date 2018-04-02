package cz.zutrasoft.base.services;

import java.util.List;

import cz.zutrasoft.database.model.User;


/**
 * Basic service methods to work with Users.
 * 
 * @author Michal Václavek
 */
public interface IUserService
{
	/**
	 * Gets {@link User} object from DB according it's identity in DB.
	 * 
	 * @param id identity of the User who's instance is required.
	 * @return found {@link User} object instance or null if not found.
	 */
	public User findById(Integer id);  
	
	/**
	 * Gets {@link User} object from DB according it's username column in DB.
	 * 
	 * @param userName - value of username attribute of the {@link User} who's instance is required.
	 * @return found {@link User} object instance or null if not found.
	 */
    public User findByUsername(String userName);

    /**
     * Saves {@link User} object into DB. Username must be unique.
     * 
     * @param user User object to be saved.
     * @return true if user is saved successfuly
     */
    public boolean saveUser(User user);
    
    /**
     * Updates {@link User} in DB.
     *  
     * @param user User object with new data to be updated in DB.
     */
    public void updateUser(User user);  
    
    public List<User> findAllUsers(); 
     
    /**
     * Checks if userName and it's ID is already saved in DB or not.
     * 
     * @param id identity of the {@link User} in DB.
     * @param userName 
     * 
     * @return true if the userName and it's id is not saved in DB, otherwise returns false
     */
    public boolean isUsernameUnique(Integer id, String userName);    
    
    /**
     * Authenticates user credintials of the {@link User} object.
     * 
     * @param userToAuthenticate
     * @return true if username and password of the userToAuthenticate are same as those already saved in DB.
     */
    public boolean authenticate(User userToAuthenticate);
    
    /**
     * Authenticates username and password credintials.
     * 
     * @param userName to be authenticated
     * @param passw password entered with username
     * 
     * @return true if entered username and password are same as those already saved in DB.
     */
	public boolean authenticate(String userName, String passw);
	
	/**
	 * Deletes User of the given identity from DB.
	 * 
	 * @param userID identity of the User who's instance is to be deleted from DB.
	 */
    public void deleteUserByUserId(Integer userID);
 
}
