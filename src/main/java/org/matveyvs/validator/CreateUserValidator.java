package org.matveyvs.validator;

import org.matveyvs.dto.CreateUserDto;
import org.matveyvs.entity.Role;

public class CreateUserValidator implements Validator<CreateUserDto>{
    private static final CreateUserValidator INSTANCE = new CreateUserValidator();
    @Override
    public ValidationResult isValid(CreateUserDto userDto){
        var validationResult = new ValidationResult();
        if (Role.find(userDto.role()).isEmpty()){
            validationResult.add(Error.of("invalid.role", "Role is invalid"));
        }
        if (userDto.username().isEmpty()){
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


    public static CreateUserValidator getInstance() {
        return INSTANCE;
    }
    private CreateUserValidator(){

    }
}
