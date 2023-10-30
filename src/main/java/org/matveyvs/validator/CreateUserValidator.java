package org.matveyvs.validator;

import org.matveyvs.dto.UserCreateDto;
import org.matveyvs.entity.Role;
import org.springframework.stereotype.Component;

@Component
public class CreateUserValidator implements Validator<UserCreateDto>{
    public ValidationResult isValid(UserCreateDto userDto){
        var validationResult = new ValidationResult();
        if (Role.find(String.valueOf(userDto.role())).isEmpty()){
            validationResult.add(Error.of("invalid.role", "Role is invalid"));
        }
        if (userDto.userName().isEmpty() || usernameValidation(userDto.userName())){
            validationResult.add(Error.of("invalid.username", "Username is invalid"));
        }
        if (userDto.email().isEmpty()){
            validationResult.add(Error.of("invalid.email", "Email is invalid"));
        }
        if (userDto.password().isEmpty()){
            validationResult.add(Error.of("invalid.password", "Password is invalid"));
        }
        if (userDto.firstName().isEmpty()){
            validationResult.add(Error.of("invalid.firstname", "First name is invalid"));
        }
        if (userDto.lastName().isEmpty()){
            validationResult.add(Error.of("invalid.lastname", "Last name is invalid"));
        }
        return validationResult;
    }

    private boolean usernameValidation(String username){
        return username.contains("@");
    }
}
