package com.example.cms.model.entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "workoutplan")
public class WorkoutPlan {
    @Id
    private long id;

    @NotEmpty//record date of workoutplan
    private String dates;
    @ManyToOne
    @JoinColumn(name="clientId")
    private Client client;//Show who is creating the workoutplan

    @NotEmpty
    private String planname;//user specified plan name

    @Nullable
    private double caloriesburned;// calculated by sum of each exercises burned calories

    @OneToMany(mappedBy = "workoutplan")// display the plan details within workout plan
    @Nullable
    private List<PlanDetail> plans = new ArrayList<>();

    public WorkoutPlan(Long id, String dates, Client client, String planname){
        this.id = id;
        this.dates = dates;
        this.client = client;
        this.planname = planname;
    }

}
