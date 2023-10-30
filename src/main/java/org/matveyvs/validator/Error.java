package org.matveyvs.validator;

import lombok.ToString;
import lombok.Value;

@Value(staticConstructor = "of")
@ToString
public class Error{
    String code;
    String message;
}
