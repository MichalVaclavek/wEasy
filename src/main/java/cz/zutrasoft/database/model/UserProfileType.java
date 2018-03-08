package cz.zutrasoft.database.model;

import java.io.Serializable;

/**
 * Základní třída pro výčet identifikátorů použitých v {@link UserProfile} <br>
 * Převzato z projektu SpringMVCSecureLogin na stránkách websystique.? <br>
 *
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
