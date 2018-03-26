package cz.zutrasoft.base;

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.request.Request;

import cz.zutrasoft.base.services.IUserService;
import cz.zutrasoft.base.servicesimpl.UserService;
import cz.zutrasoft.database.model.User;
import cz.zutrasoft.database.model.UserProfile;

/**
 * Authentication session for the application.
 * 
 * @author vitfo
 * @author Michal Václavek
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

	/**
	 * Authenticate user using username and password using {@link UserService}
	 */
	@Override
    public boolean authenticate(String username, String password)
    {
    	boolean retV = false;  	  	

    	IUserService userService = UserService.getInstance();
    	
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
	 * Get Wicket roles of the logged-in user
	 */
    @Override
    public Roles getRoles()
    {
    	Roles roles = new Roles();
    	
        if (isSignedIn())
        {
            roles.add(Roles.USER);

            for (UserProfile userProfile : logedInUser.getUserProfiles())
    		{
    			if (userProfile.getType().equalsIgnoreCase("ADMIN"))
    			{
    				roles.add(Roles.ADMIN);   				
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
