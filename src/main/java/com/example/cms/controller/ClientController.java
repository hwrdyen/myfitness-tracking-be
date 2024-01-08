package com.example.cms.controller;
import com.example.cms.controller.exceptions.ClientNotFoundException;
import com.example.cms.controller.exceptions.ExerciseNotFoundException;
import com.example.cms.model.entity.Client;
import com.example.cms.model.entity.Exercise;
import com.example.cms.model.entity.User;
import com.example.cms.model.repository.ClientRepository;
import com.example.cms.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
public class ClientController {
    @Autowired
    private final ClientRepository repository;

    @Autowired
    private UserRepository userRepository;

    public ClientController(ClientRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/clients")//Only administrator have access
    List<Client> retrieveAllClients() {
        return repository.findAll();
    }

    @GetMapping("/clients/{id}")//Search exercise by Id
    Client retrieveClient(@PathVariable("id") Long clientId) {
        return repository.findById(clientId)
                .orElseThrow(() -> new ExerciseNotFoundException(clientId));
    }

    @PostMapping("/clients")//Only Client have access(Id are automatically generated)
    Client createClient(@RequestBody Client newClient) {
        if (repository.existsById(newClient.getId())) {
            throw new IllegalArgumentException("Id Already Existed!");
        } else {
            return repository.save(newClient);
        }
    }



    @PutMapping("/clients/{id}")//Both client and administrator have access(Client can only change their own profile)
    Client updateClient(@RequestBody Client newClient, @PathVariable("id") Long clientId) {
        userRepository.findById(clientId).map(user -> {
            user.setFirstName(newClient.getFirstName());
            user.setLastName(newClient.getLastName());
            user.setEmail(newClient.getEmail());
             return userRepository.save(user);
        }).orElseGet(() -> {
            throw new IllegalArgumentException("Id Does Not Exist!");
        });

        return repository.findById(clientId)
                .map(client -> {
                    client.setFirstName(newClient.getFirstName());
                    client.setLastName(newClient.getLastName());
                    client.setEmail(newClient.getEmail());
                    return repository.save(client);
                })
                .orElseGet(() -> {
                    newClient.setId(clientId);
                    return repository.save(newClient);
                });
    }

    @DeleteMapping("/clients/{id}")//Only Administrator has access
    void deleteClient(@PathVariable("id") Long clientId) {
        repository.deleteById(clientId);
    }

}
