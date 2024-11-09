package com.example.runningservice.service;

import com.example.runningservice.dto.runGoal.RunGoalRequestDto;
import com.example.runningservice.dto.runGoal.RunGoalResponseDto;
import com.example.runningservice.entity.RunGoalEntity;
import com.example.runningservice.repository.RunGoalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RunGoalServiceTest {

    @InjectMocks
    private RunGoalService runGoalService;

    @Mock
    private RunGoalRepository runGoalRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createRunGoal_Success() {
        // given
        RunGoalRequestDto requestDto = RunGoalRequestDto.builder()
                .startDate(LocalDate.of(2024, 1, 1))
                .endDate(LocalDate.of(2024, 12, 31))
                .targetDistance(1000.0)
                .targetPace(5.0)
                .targetTime(36000)
                .build();

        RunGoalEntity savedEntity = requestDto.toEntity();

        when(runGoalRepository.save(any(RunGoalEntity.class))).thenReturn(savedEntity);

        // when
        RunGoalResponseDto responseDto = runGoalService.createRunGoal(requestDto);

        // then
        assertNotNull(responseDto);
        assertEquals(1000.0, responseDto.getTargetDistance());
        assertEquals(5.0, responseDto.getTargetPace());
    }

    @Test
    void getRunGoal_Success() {
        // given
        Long id = 1L;
        RunGoalEntity runGoalEntity = RunGoalEntity.builder()
                .startDate(LocalDate.of(2024, 1, 1))
                .endDate(LocalDate.of(2024, 12, 31))
                .targetDistance(1000.0)
                .targetPace(5.0)
                .targetTime(36000)
                .build();

        when(runGoalRepository.findById(id)).thenReturn(Optional.of(runGoalEntity));

        // when
        RunGoalResponseDto responseDto = runGoalService.getRunGoal(id);

        // then
        assertNotNull(responseDto);
        assertEquals(1000.0, responseDto.getTargetDistance());
        assertEquals(5.0, responseDto.getTargetPace());
    }

    @Test
    void getRunGoal_NotFound() {
        // given
        Long id = 1L;
        when(runGoalRepository.findById(id)).thenReturn(Optional.empty());

        // when / then
        assertThrows(NoSuchElementException.class, () -> runGoalService.getRunGoal(id));
    }

    @Test
    void updateRunGoal_Success() {
        // given
        Long id = 1L;
        RunGoalEntity existingEntity = RunGoalEntity.builder()
                .startDate(LocalDate.of(2024, 1, 1))
                .endDate(LocalDate.of(2024, 12, 31))
                .targetDistance(1000.0)
                .targetPace(5.0)
                .targetTime(36000)
                .build();

        RunGoalRequestDto updatedDto = RunGoalRequestDto.builder()
                .startDate(LocalDate.of(2024, 1, 1))
                .endDate(LocalDate.of(2024, 12, 31))
                .targetDistance(1500.0) // 업데이트된 거리
                .targetPace(4.5)        // 업데이트된 페이스
                .targetTime(45000)
                .build();

        when(runGoalRepository.findById(id)).thenReturn(Optional.of(existingEntity));
        when(runGoalRepository.save(any(RunGoalEntity.class))).thenReturn(updatedDto.toEntity());

        // when
        RunGoalResponseDto responseDto = runGoalService.updateRunGoal(id, updatedDto);

        // then
        assertNotNull(responseDto);
        assertEquals(1500.0, responseDto.getTargetDistance());
        assertEquals(4.5, responseDto.getTargetPace());
    }

    @Test
    void deleteRunGoal_Success() {
        // given
        Long id = 1L;

        doNothing().when(runGoalRepository).deleteById(id);

        // when
        runGoalService.deleteRunGoal(id);

        // then
        verify(runGoalRepository, times(1)).deleteById(id);
    }

    @Test
    void getAllRunGoals_Success() {
        // given
        List<RunGoalEntity> runGoalEntities = new ArrayList<>();
        runGoalEntities.add(RunGoalEntity.builder()
                .startDate(LocalDate.of(2024, 1, 1))
                .endDate(LocalDate.of(2024, 12, 31))
                .targetDistance(1000.0)
                .targetPace(5.0)
                .targetTime(36000)
                .build());

        when(runGoalRepository.findAll()).thenReturn(runGoalEntities);

        // when
        List<RunGoalResponseDto> responseDtos = runGoalService.getAllRunGoals();

        // then
        assertNotNull(responseDtos);
        assertFalse(responseDtos.isEmpty());
        assertEquals(1, responseDtos.size());
        assertEquals(1000.0, responseDtos.getFirst().getTargetDistance());
    }

    @Test
    void getUserRunGoals_Success() {
        // given
        Long userId = 1L;
        List<RunGoalEntity> runGoalEntities = new ArrayList<>();
        runGoalEntities.add(RunGoalEntity.builder()
                .startDate(LocalDate.of(2024, 1, 1))
                .endDate(LocalDate.of(2024, 12, 31))
                .targetDistance(1000.0)
                .targetPace(5.0)
                .targetTime(36000)
                .build());

        when(runGoalRepository.findByUserId(userId)).thenReturn(runGoalEntities);

        // when
        List<RunGoalResponseDto> responseDtos = runGoalService.getUserRunGoals(userId);

        // then
        assertNotNull(responseDtos);
        assertFalse(responseDtos.isEmpty());
        assertEquals(1000.0, responseDtos.getFirst().getTargetDistance());
    }
}
