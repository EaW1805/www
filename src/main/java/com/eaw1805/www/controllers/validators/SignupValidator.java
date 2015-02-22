package com.eaw1805.www.controllers.validators;

import com.eaw1805.data.managers.beans.UserManagerBean;
import com.eaw1805.data.model.User;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Validate signup form
 */
public class SignupValidator
        implements Validator {

    /**
     * a log4j logger to print messages.
     */
    protected static final Logger LOGGER = LogManager.getLogger(SignupValidator.class);

    private static final String USERNAME_PATTERN = "^[A-Za-z0-9_-]{3,20}$";

    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private static final Pattern patternUsername = Pattern.compile(USERNAME_PATTERN);

    private static final Pattern patternEmail = Pattern.compile(EMAIL_PATTERN);

    public boolean supports(Class aClass) {
        return aClass.equals(User.class);
    }

    public void validate(Object command, Errors errors) {
        final User user = (User) command;

        /* Checking if a field is empty or has a white space and reject it.*/
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "required.username");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "fullname", "required.fullname");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "required.email");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "emailEncoded", "required.email_confirm");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "required.password");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "userPermissions", "required.password_confirm");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "location", "required.location");

        // check if username is long enough
        if (user.getUsername().length() < 3) {
            errors.rejectValue("username", "required.username_short");
        }

        // check if username is short enough
        if (user.getUsername().length() > 20) {
            errors.rejectValue("username", "required.username_long");
        }

        if (!patternUsername.matcher(user.getUsername()).matches()) {
            errors.rejectValue("username", "required.field_problem");
            return;
        }

        validateSpecial(errors, user.getFullname(), "fullname");
        validateSpecial(errors, user.getPassword(), "password");

        // first round of checks
        if (errors.hasErrors()) {
            return;
        }

        // Check if user with duplicate name exists
        final User thatUser2 = userManager.getByUserName(user.getUsername());
        if (thatUser2 != null && thatUser2.getUserId() != user.getUserId()) {
            errors.rejectValue("username", "required.uniqueUserName");
            return;
        }

        // validate e-mail
        validateEmail(user.getEmail(), errors);

        // second round of checks
        if (errors.hasErrors()) {
            return;
        }

        // Check if confirmation email matches
        if (!user.getEmail().equals(user.getEmailEncoded())) {
            errors.rejectValue("email", "required.email_confirmation");
            errors.rejectValue("emailEncoded", "required.email_confirmation");
        }

        // Check if user with duplicate email exists
        final List<User> thatUser = userManager.searchByEmail(user.getEmail());

        if ((!thatUser.isEmpty()) && (thatUser.get(0).getUserId() != user.getUserId())) {
            errors.rejectValue("email", "required.uniqueEmail");
        }

        // check if password is long enough
        if (user.getPassword().length() < 6) {
            errors.rejectValue("password", "required.password_short");
        }

        // check if password confirms
        if (!user.getPassword().equals(user.getUserPermissions())) {
            errors.rejectValue("password", "required.password_confirmation");
            errors.rejectValue("userPermissions", "required.password_confirmation");
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
