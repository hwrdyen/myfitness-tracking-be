package com.example.cms.controller;
import com.example.cms.controller.dto.WorkoutplanDto;
import com.example.cms.controller.exceptions.ClientNotFoundException;
import com.example.cms.controller.exceptions.WorkoutplanNotFoundException;
import com.example.cms.model.entity.Client;
import com.example.cms.model.entity.WorkoutPlan;
import com.example.cms.model.repository.ClientRepository;
import com.example.cms.model.repository.WorkoutplanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
public class WorkoutPlanController {
    @Autowired
    private final WorkoutplanRepository repository;

    @Autowired
    private ClientRepository clientRepository;


    public WorkoutPlanController(WorkoutplanRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/workoutplans/{id}")//Search workoutplan by Id
    WorkoutPlan retrieveWorkoutplan(@PathVariable("id") Long WorkoutplanId) {
        return repository.findById(WorkoutplanId)
                .orElseThrow(() -> new WorkoutplanNotFoundException(WorkoutplanId));
    }

    @GetMapping("/individualWorkoutplan/{clientId}")//Search workoutplan by clientId
    List<WorkoutPlan> retrieveClientWorkoutPlan(@PathVariable("clientId") Long clientId) {
        return repository.findMyPlans(clientId);
    }

    @GetMapping("/workoutplans")//Both Client and Administrator have access
    List<WorkoutPlan> FindAllWorkoutplans() {
        return repository.findAll();
    }

    @PostMapping("/workoutplans")//User create workoutplans
    WorkoutPlan createWorkoutplan(@RequestBody WorkoutplanDto workoutplanDto) {
        if (repository.existsById(workoutplanDto.getId())) {
            throw new IllegalArgumentException("Id Already Existed!");
        } else {
            WorkoutPlan newWorkoutplan = new WorkoutPlan();
            newWorkoutplan.setId(workoutplanDto.getId());
            newWorkoutplan.setDates(workoutplanDto.getDates());
            Client client = clientRepository.findById(workoutplanDto.getClientId()).orElseThrow(
                    () -> new ClientNotFoundException(workoutplanDto.getClientId()));
            newWorkoutplan.setClient(client);
            newWorkoutplan.setPlanname(workoutplanDto.getPlanname());
            return repository.save(newWorkoutplan);
        }
    }

    @PutMapping("/workoutplans/{id}")//User update workoutplans
    WorkoutPlan updateWorkoutplan(@RequestBody WorkoutplanDto workoutplanDto, @PathVariable("id") Long workoutplanId) {
        return repository.findById(workoutplanId)
                .map(workoutplan -> {
                    workoutplan.setDates(workoutplanDto.getDates());
                    Client client = clientRepository.findById(workoutplanDto.getClientId()).orElseThrow(
                            () -> new ClientNotFoundException(workoutplanDto.getClientId()));
                    workoutplan.setClient(client);
                    workoutplan.setPlanname(workoutplanDto.getPlanname());
                    return repository.save(workoutplan);
                })
                .orElseGet(() -> {
                    WorkoutPlan newWorkoutplan = new WorkoutPlan();
                    newWorkoutplan.setId(workoutplanId);
                    newWorkoutplan.setDates(workoutplanDto.getDates());
                    Client client = clientRepository.findById(workoutplanDto.getClientId()).orElseThrow(
                            () -> new ClientNotFoundException(workoutplanDto.getClientId()));
                    newWorkoutplan.setClient(client);
                    newWorkoutplan.setPlanname(workoutplanDto.getPlanname());
                    return repository.save(newWorkoutplan);
                });
    }

    @GetMapping("/workoutplans/result/{id}")//Calculate calories for workout plan
    WorkoutPlan planresult(@PathVariable("id") Long WorkoutplanId){
        repository.calcCalories(WorkoutplanId);
        return retrieveWorkoutplan(WorkoutplanId);
    }
    @DeleteMapping("/workoutplans/{id}")//User delete workoutplans
    void deleteWorkoutplan(@PathVariable("id") Long workoutplanId) {
        repository.deleteById(workoutplanId);
    }



}
