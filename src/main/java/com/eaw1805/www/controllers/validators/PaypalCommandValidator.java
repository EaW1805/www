package com.eaw1805.www.controllers.validators;

import com.eaw1805.data.model.User;
import com.eaw1805.www.commands.PaypalCommand;
import com.eaw1805.www.controllers.payments.PaypalManager;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Validate paypal command.
 */
public class PaypalCommandValidator implements Validator {

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = LogManager.getLogger(PaypalCommandValidator.class);

    @Override
    public boolean supports(final Class aClass) {
        return aClass.equals(User.class);
    }

    public void validate(final Object command, final Errors errors) {

        final PaypalCommand paypalCommand = (PaypalCommand) command;

        /* Checking if a field is empty or has a white space and reject it.*/
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "amount", "required.comment");
        if (errors.hasErrors()) {
            return;
        }

        if (paypalCommand.getAmount() == null || paypalCommand.getAmount().isEmpty()) {
            errors.rejectValue("amount", "required.comment");
            return;
        }

        int amount = 0;
        try {
            amount = Integer.decode(paypalCommand.getAmount());
        } catch (final Exception e) {
            errors.rejectValue("amount", "invalid.credits");
            return;
        }

        if (!PaypalManager.VALID_AMOUNTS.contains(amount)) {
            errors.rejectValue("amount", "invalid.credits");
        }
    }

}




