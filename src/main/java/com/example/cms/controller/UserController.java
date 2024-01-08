package com.example.cms.controller;

import com.example.cms.controller.exceptions.UserNotFoundException;
import com.example.cms.model.entity.Administrator;
import com.example.cms.model.entity.Client;
import com.example.cms.model.entity.User;
import com.example.cms.model.repository.AdministratorRepository;
import com.example.cms.model.repository.ClientRepository;
import com.example.cms.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
public class UserController {
    @Autowired
    private final UserRepository repository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AdministratorRepository administratorRepository;

    public UserController(UserRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/users")
    List<User> retrieveAllProfessors() {
        return repository.findAll();
    }

    @GetMapping("/users/{id}")
    User retrieveUser(@PathVariable("id") Long userId) {
        return repository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    @PostMapping("/users")
    User createUser(@RequestBody User newUser) {
        if (repository.existsById(newUser.getId())) {
            throw new IllegalArgumentException("Id Already Existed!");
        } else {
            if (newUser.getIsAdmin() == true) {
                // Add NewUser into Administrator Table is isAdmin == true
                Administrator admin = new Administrator();
                admin.setId(newUser.getId());
                admin.setFirstName(newUser.getFirstName());
                admin.setLastName(newUser.getLastName());
                admin.setEmail(newUser.getEmail());
                administratorRepository.save(admin);

            } else {
                // Add NewUser into Client Table is isAdmin == false
                Client client = new Client();
                client.setId(newUser.getId());
                client.setFirstName(newUser.getFirstName());
                client.setLastName(newUser.getLastName());
                client.setEmail(newUser.getEmail());
                clientRepository.save(client);
            }
            return repository.save(newUser);
        }

    }
}
