/**
 * 
 */
package cz.zutrasoft.mainweb.database;

import static org.junit.Assert.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runners.Parameterized.Parameter;

import cz.zutrasoft.base.exceptions.UserSsoNotUniqueException;
import cz.zutrasoft.base.services.IUserService;
import cz.zutrasoft.base.servicesimpl.EncoderDecoderService;
import cz.zutrasoft.base.servicesimpl.UserService;
import cz.zutrasoft.database.model.User;
import cz.zutrasoft.database.model.UserProfile;

/**
 * Testing all methods for handling application users.
 * 
 * @author Michal Václavek
 */
public class TestUser
{

	private static IUserService userService;
	private static EncoderDecoderService ed;
	
	private User uniqueUser;
	private static String uniqueUserName = "uniqueUser";
	
	/**
	 */
	@BeforeClass
	public static void setUpBeforeClass()
	{
		ed = EncoderDecoderService.init("12goro.45.7");	
		userService = UserService.getInstance();
	}
	
	/**
	 */
	@AfterClass
	public static void setUpAfterClass()
	{
		User userToDelete = userService.findByUsername(uniqueUserName);
	    
    	userService.deleteUserByUserId(userToDelete.getId());		
	}
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		//userService = new UserServiceImpl();
		
	}
		
	
	/** ============ TESTOVANI VYTVARENi, UPDATE, LOGIN a DELETE USERA ============ **/
	
    @Parameter
    public String testCreateFirstUserSso = "FirstGoro";

    @Parameter
    public String testPasswd = "Limonada77";
    

    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    
	/**
	 * Otestuje vytvoreni noveho usera, zasifrovani jeho hesla, jeho ulozeni do DB,
	 * zkontroluje jeho opetovne ziskani z DB a jeho smazani. 
	 */
	@Test
	public void test_NewUser_CreateAndSave_Get_Delete()
	{				
	    User newUser = new User();
	    
	    newUser.setFirstName("Goro");
	    newUser.setLastName("Norm8l");
	    newUser.setUsername(testCreateFirstUserSso );
	    newUser.setPassword(testPasswd);
		
	    String emailAddr = "goroadmin@tokio.jp";
	    newUser.setEmail(emailAddr);
	    
	    Set<UserProfile> userProfiles = new HashSet<UserProfile>();
	    userProfiles.add(new UserProfile(1, "USER"));
	    newUser.setUserProfiles(userProfiles);	    
                 
        // Overeni, ze doslo ke zvyseni poctu uzivatelu v DB
        int numberOfUsersBeforeCreate= userService.findAllUsers().size();
        userService.saveUser(newUser);	                 
        // Pocet vsech Useru se zvysil o 1
        assertTrue(userService.findAllUsers().size() == (numberOfUsersBeforeCreate + 1)); 
        
        User loadedUser = userService.findByUsername(testCreateFirstUserSso);
        
        assertNotNull(loadedUser);
        
        assertTrue(loadedUser.getFirstName().equals(newUser.getFirstName()));
        assertTrue(loadedUser.getLastName().equals(newUser.getLastName()));
        assertTrue(loadedUser.getUsername().equals(newUser.getUsername()));
        assertTrue(loadedUser.getEmail().equals(newUser.getEmail()));
                  
        // Smazani usera
        int numberOfUsersBeforeDelete = userService.findAllUsers().size();
        //userService.deleteUserByUsername(testCreateFirstUserSso);
        userService.deleteUserByUserId(loadedUser.getId());      
        
        // Pocet vsech Useru se snizil o 1
        assertTrue(userService.findAllUsers().size() == (numberOfUsersBeforeDelete - 1)); 
	}
	
	/**
	 * Kontroluje ze se pri ukladani noveho usera spravne overi, ze novy user je unikatni
	 * a v pripade ze unikatni neni, ze se vyhodi vyjimka. Podle vzoru ve SpringMVCSecureLogin.
	 * 
	 */
	@Test
	public void validate_If_New_User_Is_Unique()
	{
		uniqueUser = new User();
	    
		uniqueUser.setFirstName("Goro");
		uniqueUser.setLastName("Normal");
		uniqueUser.setUsername(uniqueUserName);
	   
		uniqueUser.setPassword(testPasswd);
		    
		uniqueUser.setEmail("gorouser@tokio.jp");
	    
	    Set<UserProfile> userProfiles = new HashSet<UserProfile>();
	    userProfiles.add(new UserProfile(1, "USER"));
	    uniqueUser.setUserProfiles(userProfiles);

	    userService.saveUser(uniqueUser);
	    
	    // Druhy User se stejnym username
	    User newUser2 = new User();
	    
	    newUser2.setFirstName("Goro");
	    newUser2.setLastName("Normal");
	    newUser2.setUsername(uniqueUserName);
	    
		newUser2.setPassword(testPasswd);
			    
	    newUser2.setEmail("gorouser2@tokio.jp");
	    
	    Set<UserProfile> userProfiles2 = new HashSet<UserProfile>();
	    userProfiles.add(new UserProfile(1, "USER"));
	    newUser2.setUserProfiles(userProfiles2);
	    
		// Overeni, je vyhozena vyjimka, pokud se uklada dalsi user s jiz pouzitym sso tj. username	    
	    exception.expect(UserSsoNotUniqueException.class);
	    userService.saveUser(newUser2);	    		    	    	
	    
	}
	
	@Parameter
    public String testUpdateUserSso = "pavel2";
	
	/**
	 * Overeni, ze lze uspesne v DB updatovat User data vcetne hesla
	 */
	@Test
	public void test_Create_And_Update_UserData()
	{		
		User newUser = new User();
	    
	    newUser.setFirstName("Pavel");
	    newUser.setLastName("V");
	    newUser.setUsername(testUpdateUserSso);
	    
		newUser.setPassword("veverka");
				
	    newUser.setEmail("pavelv@zutrasoft.cz");
	    
	    Set<UserProfile> userProfiles = new HashSet<UserProfile>();
	    userProfiles.add(new UserProfile(1, "USER"));
	    newUser.setUserProfiles(userProfiles);
	        	    
	    // Overeni, ze doslo ke zvyseni poctu uzivatelu v DB
        int numberOfUsersBeforeCreate= userService.findAllUsers().size();
        userService.saveUser(newUser);	                 
        // Pocet vsech Useru se zvysil o 1
        assertTrue(userService.findAllUsers().size() == (numberOfUsersBeforeCreate + 1)); 
				
		// Ziskani drive ulozeneho user z DB
		//User loadedUser = userService.findByUsername(testUpdateUserSso);
		// Update jmena, hesla apod.
		String newFirstName = "Nove jmeno2";
		String newPasswd = "12n33";
		String newEmail = "pavelv@fungisoft.cz";
		
		newUser.setFirstName(newFirstName);		
		newUser.setPassword(newPasswd);
		newUser.setEmail(newEmail);
		
		// Zavolani userService.updateUser();
		userService.updateUser(newUser);
				
		// Znovu nacteni usera a porovnani, ze v DB jsou ulozeny nove hodnoty
		User loadedUserAfterUpdate = userService.findByUsername(testUpdateUserSso);
		
		assertTrue(loadedUserAfterUpdate.getFirstName().equals(newFirstName));
		try
		{			
			assertTrue(ed.decrypt(loadedUserAfterUpdate.getPassword()).equals(newPasswd));
		}
		catch (GeneralSecurityException | IOException  e)
		{
			System.err.println("Chyba pri kodovani hesla." + e.getMessage());
		}
		assertTrue(loadedUserAfterUpdate.getEmail().equals(newEmail));
		
		// Smazani drive vytvoreneho usera, pro opakovani testu nezadouci
	    //userService.deleteUserByUserId(loadedUserAfterUpdate.getId());
		userService.deleteUserByUserId(newUser.getId());
				
	}
	
	@Parameter
	String userAdminNameToValidate = "michalv2";
	@Parameter
	String userAdminRightPassword = "Pes.goro54";
	
	@Parameter
	String userAdminWrongPasswordValidate = "Pes.goro55";
	
	@Test
	public void authenticate_Loging()
	{
		User newUser = new User();
	    
	    newUser.setFirstName("MMM");
	    newUser.setLastName("Silenec");
	    newUser.setUsername(userAdminNameToValidate);
	    newUser.setPassword(userAdminRightPassword);
		
	    String emailAddr = "goroadmin@tokio.jp";
	    newUser.setEmail(emailAddr);
	    
	    Set<UserProfile> userProfiles = new HashSet<UserProfile>();
	    userProfiles.add(new UserProfile(2, "ADMIN"));
	    newUser.setUserProfiles(userProfiles);	    
         
        userService.saveUser(newUser);	
		
        assertTrue(userService.authenticate(userAdminNameToValidate, userAdminRightPassword));
        
        assertFalse(userService.authenticate(userAdminNameToValidate, userAdminWrongPasswordValidate));
        
        // Druha metoda pro validaci ocekava objekt User
		User newUserToValid = new User();
		newUserToValid.setUsername(userAdminNameToValidate);
		
		newUserToValid.setPassword(userAdminRightPassword);
									 		 
		assertTrue(userService.authenticate(newUserToValid));	
		
		newUserToValid.setPassword(userAdminWrongPasswordValidate);
		
		assertFalse(userService.authenticate(newUserToValid));
		
		// Jen kontrola, ze prihlasovany User je ADMIN		
		User logedInAdmin = userService.findByUsername(userAdminNameToValidate);
		
		// Vypis User profiles
		logedInAdmin.getUserProfiles().forEach(prof -> { System.out.println(prof.getType());});
		// Validace, ze jde ADMIN
		logedInAdmin.getUserProfiles().forEach(prof -> { assertTrue(prof.getType().equalsIgnoreCase("ADMIN"));});
		
		userService.deleteUserByUserId(newUser.getId());   
	}
	
	
	@Test
	public void listAllUsers()
	{			
		// získání všech uživatelů z databáze
        List<User> users = userService.findAllUsers();
        
        assertNotNull(users);
        assertTrue(users.size() > 0);	 		
	}
	

}
