package com.example.runningservice.dto.runGoal;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.runningservice.entity.RunGoalEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RunGoalResponseDto {
    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private double targetDistance;
    private double targetPace;
    private int targetTime;
    private boolean achieved;
    private double achievementRate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public RunGoalResponseDto(Long id, LocalDate startDate, LocalDate endDate, double targetDistance, double targetPace, int targetTime, boolean achieved) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.targetDistance = targetDistance;
        this.targetPace = targetPace;
        this.targetTime = targetTime;
        this.achieved = achieved;
    }

    public static RunGoalResponseDto fromEntity(RunGoalEntity entity) {
        return RunGoalResponseDto.builder()
                .id(entity.getId())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .targetDistance(entity.getTargetDistance())
                .targetPace(entity.getTargetPace())
                .targetTime(entity.getTargetTime())
                .achieved(entity.isAchieved())
                .build();
    }
}
