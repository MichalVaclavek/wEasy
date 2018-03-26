package cz.zutrasoft.database.model;

import java.io.Serializable;

/**
 * Class to enumerate basic {@link UserProfile} identifiers.
 * 
 * @author Michal Václavek
 */
public enum UserProfileType implements Serializable
{
    USER("USER"),
    DBA("DBA"),
    ADMIN("ADMIN");
     
    String userProfileType;
     
    private UserProfileType(String userProfileType)
    {
        this.userProfileType = userProfileType;
    }
     
    public String getUserProfileType()
    {
        return userProfileType;
    }
     
}
