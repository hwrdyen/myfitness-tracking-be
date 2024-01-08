package com.example.cms.model.entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
public class PlanDetailKey implements Serializable{

    @Column(name = "workoutplanId")//Part of composite key
    Long workoutplanId;

    @Column(name = "exerciseId")// Part of composite key
    Long exerciseId;

    @Override
    public int hashCode() {
        String concatString = String.valueOf(workoutplanId.hashCode())  + String.valueOf(exerciseId.hashCode());
        return concatString.hashCode();
    }

    public PlanDetailKey(){}

    public PlanDetailKey(Long workoutplanId, Long exerciseId){
        this.setWorkoutplanId(workoutplanId);
        this.setExerciseId(exerciseId);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null){
            return false;
        }
        if (o == this)
            return true;
        if (!(o instanceof PlanDetailKey))
            return false;
        PlanDetailKey other = (PlanDetailKey) o;
        return workoutplanId.equals(other.workoutplanId)  && exerciseId.equals(other.exerciseId);
    }

}
