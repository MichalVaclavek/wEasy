/**
 * 
 */
package cz.zutrasoft.base.exceptions;

/**
 * Vyjimka indikujici pokus o vytvoreni noveho Usera, ktery je jiz
 * vytvoren resp. pokud jiz existuje jiny user se stejnym SSO
 * 
 * @author Michal
 *
 */
public class UserSsoNotUniqueException extends RuntimeException
{
	public UserSsoNotUniqueException(String message)
	{
		super(message);
	}
}
