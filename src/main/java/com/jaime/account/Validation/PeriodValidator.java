package com.jaime.account.Validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PeriodValidator implements
        ConstraintValidator<PeriodValidation, String> {

    public void initialize(PeriodValidator constraintAnnotation) {
    }

    @Override
    public boolean isValid(String strDate, ConstraintValidatorContext context) {
        if (strDate == null || strDate.trim().equals("")) {
            return false;
        } else {
            SimpleDateFormat format = new SimpleDateFormat("MM-yyyy");
            format.setLenient(false);
            try {
                Date javaDate = format.parse(strDate);
            }
            catch (ParseException e) {
                return false;
            }
            return true;
        }
    }
}