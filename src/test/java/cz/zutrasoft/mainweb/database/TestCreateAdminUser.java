/**
 * 
 */
package cz.zutrasoft.mainweb.database;

import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import cz.zutrasoft.base.EncoderDecoder;
import cz.zutrasoft.base.exceptions.UserSsoNotUniqueException;
import cz.zutrasoft.base.services.UserService;
import cz.zutrasoft.base.servicesimpl.UserServiceImpl;
import cz.zutrasoft.database.dao.IUserDao;
import cz.zutrasoft.database.daoimpl.UserDaoImpl;
import cz.zutrasoft.database.model.User;
import cz.zutrasoft.database.model.UserProfile;

/**
 * Special test for creation ADMIN user
 * 
 * @author Michal Václavek
 *
 */
public class TestCreateAdminUser
{

	//private static EncoderDecoder passwordEncoder;
	//private IUserDao userDao;
	private UserService userService;
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		//passwordEncoder = EncoderDecoder.init("12goro.45.7");		
	}


	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		userService = new UserServiceImpl();
	}

	@Rule
    public ExpectedException exception = ExpectedException.none();

	@Test
	public void testCreateNewAdminUser()
	{		
	    User newUser = new User();
	    
	    newUser.setFirstName("Michal");
	    newUser.setLastName("V");
	    newUser.setUsername("michalv");
	    	    
		newUser.setPassword("Pes.goro54");
				
	    newUser.setEmail("michalv@zutrasoft.cz");
	    
	    Set<UserProfile> userProfiles = new HashSet<UserProfile>();
	    userProfiles.add(new UserProfile(2, "ADMIN"));
	    newUser.setUserProfiles(userProfiles);
	        	            
	    
	    // Overeni, ze doslo ke zvyseni poctu uzivatelu v DB
        int numberOfUsersBeforeCreate= userService.findAllUsers().size();
        userService.saveUser(newUser);	                
        // Pocet vsech Useru se zvysil o 1
        assertTrue(userService.findAllUsers().size() == (numberOfUsersBeforeCreate + 1)); 
	    	
	}
	
	

}
