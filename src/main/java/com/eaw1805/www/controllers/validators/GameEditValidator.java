package com.eaw1805.www.controllers.validators;

import com.eaw1805.data.model.Game;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Calendar;

public class GameEditValidator implements Validator {

    public boolean supports(Class aClass) {
        return aClass.equals(Game.class);
    }


    public void validate(Object command, Errors errors) {
        final Game game = (Game) command;

        if (game.getDateNextProc() == null) {
            errors.rejectValue("dateNextProc", "required.date");

        } else {
            Calendar gameCal = Calendar.getInstance();
            gameCal.setTime(game.getDateNextProc());

            Calendar today = Calendar.getInstance();

            if (today.get(Calendar.YEAR) > gameCal.get(Calendar.YEAR)) {
                errors.rejectValue("dateNextProc", "invalid.smallDate");

            } else if (today.get(Calendar.MONTH) > gameCal.get(Calendar.MONTH)) {
                errors.rejectValue("dateNextProc", "invalid.smallDate");

            } else if (today.get(Calendar.DAY_OF_MONTH) > gameCal.get(Calendar.DAY_OF_MONTH)) {
                errors.rejectValue("dateNextProc", "invalid.smallDate");
            }
        }

        if (game.getSchedule() < 0) {
            errors.rejectValue("schedule", "invalid.mustBePositive");
        }
    }


}
