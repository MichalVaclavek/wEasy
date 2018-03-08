package cz.zutrasoft.base;

import static org.junit.Assert.assertTrue;

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.request.Request;

import cz.zutrasoft.base.services.UserService;
import cz.zutrasoft.base.servicesimpl.UserServiceImpl;
import cz.zutrasoft.database.daoimpl.UserDaoImpl;
import cz.zutrasoft.database.model.User;
import cz.zutrasoft.database.model.UserProfile;
import cz.zutrasoft.database.model.UserProfileType;

/**
 * Authentication session for the application.
 * 
 * @author zutrasoft
 */
public class BasicAutorizationAndAuthenticationSession extends AuthenticatedWebSession
{

	private static final long serialVersionUID = -2815836672309139503L;
	
	private String email;
	private Integer userId;
	private String username;
	
	private User logedInUser;

	public BasicAutorizationAndAuthenticationSession(Request request)
	{
		super(request);
	}

	
	@Override
    public boolean authenticate(String username, String password)
    {
    	// Nacteni usera z DB, decryptovani hesla a porovnani s vlozenymi hodnotami
    	boolean retV = false;  	  	
    	UserService userService = new UserServiceImpl();
    	
    	if (userService.authenticate(username, password))
    	{
    		logedInUser = userService.findByUsername(username);
    		
    		this.userId = logedInUser.getId();
			this.username = logedInUser.getUsername();
			this.email = logedInUser.getEmail();
			
			retV = true;    		
    	}
    	
    	return retV;	
		 
    }

	/**
	 * Kontrola a pridani roli uzivateli
	 */
    @Override
    public Roles getRoles()
    {
    	Roles roles = new Roles();
    	
        if (isSignedIn())
        {
            roles.add(Roles.USER);
            //TODO Dodělání načítání a zjišťování Rolí resp. Profilů asi k aktuálnímu uživateli
           // if (email.equals("admin@zutrasoft.cz"))
            for(UserProfile userProfile : logedInUser.getUserProfiles())
    		{
    			if (userProfile.getType().equalsIgnoreCase("ADMIN"))
    			{
    				roles.add(Roles.ADMIN);
    				//break;
    			}
            }
                                 
        }
        
        return roles;
    }
	
	    
	public Integer getUserId()
	{
		return this.userId;
	}
	public String getUsername()
	{
		return this.username;
	}
	public String getEmail()
	{

		return this.email;
	}
	
}
