package jp.co.sample.emp_management.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import jp.co.sample.emp_management.form.InsertAdministratorForm;

/**
 * インタフェースを実装して、相関項目チェックルールを実現する.
 * 
 * @author keita.tomooka
 *
 */
@Component // (1)
public class PasswordEqualsValidator implements Validator {

	/**
	 * このValidatorのチェック対象であるかどうかを判別する.
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return InsertAdministratorForm.class.isAssignableFrom(clazz); // (2)
	}

	/**
	 *パスワードと確認用パスワードが一致するか確認する.
	 */
	@Override
	public void validate(Object target, Errors errors) {
		InsertAdministratorForm form = (InsertAdministratorForm) target;
		String password = form.getPassword();
		String confirmPassword = form.getConfirmPassword();

		if (password == null || confirmPassword == null) { // (3)
			return;
		}
		if (!password.equals(confirmPassword)) { // (4)
			errors.rejectValue(/* (5) */ "password", 
					/* (6) */ "PasswordEqualsValidator.passwordResetForm.password",
					/* (7) */ "password and confirm password must be same.");
		}
	}
}