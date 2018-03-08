package cz.zutrasoft.base.servicesimpl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.zutrasoft.base.EncoderDecoder;
import cz.zutrasoft.base.exceptions.UserSsoNotUniqueException;
import cz.zutrasoft.base.services.UserService;
import cz.zutrasoft.database.dao.IUserDao;
import cz.zutrasoft.database.daoimpl.CategoryDaoImpl;
import cz.zutrasoft.database.daoimpl.UserDaoImpl;
import cz.zutrasoft.database.model.User;
import cz.zutrasoft.database.model.UserProfile;


//@Transactional // Jde o anotaci Springu
public class UserServiceImpl implements UserService
{
	static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
	private IUserDao userDao = new UserDaoImpl();
 
    //private EncoderDecoder passwordEncoder = EncoderDecoder.init();
    private static EncoderDecoder passwordEncoder = EncoderDecoder.init("12goro.45.7");
     
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
     * Komentář z původního zdroje, pokud je operace prováděna ve Springu: <br>
     * 
     * Since the method is running with Transaction, No need to call hibernate update explicitly.
     * Just fetch the entity from db and update it with proper values within transaction.
     * It will be updated in db once transaction ends.
     * V73e uveden0 tedy platí pouze pro Spring, v této aplikaci je třeba volat update extra
     * 
     */
    @Override
    public void updateUser(User user)
    {
        //User entity = userDao.findById(user.getId());
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
    //public void deleteUserByUsername(String userName)
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
    
    /*
    @Override
    public boolean isUsernameUnique(Integer id, String sso)
    {
        User user = findByUsername(sso);
        return ( user == null || ((id != null) && (user.getId() == id)));
    }
*/
    
    /**
     * Authentikace noveho Usera. Jeho heslo se predpoklada jeste nezasifrovane ...
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
     * Authentikace username a password
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
