package com.example.runningservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "runGoal")
@EntityListeners(AuditingEntityListener.class)
public class RunGoalEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate startDate;
    private LocalDate endDate;

    private double targetDistance;  // 목표 누적 거리 (km)
    private double targetPace;      // 목표 평균 페이스 (min/km)
    private int targetTime;         // 목표 누적 시간 (초 단위)

    // 추가로 목표의 달성 여부를 기록할 수 있는 필드들
    private boolean achieved = false;

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Builder
    public RunGoalEntity(LocalDate startDate, LocalDate endDate, double targetDistance, double targetPace, int targetTime) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.targetDistance = targetDistance;
        this.targetPace = targetPace;
        this.targetTime = targetTime;
    }
}
