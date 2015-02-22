package com.eaw1805.www.controllers.validators;

import com.eaw1805.data.managers.beans.UserManagerBean;
import com.eaw1805.data.model.User;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Validate update form on settings.
 */
public class SettingsValidator implements Validator {

    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private static final Pattern patternEmail = Pattern.compile(EMAIL_PATTERN);

    public boolean supports(Class aClass) {
        return aClass.equals(User.class);
    }

    public void validate(Object command, Errors errors) {
        final User user = (User) command;

        /* Checking if a field is empty or has a white space and reject it.*/
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "fullname", "required.fullname");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "required.username");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "required.password");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "location", "required.location");

        validateEmail(user.getEmail(), errors);
        if (errors.hasErrors()) {
            return;
        }


        // Check if user with duplicate email exists
        final List<User> thatUser = userManager.searchByEmail(user.getEmail());

        if ((!thatUser.isEmpty()) && (thatUser.get(0).getUserId() != user.getUserId())) {
            errors.rejectValue("email", "require.uniqueEmail");
        }

        // Check if user with duplicate name exists
        final User thatUser2 = userManager.getByUserName(user.getUsername());
        if (thatUser2 != null && thatUser2.getUserId() != user.getUserId()) {
            errors.rejectValue("username", "require.uniqueUserName");
        }

        // validate e-mail
        validateEmail(user.getEmail(), errors);

        if (errors.hasErrors()) {
            return;
        }

        // Check if user with duplicate email exists
        final List<User> userEmailList = userManager.searchByEmail(user.getEmail());

        if ((!userEmailList.isEmpty()) && (userEmailList.get(0).getUserId() != user.getUserId())) {
            errors.rejectValue("email", "required.uniqueEmail");
        }
    }

    private void validateSpecial(final Errors errors, final String value, final String field) {
        if (value.contains("'")
                || value.contains("\"")
                || value.contains("\'")
                || value.contains("\\")
                || value.contains("/")) {
            errors.rejectValue(field, "required.field_problem");
        }
    }

    /**
     * validate the entry of the input for the email field of a user.
     *
     * @param email  an email for a user.
     * @param errors an object of the class Errors.
     */
    private void validateEmail(final String email, final Errors errors) {
        if (!patternEmail.matcher(email).matches()) {
            errors.rejectValue("email", "required.emailRightFormat");
        }
    }

    /**
     * Instance of UserManager class to perfom queries about
     * users.
     */
    private transient UserManagerBean userManager;

    /**
     * Setter method used by spring to inject a userManager bean.
     *
     * @param injUserManager a userManager bean.
     */
    public void setUserManager(final UserManagerBean injUserManager) {
        this.userManager = injUserManager;
    }
}
