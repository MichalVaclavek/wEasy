/**
 * 
 */
package cz.zutrasoft.external.pages.registerpage;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

import cz.zutrasoft.base.services.IUserService;
import cz.zutrasoft.base.servicesimpl.UserService;

/**
 * Validates choosen user name for duplicity using {@link UserService} i.e. within DB of users.
 * 
 * @author Michal Václavek
 *
 */
public class UserNameValidator implements IValidator<String>
{
	private static final long serialVersionUID = 4161003381024833740L;

	@Override
	public void validate(IValidatable<String> validatable)
	{
		IUserService userService = UserService.getInstance();
		String chosenUserName = validatable.getValue();

		if (chosenUserName.isEmpty() || !userService.isUsernameUnique(null, chosenUserName))
		{
			ValidationError error = new ValidationError(this);
			
			error.addKey("registerForm.username.Unique");
			validatable.error(error);
		}
	}
				
}
