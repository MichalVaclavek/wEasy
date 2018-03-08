package cz.zutrasoft.database.dao;

import java.util.List;

import cz.zutrasoft.database.model.User;

public interface IUserDao 
{	
	public User findById(Integer id);    
	public User findByUsername(String userName);     
	public void save(User user);  
	public void update(User user); 
	
	public void deleteByUserId(Integer userId);     
	public List<User> findAllUsers();
}
