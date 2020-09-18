package com.vega.springit.model.validator;
import com.vega.springit.model.User;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

// ON
public class PasswordsMatchValidator implements ConstraintValidator<PasswordsMatch, User> {

    @Override
    public void initialize(PasswordsMatch passwordsMatch){
    }

    public boolean isValid(User user, ConstraintValidatorContext context) {
        return user.getPassword().equals(user.getConfirmPassword());
    }

}