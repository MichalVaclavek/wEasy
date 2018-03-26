/**
 * 
 */
package cz.zutrasoft.base.exceptions;

/**
 * Exception to indicate an attempt to create a new user with already used/saved username.
 * 
 * @author Michal Václavek
 */
@SuppressWarnings("serial")
public class UserSsoNotUniqueException extends RuntimeException
{
	public UserSsoNotUniqueException(String message)
	{
		super(message);
	}
}
