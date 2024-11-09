package com.example.runningservice.service;

import com.example.runningservice.dto.runGoal.RunGoalRequestDto;
import com.example.runningservice.dto.runGoal.RunGoalResponseDto;
import com.example.runningservice.entity.RunGoalEntity;
import com.example.runningservice.repository.RunGoalRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RunGoalService {

    @Autowired
    private RunGoalRepository runGoalRepository;

    // RunGoal 생성
    public RunGoalResponseDto createRunGoal(RunGoalRequestDto runGoalRequestDTO) {
        RunGoalEntity runGoalEntity = runGoalRequestDTO.toEntity();
        runGoalEntity = runGoalRepository.save(runGoalEntity);
        return RunGoalResponseDto.fromEntity(runGoalEntity);
    }

    // RunGoal 업데이트
    public RunGoalResponseDto updateRunGoal(Long id, RunGoalRequestDto updatedRunGoalDTO) {
        RunGoalEntity existingGoal = runGoalRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Goal not found"));

        RunGoalEntity updatedGoal = updatedRunGoalDTO.toEntity();
        updatedGoal = runGoalRepository.save(updatedGoal);
        return RunGoalResponseDto.fromEntity(updatedGoal);
    }

    // RunGoal 삭제
    public void deleteRunGoal(Long id) {
        runGoalRepository.deleteById(id);
    }

    // RunGoal 조회
    public RunGoalResponseDto getRunGoal(Long id) {
        RunGoalEntity runGoal = runGoalRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Goal not found"));
        return RunGoalResponseDto.fromEntity(runGoal);
    }

    // 전체 러닝 목표 조회
    public List<RunGoalResponseDto> getAllRunGoals() {
        List<RunGoalEntity> runGoals = runGoalRepository.findAll();
        return runGoals.stream()
                .map(RunGoalResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    // 특정 사용자의 러닝 목표 조회
    public List<RunGoalResponseDto> getUserRunGoals(Long userId) {
        List<RunGoalEntity> runGoals = runGoalRepository.findByUserId(userId);
        return runGoals.stream()
                .map(RunGoalResponseDto::fromEntity)
                .collect(Collectors.toList());
    }
}
