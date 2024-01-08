package com.example.cms.controller.exceptions;

import com.example.cms.model.entity.PlanDetailKey;

public class PlanDetailNotFoundException extends RuntimeException{
    public PlanDetailNotFoundException(PlanDetailKey key) {
        super("Could not find plandetail " + key);
    }
}
