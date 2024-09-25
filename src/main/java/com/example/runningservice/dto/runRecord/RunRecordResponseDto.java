package com.example.runningservice.dto.runRecord;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.runningservice.entity.RunRecordEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RunRecordResponseDto {
    private Long id;
    private Long userId;
    private Double distance;
    private Integer runningTime;
    private Integer pace;
    private Integer runCount;
    private LocalDate runningDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static RunRecordResponseDto fromEntity(RunRecordEntity entity) {
        return RunRecordResponseDto.builder()
                .id(entity.getId())
                .userId(entity.getUserId().getId())
                .distance(entity.getDistance())
                .runningTime(entity.getRunningTime())
                .pace(entity.getPace())
                .runningDate(entity.getRunningDate())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
