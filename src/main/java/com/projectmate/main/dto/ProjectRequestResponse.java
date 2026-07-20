package com.projectmate.main.dto;

import java.time.LocalDateTime;

import com.projectmate.main.enums.RequestStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectRequestResponse {
    private Long requestId;
    private Long projectId;
    private String projectTitle;
    private Long senderId;
    private String senderName;
    private String college;
    private String skills;
    private String experience;
    private RequestStatus status;
    private LocalDateTime requestDate;
}
