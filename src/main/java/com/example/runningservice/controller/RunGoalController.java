package com.example.runningservice.controller;

import com.example.runningservice.dto.runGoal.RunGoalRequestDto;
import com.example.runningservice.dto.runGoal.RunGoalResponseDto;
import com.example.runningservice.service.RunGoalService;
import com.example.runningservice.util.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/run/goals")
public class RunGoalController {

    private final RunGoalService runGoalService;
    /**
      * 러닝 목표(전체) 조회
     */
    @GetMapping
    public ResponseEntity<List<RunGoalResponseDto>> getAllRunGoals() {
        List<RunGoalResponseDto> responseDtoList = runGoalService.getAllRunGoals();
        return ResponseEntity.ok(responseDtoList);
    }

    /**
     * 사용자 러닝 목표(전체) 조회
     */
    @GetMapping("/my")
    public ResponseEntity<List<RunGoalResponseDto>> getUserRunGoals(@LoginUser Long userId) {
        List<RunGoalResponseDto> responseDtoList = runGoalService.getUserRunGoals(userId);
        return ResponseEntity.ok(responseDtoList);
    }

    /**
     * 러닝 목표 조회
     */
    @GetMapping("/{runGoalId}")
    public ResponseEntity<RunGoalResponseDto> getRunGoal(@PathVariable Long runGoalId) {
        RunGoalResponseDto responseDto = runGoalService.getRunGoal(runGoalId);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 러닝 목표 생성
     */
    public ResponseEntity<RunGoalResponseDto> createRunGoal(@RequestBody RunGoalRequestDto runGoalRequestDto) {
        RunGoalResponseDto responseDto = runGoalService.createRunGoal(runGoalRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    /**
     * 러닝 목표 수정
     */
    @PutMapping("/{runGoalId}")
    public ResponseEntity<RunGoalResponseDto> updateRunGoal(@PathVariable Long runGoalId, @RequestBody RunGoalRequestDto updatedRunGoalDto) {
        RunGoalResponseDto responseDto = runGoalService.updateRunGoal(runGoalId, updatedRunGoalDto);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 러닝 목표 삭제
     */
    @DeleteMapping("/{runGoalId}")
    public ResponseEntity<Void> deleteRunGoal(@PathVariable Long runGoalId) {
        runGoalService.deleteRunGoal(runGoalId);
        return ResponseEntity.noContent().build();
    }

    /**
     * 러닝 목표 공개 설정
     */
    @PutMapping("/visibility/{runGoalId}")
    public ResponseEntity<RunGoalResponseDto> updateRunGoalVisibility(@PathVariable Long runGoalId, @RequestBody RunGoalRequestDto runGoalRequestDto) {
        RunGoalResponseDto runGoalResponseDto =  runGoalService.updateRunGoal(runGoalId, runGoalRequestDto);
        return ResponseEntity.status(201).body(runGoalResponseDto);
    }
}
