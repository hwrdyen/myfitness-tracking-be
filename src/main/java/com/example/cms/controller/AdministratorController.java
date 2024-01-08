package com.example.cms.controller;


import com.example.cms.controller.exceptions.AdministratorNotFoundException;
import com.example.cms.model.entity.Administrator;

import com.example.cms.model.entity.Client;
import com.example.cms.model.repository.AdministratorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
public class AdministratorController {
    @Autowired
    private final AdministratorRepository repository;

    public AdministratorController(AdministratorRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/administrators")//Administrator create their account
    Administrator createAdministrator(@RequestBody Administrator newAdministrator) {
        if (repository.existsById(newAdministrator.getId())) {
            throw new IllegalArgumentException("Id Already Existed!");
        } else {
            return repository.save(newAdministrator);
        }
    }

    @GetMapping("/administrators")//Only administrator have access
    List<Administrator> retrieveAllAdministrators() {
        return repository.findAll();
    }

    @GetMapping("/administrators/{id}")//Administrators can only access their own profile(id should equal to admin id)
    Administrator retrieveAdministrator(@PathVariable("id") Long administratorId) {
        return repository.findById(administratorId)
                .orElseThrow(() -> new AdministratorNotFoundException(administratorId));
    }

    @PutMapping("/administrators/{id}")//Administrator can only change their own profile
    Administrator updateAdministrator(@RequestBody Administrator newAdministrator, @PathVariable("id") Long administratorId) {
        return repository.findById(administratorId)
                .map(administrator -> {
                    administrator.setFirstName(newAdministrator.getFirstName());
                    administrator.setLastName(newAdministrator.getLastName());
                    administrator.setEmail(newAdministrator.getEmail());
                    return repository.save(administrator);
                })
                .orElseGet(() -> {
                    newAdministrator.setId(administratorId);
                    return repository.save(newAdministrator);
                });
    }

    @DeleteMapping("/administrators/{id}")//Administrators can only delete their own account
    void deleteAdministrator(@PathVariable("id") Long administratorId) {
        repository.deleteById(administratorId);
    }

}
