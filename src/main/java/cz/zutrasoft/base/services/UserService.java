package cz.zutrasoft.base.services;

import java.util.List;

import cz.zutrasoft.database.model.User;


//import cz.zutrasoft.springmvc.model.User;
 
 
public interface UserService
{
	public User findById(Integer id);         
    public User findByUsername(String userName);

    public boolean saveUser(User user);
     
    public void updateUser(User user);     
    //public void deleteUserByUsername(String userName);
    public void deleteUserByUserId(Integer userName);
 
    public List<User> findAllUsers(); 
     
    public boolean isUsernameUnique(Integer id, String userName);    
    public boolean authenticate(User userToAuthenticate);
	public boolean authenticate(String userName, String passw);
 
}
