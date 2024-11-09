package com.example.runningservice.repository;

import com.example.runningservice.entity.RunGoalEntity;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RunGoalRepository extends JpaRepository<RunGoalEntity, Long> {

    void deleteAllByUserId_Id(Long userId);

    // 특정 기간 내의 목표 조회 (startDate와 endDate 사이에 속하는 목표들)
    List<RunGoalEntity> findByStartDateBeforeAndEndDateAfter(LocalDate currentDate);
    List<RunGoalEntity> findByUserId(Long userId);

}
