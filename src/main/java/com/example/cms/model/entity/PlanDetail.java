package com.example.cms.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "plans")//As a relational table describe relationships between Client, Exercise and workoutplan
public class PlanDetail {

    @EmbeddedId
    PlanDetailKey plandetailId;
    @ManyToOne//Many plan details can be contained in one WorkoutPlan(Part of the composite key)
    @MapsId("workoutplanId")
    @JoinColumn(name = "workoutplanId")
    @JsonIgnoreProperties({"plans"})
    private WorkoutPlan workoutplan;

    @ManyToOne//Many plan details can contain the same exercises
    @MapsId("exerciseId")
    @JoinColumn(name = "exerciseId")
    private Exercise exercise;

    @NotNull//User input
    private double duration;

    @Nullable
    private double calories;

    public PlanDetail(WorkoutPlan workoutplan, Exercise exercise, Double duration){
        this.workoutplan = workoutplan;
        this.exercise = exercise;
        this.duration = duration;
    }


}
