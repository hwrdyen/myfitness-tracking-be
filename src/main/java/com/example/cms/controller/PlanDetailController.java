package com.example.cms.controller;
import com.example.cms.controller.dto.PlanDetailDto;
import com.example.cms.controller.dto.WorkoutplanDto;
import com.example.cms.controller.exceptions.ClientNotFoundException;
import com.example.cms.controller.exceptions.ExerciseNotFoundException;
import com.example.cms.controller.exceptions.PlanDetailNotFoundException;
import com.example.cms.controller.exceptions.WorkoutplanNotFoundException;
import com.example.cms.model.entity.*;
import com.example.cms.model.repository.ClientRepository;
import com.example.cms.model.repository.ExerciseRepository;
import com.example.cms.model.repository.PlanDetailRepository;
import com.example.cms.model.repository.WorkoutplanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
public class PlanDetailController {
    @Autowired
    private final PlanDetailRepository repository;

    @Autowired
    private WorkoutplanRepository workoutplanRepository;
    @Autowired
    private ExerciseRepository exerciseRepository;


    public PlanDetailController(PlanDetailRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/plans/{workoutplanId}/{exerciseId}")
    PlanDetail retrievePlans(@PathVariable ("workoutplanId") long workoutplanId ,
                             @PathVariable ("exerciseId") long exerciseId ) {

        PlanDetailKey key = new PlanDetailKey();
        key.setWorkoutplanId(workoutplanId);
        key.setExerciseId(exerciseId);

        return repository.findById(key)
                .orElseThrow(() -> new PlanDetailNotFoundException(key));
    }
    @GetMapping("/plans/result/{workoutplanId}/{exerciseId}")//Calculate calories for a plan
    PlanDetail planresult(@PathVariable("workoutplanId") Long WorkoutplanId,
                           @PathVariable ("exerciseId") Long ExerciseId){
        repository.calcCalories();
        return retrievePlans(WorkoutplanId, ExerciseId);
    }
    @GetMapping("/plans/{searchTerm}")//Search exercise by Id
    List<PlanDetail> retrieveExercise(@PathVariable("searchTerm") Long searchTerm) {
        return repository.retrieveAllplans(searchTerm);

    }

    @PostMapping ("/plans")
    PlanDetail createPlans(@RequestBody PlanDetailDto plandetailDto) {

        PlanDetail newPlanDetail = new PlanDetail();
        long workoutplanId = plandetailDto.getWorkoutplanId();
        long exerciseId = plandetailDto.getExerciseId();
        double duration = plandetailDto.getDuration();

        PlanDetailKey key = new PlanDetailKey();
        key.setWorkoutplanId(plandetailDto.getWorkoutplanId());
        key.setExerciseId(plandetailDto.getExerciseId());
        newPlanDetail.setPlandetailId(key);

        WorkoutPlan workoutplan = workoutplanRepository.findById(workoutplanId).orElseThrow(
                () -> new WorkoutplanNotFoundException(plandetailDto.getWorkoutplanId()));
        Exercise exercise = exerciseRepository.findById(exerciseId).orElseThrow(
                () -> new ExerciseNotFoundException(plandetailDto.getExerciseId()));

        newPlanDetail.setWorkoutplan(workoutplan);
        newPlanDetail.setExercise(exercise);
        newPlanDetail.setDuration(duration);

        return repository.save(newPlanDetail);
    }

    @PutMapping ("/plans/{workoutplanId}/{exerciseId}")
    PlanDetail updatePlans(@RequestBody PlanDetailDto plandetailDto,
                           @PathVariable ("workoutplanId") long workoutplanId ,
                           @PathVariable ("exerciseId") long exerciseId ) {

        double duration = plandetailDto.getDuration();

        PlanDetailKey key = new PlanDetailKey();
        key.setWorkoutplanId(plandetailDto.getWorkoutplanId());
        key.setExerciseId(plandetailDto.getExerciseId());


        return repository.findById(key)
                .map(planDetail -> {
                    planDetail.setDuration (plandetailDto.getDuration());
                    return repository .save(planDetail) ;
                })
                . orElseGet(() -> {
                    PlanDetail newPlanDetail = new PlanDetail();
                    newPlanDetail.setPlandetailId(key);
                    newPlanDetail.setDuration(duration);

                    WorkoutPlan workoutplan = workoutplanRepository.findById(workoutplanId).orElseThrow(
                            () -> new WorkoutplanNotFoundException(workoutplanId));
                    Exercise exercise = exerciseRepository.findById(exerciseId).orElseThrow(
                            () -> new ExerciseNotFoundException(exerciseId));

                    newPlanDetail.setWorkoutplan(workoutplan);
                    newPlanDetail.setExercise(exercise);
                    return repository.save(newPlanDetail);

                });
    }
    @DeleteMapping ("/plans/{workoutplanId}/{exerciseId}")
    void deletePlan(@PathVariable ("workoutplanId") long workoutplanId ,
                           @PathVariable ("exerciseId") long exerciseId ) {

        PlanDetailKey key = new PlanDetailKey();
        key.setWorkoutplanId(workoutplanId);
        key.setExerciseId(exerciseId);

        repository.deleteById(key);
    }



}
