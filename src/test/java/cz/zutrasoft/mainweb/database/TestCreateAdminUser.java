/**
 * 
 */
package cz.zutrasoft.mainweb.database;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import cz.zutrasoft.base.services.IUserService;
import cz.zutrasoft.base.servicesimpl.UserService;
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
	private IUserService userService;
	
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
		//userService = new UserServiceImpl();
		userService = UserService.getInstance();
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
