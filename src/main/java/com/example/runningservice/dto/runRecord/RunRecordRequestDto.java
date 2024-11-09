package com.example.runningservice.dto.runRecord;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import com.example.runningservice.entity.MemberEntity;
import com.example.runningservice.entity.RunGoalEntity;
import com.example.runningservice.entity.RunRecordEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RunRecordRequestDto {
    private Long goalId;
    private Double distance = 0.0;
    private String runningTime = "00:00:00";
    private String pace = "00:00";
    private LocalDate runningDate;

    public RunRecordEntity toEntity(MemberEntity member, RunGoalEntity goal) {
        // transformDTO 메소드를 통해 runningTime과 pace를 정수형으로 변환
        Map<String, Integer> timeMap = transformDTO();
        return RunRecordEntity.builder()
                .userId(member)
                .goalId(goal)
                .distance(this.distance)
                .runningTime(timeMap.get("runningTime"))
                .pace(timeMap.get("pace"))
                .runningDate(this.runningDate)
                .build();
    }

    private Map<String, Integer> transformDTO() {
        Map<String, Integer> map = new HashMap<>();
        // runningTime 시:분:초 -> sec 변환
        String[] runningTimes = this.runningTime.split(":");
        int runningTime = Integer.parseInt(runningTimes[0]) * 3600 + Integer.parseInt(runningTimes[1]) * 60 + Integer.parseInt(runningTimes[2]);
        map.put("runningTime", runningTime);

        // pace 분:초 -> sec 변환
        String[] paces = this.pace.split(":");
        int pace = Integer.parseInt(paces[0]) * 60 + Integer.parseInt(paces[1]);
        map.put("pace", pace);

        return map;
    }
}
