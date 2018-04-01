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
	private IUserService userService;
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{	
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		userService = UserService.getInstance();
	}

	@Rule
    public ExpectedException exception = ExpectedException.none();

	@Test
	public void testCreateNewAdminUser()
	{		
	    User newUser = new User();
	    
	    newUser.setFirstName("Michal");
	    newUser.setLastName("Vc");
	    newUser.setUsername("michalv");	    	    
		newUser.setPassword("Pes.goro54");				
	    newUser.setEmail("michalv@zutrasoft.cz");
	    
	    Set<UserProfile> userProfiles = new HashSet<UserProfile>();
	    userProfiles.add(new UserProfile(2, "ADMIN"));
	    newUser.setUserProfiles(userProfiles);
	        	            	    
        int numberOfUsersBeforeCreate= userService.findAllUsers().size();
        userService.saveUser(newUser);	                
        // Number of users in DB increased by 1
        assertTrue(userService.findAllUsers().size() == (numberOfUsersBeforeCreate + 1)); 	    	
	}
	
	
}
