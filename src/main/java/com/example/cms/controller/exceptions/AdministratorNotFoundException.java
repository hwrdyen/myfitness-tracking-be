package com.example.cms.controller.exceptions;

public class AdministratorNotFoundException extends RuntimeException{
    public AdministratorNotFoundException(Long id) {
        super("Could not find administrator " + id);
    }
}
