package com.example.runningservice.service;

import com.example.runningservice.dto.runRecord.RunRecordRequestDto;
import com.example.runningservice.dto.runRecord.RunRecordResponseDto;
import com.example.runningservice.entity.MemberEntity;
import com.example.runningservice.entity.RunGoalEntity;
import com.example.runningservice.entity.RunRecordEntity;
import com.example.runningservice.exception.CustomException;
import com.example.runningservice.repository.MemberRepository;
import com.example.runningservice.repository.RunGoalRepository;
import com.example.runningservice.repository.RunRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RunRecordServiceTest {

    @InjectMocks
    private RunRecordService runRecordService;

    @Mock
    private RunRecordRepository runRecordRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private RunGoalRepository runGoalRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createRunRecord_Success() {
        // given
        Long userId = 1L;
        RunRecordRequestDto requestDto = RunRecordRequestDto.builder()
                .goalId(1L)
                .distance(10.0)
                .runningTime("01:30:00")
                .pace("05:00")
                .runningDate(LocalDate.now())
                .build();

        MemberEntity member = MemberEntity.builder().id(userId).build();
        RunGoalEntity goal = RunGoalEntity.builder().id(1L).build();

        when(memberRepository.findById(userId)).thenReturn(Optional.of(member));
        when(runGoalRepository.findById(requestDto.getGoalId())).thenReturn(Optional.of(goal));

        RunRecordEntity runRecordEntity = requestDto.toEntity(member, goal);
        when(runRecordRepository.save(any(RunRecordEntity.class))).thenReturn(runRecordEntity);

        // when
        RunRecordResponseDto responseDto = runRecordService.createRunRecord(userId, requestDto);

        // then
        assertNotNull(responseDto);
        assertEquals(10.0, responseDto.getDistance());
        assertEquals(1800, responseDto.getRunningTime()); // 01:30:00 -> 1800 seconds
        assertEquals(300, responseDto.getPace()); // 05:00 -> 300 seconds
    }

    @Test
    void createRunRecord_UserNotFound() {
        // given
        Long userId = 1L;
        RunRecordRequestDto requestDto = RunRecordRequestDto.builder().build();

        when(memberRepository.findById(userId)).thenReturn(Optional.empty());

        // when / then
        assertThrows(CustomException.class, () -> runRecordService.createRunRecord(userId, requestDto));
    }

    @Test
    void updateRunRecord_Success() {
        // given
        Long recordId = 1L;
        RunRecordRequestDto requestDto = RunRecordRequestDto.builder()
                .goalId(1L)
                .distance(15.0)
                .runningTime("01:45:00")
                .pace("04:50")
                .runningDate(LocalDate.now())
                .build();

        RunRecordEntity existingEntity = RunRecordEntity.builder()
                .id(recordId)
                .distance(10.0)
                .runningTime(3600)
                .pace(300)
                .build();

        when(runRecordRepository.findById(recordId)).thenReturn(Optional.of(existingEntity));
        when(runGoalRepository.getReferenceById(requestDto.getGoalId())).thenReturn(RunGoalEntity.builder().id(1L).build());
        when(runRecordRepository.save(any(RunRecordEntity.class))).thenReturn(existingEntity);

        // when
        RunRecordResponseDto responseDto = runRecordService.updateRunRecord(recordId, requestDto);

        // then
        assertNotNull(responseDto);
        assertEquals(15.0, responseDto.getDistance());
        assertEquals(6300, responseDto.getRunningTime()); // 01:45:00 -> 6300 seconds
    }

    @Test
    void calculateTotalRunRecords_Success() {
        // given
        Long userId = 1L;
        List<RunRecordEntity> runRecords = Arrays.asList(
                RunRecordEntity.builder().distance(5.0).runningTime(1800).pace(300).build(),
                RunRecordEntity.builder().distance(10.0).runningTime(3600).pace(320).build()
        );

        when(runRecordRepository.findByUserId_Id(userId)).thenReturn(runRecords);

        // when
        RunRecordResponseDto responseDto = runRecordService.calculateTotalRunRecords(userId);

        // then
        assertNotNull(responseDto);
        assertEquals(15.0, responseDto.getDistance());
        assertEquals(5400, responseDto.getRunningTime()); // 1800 + 3600
        assertEquals(310, responseDto.getPace()); // 평균 페이스 (300 + 320) / 2 = 310
    }

    @Test
    void getTotalDistanceForPeriod_Success() {
        // given
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);
        when(runRecordRepository.findTotalDistanceBetweenDates(startDate, endDate)).thenReturn(100.0);

        // when
        double totalDistance = runRecordService.getTotalDistanceForPeriod(startDate, endDate);

        // then
        assertEquals(100.0, totalDistance);
    }

    @Test
    void getTotalTimeForPeriod_Success() {
        // given
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);
        when(runRecordRepository.findTotalTimeBetweenDates(startDate, endDate)).thenReturn(7200);

        // when
        int totalTime = runRecordService.getTotalTimeForPeriod(startDate, endDate);

        // then
        assertEquals(7200, totalTime); // 총 러닝 시간 7200초
    }

    @Test
    void getRecordsForPeriod_Success() {
        // given
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);
        List<RunRecordEntity> records = Collections.singletonList(
                RunRecordEntity.builder().distance(10.0).runningDate(LocalDate.of(2024, 6, 15)).build()
        );

        when(runRecordRepository.findByDateBetween(startDate, endDate)).thenReturn(records);

        // when
        List<RunRecordEntity> result = runRecordService.getRecordsForPeriod(startDate, endDate);

        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(10.0, result.getFirst().getDistance());
    }
}
