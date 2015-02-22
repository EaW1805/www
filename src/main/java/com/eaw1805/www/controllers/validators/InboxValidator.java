package com.eaw1805.www.controllers.validators;


import com.eaw1805.data.model.Message;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Validates inbox messages.
 */
public class InboxValidator implements Validator {

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = LogManager.getLogger(InboxValidator.class);

    @Override
    public boolean supports(final Class aClass) {
        return aClass.equals(Message.class);
    }

    @Override
    public void validate(Object inputMessage, Errors errors) {
        final Message message = (Message) inputMessage;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "subject", "required.subject");
        if (errors.hasErrors()) {
            return;
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "bodyMessage", "required.body");
        if (errors.hasErrors()) {
            return;
        }

    }
}
