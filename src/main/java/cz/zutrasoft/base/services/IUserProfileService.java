package cz.zutrasoft.base.services;

import java.util.List;

import cz.zutrasoft.database.model.UserProfile;


/**
 * Basic service methods to work with {@link UserProfile} .
 * 
 * @author Michal Václavek
 */ 
public interface IUserProfileService
{ 
    public UserProfile findById(int id);
    public UserProfile findByType(String type);
    public List<UserProfile> findAll();     
}
