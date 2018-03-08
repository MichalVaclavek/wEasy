package cz.zutrasoft.database.dao;

import java.util.List;

import cz.zutrasoft.database.model.UserProfile;


public interface IUserProfileDao
{
    public List<UserProfile> findAll();    
    public UserProfile findByType(String type);     
    public UserProfile findById(int id);
}
