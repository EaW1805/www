package com.eaw1805.www.controllers.validators;

import com.eaw1805.data.model.User;
import com.eaw1805.www.commands.TransferCommand;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Validate transfer form.
 */
public class TransferValidator implements Validator {

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = LogManager.getLogger(TransferValidator.class);

    @Override
    public boolean supports(final Class aClass) {
        return aClass.equals(User.class);
    }

    public void validate(final Object command, final Errors errors) {
        final TransferCommand transferCommand = (TransferCommand) command;
        /* Checking if a field is empty or has a white space and reject it.*/


        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "givenPassword", "required.password");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "creditsAmount", "required.credits");

        if (errors.hasErrors()) {
            return;
        }

        if (transferCommand.getReceiver() == null || transferCommand.getReceiver().getUserId() <= 0) {
            errors.rejectValue("receiver", "required.receiver");
            return;
        }

        if (transferCommand.getReceiver().getUsername().equals(transferCommand.getSender().getUsername())) {
            errors.rejectValue("receiver", "wrong.receiver");
            return;
        }

        int amount;
        try {
            amount = Integer.decode(transferCommand.getCreditsAmount());
        } catch (final Exception e) {
            errors.rejectValue("creditsAmount", "invalid.credits");
            return;
        }
        if (amount <= 0) {
            errors.rejectValue("creditsAmount", "invalid.mustBePositive");
            return;
        }

        String md5Password;
        try {
            md5Password = convertToMD5(transferCommand.getGivenPassword());
        } catch (final NoSuchAlgorithmException e) {
            LOGGER.error(e);
            errors.rejectValue("givenPassword", "wrong.password");
            return;
        }

        if (!transferCommand.getSender().getPassword().equals(md5Password)) {
            errors.rejectValue("givenPassword", "wrong.password");
            return;
        }

        if (transferCommand.getSender().getCreditBought() < amount) {
            errors.rejectValue("creditsAmount", "wrong.creditsAmount");
        }
    }

    /**
     * Encrypts the password string of the new user.
     *
     * @param password the password inserted to the form
     * @return the encrypted password.
     * @throws java.security.NoSuchAlgorithmException
     *          No encryption algorithm exception.
     */
    protected String convertToMD5(final String password) throws NoSuchAlgorithmException {

        final MessageDigest messageDialect = MessageDigest.getInstance("MD5");
        messageDialect.update(password.getBytes());

        final byte byteData[] = messageDialect.digest();

        //convert the byte to hex format
        final StringBuilder hexString = new StringBuilder();

        for (byte singleByte : byteData) {
            final String hex = Integer.toHexString(0xff & singleByte);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}



