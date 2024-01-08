package com.example.cms.controller.exceptions;

public class WorkoutplanNotFoundException extends RuntimeException{
    public WorkoutplanNotFoundException(Long id) {
        super("Could not find workoutplan " + id);
    }
}
