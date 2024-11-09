package com.example.runningservice.dto.runGoal;

import com.example.runningservice.entity.RunGoalEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
public class RunGoalRequestDto {
    private LocalDate startDate;
    private LocalDate endDate;
    private double targetDistance;
    private double targetPace;
    private int targetTime;

    @Builder
    public RunGoalRequestDto(LocalDate startDate, LocalDate endDate, double targetDistance, double targetPace, int targetTime) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.targetDistance = targetDistance;
        this.targetPace = targetPace;
        this.targetTime = targetTime;
    }

    public RunGoalEntity toEntity() {
        return RunGoalEntity.builder()
                .startDate(startDate)
                .endDate(endDate)
                .targetDistance(targetDistance)
                .targetPace(targetPace)
                .targetTime(targetTime)
                .build();
    }
}
