package com.scm.exceptionHelpers;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message){
        super(message);
    }

    public ResourceNotFoundException(){
        super("Resource Not Found");
    }

}
