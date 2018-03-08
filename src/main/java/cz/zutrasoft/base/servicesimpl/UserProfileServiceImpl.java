package cz.zutrasoft.base.servicesimpl;

import java.util.List;

import cz.zutrasoft.base.services.UserProfileService;
import cz.zutrasoft.database.dao.IUserProfileDao;
import cz.zutrasoft.database.daoimpl.UserProfileDaoImpl;
import cz.zutrasoft.database.model.UserProfile;
 
 
//@Service("userProfileService")
//@Transactional
public class UserProfileServiceImpl implements UserProfileService
{     
    //@Autowired
    IUserProfileDao dao = new UserProfileDaoImpl();
     
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