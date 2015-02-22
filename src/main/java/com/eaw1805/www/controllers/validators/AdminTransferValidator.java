package com.eaw1805.www.controllers.validators;

import com.eaw1805.data.model.User;
import com.eaw1805.www.commands.AdminTransferCommand;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Validate transfer form.
 */
public class AdminTransferValidator implements Validator {

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = LogManager.getLogger(AdminTransferValidator.class);

    @Override
    public boolean supports(final Class aClass) {
        return aClass.equals(User.class);
    }

    public void validate(final Object command, final Errors errors) {
        final AdminTransferCommand transferCommand = (AdminTransferCommand ) command;
        /* Checking if a field is empty or has a white space and reject it.*/

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "comment", "required.comment");

        if (errors.hasErrors()) {
            return;
        }

        if (transferCommand.getReceiver() == null || transferCommand.getReceiver().equals("")) {
            errors.rejectValue("receiver", "required.receiver");
            return;
        }

        int freeCreditsAmount = 0;
        int boughtCreditsAmount = 0;
        int transferredCreditsAmount = 0;

        try {
            freeCreditsAmount = Integer.decode(transferCommand.getFreeCreditsAmount());
        } catch (final Exception e) {
            errors.rejectValue("freeCreditsAmount", "invalid.credits");
            return;
        }

        try {
            boughtCreditsAmount  = Integer.decode(transferCommand.getBoughtCreditsAmount());
        } catch (final Exception e) {
            errors.rejectValue("boughtCreditsAmount ", "invalid.credits");
            return;
        }

        try {
            transferredCreditsAmount = Integer.decode(transferCommand.getTransferredCreditsAmount());
        } catch (final Exception e) {
            errors.rejectValue("transferredCreditsAmount", "invalid.credits");
            return;
        }

        if(freeCreditsAmount == boughtCreditsAmount && boughtCreditsAmount == transferredCreditsAmount && transferredCreditsAmount == 0){
            errors.rejectValue("transferredCreditsAmount", "invalid.credits");
            errors.rejectValue("boughtCreditsAmount ", "invalid.credits");
            errors.rejectValue("freeCreditsAmount", "invalid.credits");
        }

    }

}




