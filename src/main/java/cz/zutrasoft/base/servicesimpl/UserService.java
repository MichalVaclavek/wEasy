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

//@Transactional // Spring annotation if Spring is used
public class UserService implements IUserService
{
	static final Logger logger = LoggerFactory.getLogger(UserService.class);
	
	private IUserDao userDao = new UserDaoImpl();
 
    private static EncoderDecoderService passwordEncoder = EncoderDecoderService.init("12goro.45.7");
    
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
     
	
    @Override
    public User findById(Integer id)
    {
        return userDao.findById(id);
    }
 
    @Override
    public User findByUsername(String sso)
    {
        User user = userDao.findByUsername(sso);
        return user;
    }
 
    /**
     * Saves new User in DB. It is checked if the username is not already used for another user in DB.<br>
     * Only basic USER role can be assigned.<br>
     * ADMIN role can be assigned only using respective test method {@link TestCreateAdminUser} during develepment
     * or directly within DB administration. 
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
	 * Updates User data in DB
     */
    @Override
    public void updateUser(User user)
    {
    	User entity = findById(user.getId());
        
        if (entity != null)
        {
            entity.setUsername(user.getUsername());
            
            if (!user.getPassword().equals(entity.getPassword()))
            {
                try
				{
					entity.setPassword(passwordEncoder.encrypt(user.getPassword()));
					
					entity.setFirstName(user.getFirstName());
		            entity.setLastName(user.getLastName());
		            entity.setEmail(user.getEmail());
		            entity.setUserProfiles(user.getUserProfiles());
		            
		            userDao.update(entity);
				} catch (UnsupportedEncodingException | GeneralSecurityException e)
				{					
					logger.error("Error updating user. Exception: " + e.getMessage());
				}
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
     */
    @Override
    public boolean authenticate(String userName, String passw)
    {
    	boolean authenticated = false;
    	
    	if (!userName.isEmpty() && !passw.isEmpty())
    	{
	    	User entity = userDao.findByUsername(userName);
	        
	        if (entity != null)
	        {
	           try
	           {
					if (passw.equals(passwordEncoder.decrypt(entity.getPassword())))
						authenticated = true;
				} catch (GeneralSecurityException | IOException e)
				{
					logger.error("Error decrypting password. Exception: " + e.getMessage());
				}           
	        }
    	}
    	
        return authenticated;
    }

    
}
