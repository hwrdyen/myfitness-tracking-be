package com.example.cms.controller;

import com.example.cms.controller.exceptions.ExerciseNotFoundException;
import com.example.cms.model.entity.Exercise;
import com.example.cms.model.repository.ExerciseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
public class ExerciseController {
    @Autowired
    private final ExerciseRepository repository;

    public ExerciseController(ExerciseRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/exercises/{id}")//Search exercise by Id
    Exercise retrieveExercise(@PathVariable("id") Long exerciseId) {
        return repository.findById(exerciseId)
                .orElseThrow(() -> new ExerciseNotFoundException(exerciseId));
    }

    @GetMapping("/exercises")//Both Client and Administrator have access
    List<Exercise> FindAllExercises() {
        return repository.findAll();
    }

    @PostMapping("/exercises")//Only Administrator has access
    Exercise createExercise(@RequestBody Exercise newExercise) {
        if (repository.existsById(newExercise.getId())) {
            throw new IllegalArgumentException("Id Already Existed!");
        } else {
            return repository.save(newExercise);
        }
    }

    @PutMapping("/exercises/{id}")//Only Administrator has access
    Exercise updateExercise(@RequestBody Exercise newExercise, @PathVariable("id") Long exerciseId) {
        return repository.findById(exerciseId)
                .map(exercise -> {
                    exercise.setName(newExercise.getName());
                    exercise.setCalories(newExercise.getCalories());
                    exercise.setVideo_link(newExercise.getVideo_link());
                    exercise.setImage_link(newExercise.getImage_link());
                    return repository.save(exercise);
                })
                .orElseGet(() -> {
                    newExercise.setId(exerciseId);
                    return repository.save(newExercise);
                });
    }

    @GetMapping("/exercises/search/{searchstring}")//Search exercise by name
    List<Exercise> searchExercise(@PathVariable("searchstring") String searchString) {
        return repository.search(searchString);
    }
    @DeleteMapping("/exercises/{id}")//Only Administrator has access
    void deleteExercise(@PathVariable("id") Long exerciseId) {
        repository.deleteById(exerciseId);
    }
}
