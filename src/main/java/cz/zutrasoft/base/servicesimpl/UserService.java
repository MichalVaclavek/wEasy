package cz.zutrasoft.base.servicesimpl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.zutrasoft.base.exceptions.UserSsoNotUniqueException;
import cz.zutrasoft.base.services.IUserService;
import cz.zutrasoft.database.dao.IUserDao;
import cz.zutrasoft.database.daoimpl.UserDaoImpl;
import cz.zutrasoft.database.model.User;
import cz.zutrasoft.database.model.UserProfile;

/**
 * Implementation of {@link IUserService} interface.
 * Contains actions needed to handle users (instancies of the {@link User} ) of the web pages app.
 * 
 * @author Michal Václavek
 *
 */
public class UserService implements IUserService
{
	static final Logger logger = LoggerFactory.getLogger(UserService.class);
	
	private IUserDao userDao = new UserDaoImpl();
 
	/**
	 * Default encoding/decoding password. The source code should be obfuscated, if used like that.
	 */
    private static EncoderDecoderService passwordEncoder = EncoderDecoderService.init("your_password_for_encrypt/decrypt");
    
    	private static class SingletonHolder
    	{
    		private static final UserService SINGLE_INSTANCE = new UserService();
    	}
	
	/**
	 * @return singleton instance of the UserService
	 */
	public static UserService getInstance()
	{				
		return SingletonHolder.SINGLE_INSTANCE;			
	}
	
	private UserService()
	{}
     
	/**
	 * Finds and gets User instance from DB with password decoded.
	 * 
	 * @param id ID of the user to be found
	 * @return {@link User} instance according it's ID
	 */
    @Override
    public User findById(Integer id)
    {
        User user =  userDao.findById(id);
        try
		{
        	if (user != null)
        		user.setPassword(passwordEncoder.decrypt(user.getPassword()));
		}
		catch (GeneralSecurityException | IOException e)
		{
			logger.error("Error getting user. Exception: " + e.getMessage());
		}
    	return user;
    }
 
    @Override
    public User findByUsername(String sso)
    {
    	User user =  userDao.findByUsername(sso);
        try
		{
			if (user != null)
				user.setPassword(passwordEncoder.decrypt(user.getPassword()));
		}
		catch (GeneralSecurityException | IOException e)
		{
			logger.error("Error getting user. Exception: " + e.getMessage());
		}
    	return user;
    }
 
    /**
     * Saves new User in DB. It is checked, if the username is not already used for another user in DB.<br>
     * Only basic USER role can be assigned.<br>
     * ADMIN role can be assigned only using respective test method {@link TestCreateAdminUser} during develepment
     * or directly within DB administration.
     * 
     * It allows empty password to be saved. Is it fine?
     * 
     * @param a User instance to be saved. It's password is encrypted before saving.
     * @return true if saved successfuly, false otherwise.
     */
    @Override
    public boolean saveUser(User user)
    {
        if (!isUsernameUnique(user.getId(), user.getUsername()))
        	throw new UserSsoNotUniqueException("User name \"" + user.getUsername() + "\" not unique. New user cannot be created!");
    	
    	try
		{        	        	
        	user.setPassword(passwordEncoder.encrypt(user.getPassword()));
        	
        	if ((user.getUserProfiles() == null) || (user.getUserProfiles().size() == 0))
        	{
        		Set<UserProfile> userProfiles = new HashSet<UserProfile>();
        		// Only basic USER role can be assigned to commom user 
        		userProfiles.add(new UserProfile(1, "USER"));
        		user.setUserProfiles(userProfiles);
        	}
        	        		      	
			userDao.save(user);
			return true;
		} catch (UnsupportedEncodingException | GeneralSecurityException e)
		{
			logger.error("Error saving user. Exception: " + e.getMessage());
		}
        
		return false;
        
    }
 
    /**
	 * Updates User data in DB. If a user's password attribute is empty, it means no change of password is required.
	 *  
	 * @param user - a User instance with changed data to be saved. It's password is encrypted before save.
     */
    @Override
    public void updateUser(User user)
    {
    	User entity = findById(user.getId());
        
        if (entity != null)
        {
            entity.setUsername(user.getUsername());
                      
            try
			{            	
            	if (!user.getPassword().isEmpty()) // New password is not empty
            		entity.setPassword(passwordEncoder.encrypt(user.getPassword())); // can be set as a new password
				
				entity.setFirstName(user.getFirstName());
	            entity.setLastName(user.getLastName());
	            entity.setEmail(user.getEmail());
	            entity.setUserProfiles(user.getUserProfiles());
	            
	            userDao.update(entity);
			} catch (GeneralSecurityException | IOException e)
			{					
				logger.error("Error updating user. Exception: " + e.getMessage());
			}                       
        }
    }
 
    @Override 
    public void deleteUserByUserId(Integer userId)
    {
        userDao.deleteByUserId(userId);
    }
 
    public List<User> findAllUsers()
    {
        return userDao.findAllUsers();
    }
 
    @Override
    public boolean isUsernameUnique(Integer id, String sso)
    {
        User user = findByUsername(sso);
        return ( user == null || ((id != null) && (user.getId() == id)));
    }
    
    /**
     * Authenticates User. Password is not encrypted yet.
     * 
     * @param userToAuthenticate - user object with password to be authenticated
     */
    @Override
    public boolean authenticate(User userToAuthenticate)
    {
    	boolean authenticated = false;
    	
    	if (userToAuthenticate != null)
    		if (!userToAuthenticate.getUsername().isEmpty()
    				&& !userToAuthenticate.getPassword().isEmpty())
    		{
    			authenticated = authenticate(userToAuthenticate.getUsername(), userToAuthenticate.getPassword());
    		}
    	   	
        return authenticated;
    }
    
    /**
     * Authenticates username and password. Password is not encrypted yet.
     * 
     * @param userName - user name to be authenticated
     * @param passws - password belonging to the userName to be checked
     */
    @Override
    public boolean authenticate(String userName, String passw)
    {
    	boolean authenticated = false;
    	
    	if (!userName.isEmpty() && !passw.isEmpty())
    	{
    		User entity = findByUsername(userName);
	        if (entity != null)
	        {
	        	if (passw.equals(entity.getPassword()))
					authenticated = true;				         
	        }
    	}
    	
        return authenticated;
    }

    
}
