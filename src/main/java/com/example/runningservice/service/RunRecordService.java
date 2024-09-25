package com.example.runningservice.service;

import com.example.runningservice.dto.runRecord.RunRecordRequestDto;
import com.example.runningservice.dto.runRecord.RunRecordResponseDto;
import com.example.runningservice.entity.MemberEntity;
import com.example.runningservice.entity.RunGoalEntity;
import com.example.runningservice.entity.RunRecordEntity;
import com.example.runningservice.exception.CustomException;
import com.example.runningservice.exception.ErrorCode;
import com.example.runningservice.repository.MemberRepository;
import com.example.runningservice.repository.RunGoalRepository;
import com.example.runningservice.repository.RunRecordRepository;

import java.time.LocalDate;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RunRecordService {
    private final RunRecordRepository runRecordRepository;

    private final MemberRepository memberRepository;
    private final RunGoalRepository runGoalRepository;

    public List<RunRecordResponseDto> findByUserId(Long userId) {
        List<RunRecordEntity> runRecords = runRecordRepository.findByUserId_Id(userId);

        return runRecords.stream()
            .map(this::entityToDto)
            .collect(Collectors.toList());
    }

    public RunRecordResponseDto createRunRecord(Long userId, RunRecordRequestDto runRecordRequestDto) {
        MemberEntity member = memberRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        RunGoalEntity goal = runGoalRepository.findById(runRecordRequestDto.getGoalId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_RUN_GOAL));

        RunRecordEntity runRecordEntity = runRecordRequestDto.toEntity(member, goal);
        runRecordEntity.setCreatedAt(LocalDateTime.now());
        runRecordEntity.setUpdatedAt(LocalDateTime.now());

        RunRecordEntity savedEntity = runRecordRepository.save(runRecordEntity);
        return RunRecordResponseDto.fromEntity(savedEntity);
    }

    public RunRecordResponseDto updateRunRecord(Long runningId, RunRecordRequestDto runRecordRequestDto) {
        RunRecordEntity existingEntity = runRecordRepository
                .findById(runningId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_RUN_RECORD));

        RunRecordEntity updatedEntity = runRecordRequestDto.toEntity(existingEntity.getUserId(), existingEntity.getGoalId());
        updatedEntity.setId(existingEntity.getId());
        updatedEntity.setCreatedAt(existingEntity.getCreatedAt());
        updatedEntity.setUpdatedAt(LocalDateTime.now());

        RunRecordEntity savedEntity = runRecordRepository.save(updatedEntity);
        return RunRecordResponseDto.fromEntity(savedEntity);
    }

    public Optional<RunRecordResponseDto> findById(Long id) {
        return runRecordRepository.findById(id).map(this::entityToDto);
    }

    public void deleteById(Long id) {
        runRecordRepository.deleteById(id);
    }

    public RunRecordResponseDto calculateTotalRunRecords(Long userId) {
        List<RunRecordEntity> runRecords = runRecordRepository.findByUserId_Id(userId);

        if (runRecords.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_RUN_RECORD);
        }


        double totalDistance = runRecords.stream()
            .mapToDouble(RunRecordEntity::getDistance)
            .sum();

        int totalRunningTime = runRecords.stream()
            .mapToInt(RunRecordEntity::getRunningTime)
            .sum();

        int totalPace = runRecords.stream()
            .mapToInt(RunRecordEntity::getPace)
            .sum();

        int averagePace = totalPace / (runRecords.size());

        return RunRecordResponseDto.builder()
            .userId(userId)
            .distance(totalDistance)
            .runningTime(totalRunningTime)
            .pace(averagePace)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
    }

    public RunRecordResponseDto entityToDto(RunRecordEntity entity) {
        if (entity == null || entity.getUserId() == null) {
            throw new IllegalArgumentException("러닝 기록이 없거나 유저정보가 없습니다.");
        }
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

    // 특정 기간 동안의 총 거리 구하기
    public double getTotalDistanceForPeriod(LocalDate startDate, LocalDate endDate) {
        return runRecordRepository.findTotalDistanceBetweenDates(startDate, endDate);
    }

    // 특정 기간 동안의 총 러닝 시간 구하기
    public int getTotalTimeForPeriod(LocalDate startDate, LocalDate endDate) {
        return runRecordRepository.findTotalTimeBetweenDates(startDate, endDate);
    }

    // 특정 기간 동안의 모든 기록 조회
    public List<RunRecordEntity> getRecordsForPeriod(LocalDate startDate, LocalDate endDate) {
        return runRecordRepository.findByDateBetween(startDate, endDate);
    }

}
