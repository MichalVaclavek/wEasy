package cz.zutrasoft.base.services;

import java.util.List;

import cz.zutrasoft.database.model.UserProfile;



 
public interface UserProfileService
{ 
    public UserProfile findById(int id);
    public UserProfile findByType(String type);
    public List<UserProfile> findAll();     
}
