package com.projectmate.main.dto;

import java.time.LocalDateTime;

import com.projectmate.main.enums.ProjectStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectResponse {
    private Long id;
    private String title;
    private String category;
    private String requiredSkills;
    private Integer teamSize;
    private String description;
    private String duration;
    private ProjectStatus status;
    private Long ownerId;
    private String ownerName;
    private Integer currentMembers;
    private Integer remainingSeats;
    private Integer pendingRequestsCount;
    private LocalDateTime createdAt;
}
