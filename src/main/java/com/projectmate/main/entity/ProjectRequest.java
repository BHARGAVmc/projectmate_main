package com.projectmate.main.entity;

import java.time.LocalDateTime;

import com.projectmate.main.enums.RequestStatus;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "project_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RequestStatus status = RequestStatus.PENDING;

    private LocalDateTime requestDate = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

}