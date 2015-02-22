package com.eaw1805.www.controllers.validators;

import com.eaw1805.data.model.User;
import com.eaw1805.data.model.paypal.PaypalTransaction;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Validates the fields of a receipt request form.
 */
public class ReceiptValidator
        implements Validator {

    public boolean supports(Class aClass) {
        return aClass.equals(User.class);
    }

    public void validate(Object command, Errors errors) {
        final PaypalTransaction trans = (PaypalTransaction) command;

        /* Checking if a field is empty or has a white space and reject it.*/
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "payerName", "required.payerName");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "payerAddress", "required.payerAddress");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "payerCity", "required.payerCity");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "payerPOCode", "required.payerPOCode");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "payerCountry", "required.payerCountry");

        validateSpecial(errors, trans.getPayerName(), "payerName");
        validateSpecial(errors, trans.getPayerAddress(), "payerAddress");
        validateSpecial(errors, trans.getPayerCity(), "payerCity");
        validateSpecial(errors, trans.getPayerPOCode(), "payerPOCode");
        validateSpecial(errors, trans.getPayerCountry(), "payerCountry");
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

}
