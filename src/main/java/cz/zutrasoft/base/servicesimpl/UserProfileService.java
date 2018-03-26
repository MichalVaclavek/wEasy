package cz.zutrasoft.base.servicesimpl;

import java.util.List;

import cz.zutrasoft.base.services.IUserProfileService;
import cz.zutrasoft.database.dao.IUserProfileDao;
import cz.zutrasoft.database.daoimpl.UserProfileDaoImpl;
import cz.zutrasoft.database.model.UserProfile;
 
 
//@Service("userProfileService") // Spring annotation
//@Transactional // Spring annotation
public class UserProfileService implements IUserProfileService
{     
    //@Autowired // Spring annotation
    IUserProfileDao dao = new UserProfileDaoImpl();
    
    private static class SingletonHolder
	{
        private static final UserProfileService SINGLE_INSTANCE = new UserProfileService();
    }
	
	/**
	 * @return singleton instance of the UserProfileService
	 */
	public static UserProfileService getInstance()
	{				
		return SingletonHolder.SINGLE_INSTANCE;			
	}
	
	private UserProfileService()
	{}
     
    public UserProfile findById(int id)
    {
        return dao.findById(id);
    }
 
    public UserProfile findByType(String type)
    {
        return dao.findByType(type);
    }
 
    public List<UserProfile> findAll()
    {
        return dao.findAll();
    }
 
    
}