package com.example.runningservice.entity;

import com.example.runningservice.enums.JoinStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.AuditOverride;

@Entity(name = "join_request")
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@AuditOverride(forClass = BaseEntity.class)
public class JoinApplyEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private MemberEntity member;
    @ManyToOne
    @JoinColumn(name = "crew_id")
    private CrewEntity crew;
    @NotNull
    @Enumerated(EnumType.STRING)
    private JoinStatus status;
    private String message;
    public static JoinApplyEntity of(MemberEntity member, CrewEntity crew, String message) {
        return JoinApplyEntity.builder()
            .member(member)
            .crew(crew)
            .message(message)
            .build();
    }

    public void markStatus(JoinStatus status) {
        this.status = status;
    }
}
