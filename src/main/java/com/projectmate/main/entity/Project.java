package com.projectmate.main.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.projectmate.main.enums.CompletionStatus;
import com.projectmate.main.enums.ProjectStatus;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "projects")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String category;

    @Column(columnDefinition = "TEXT")
    private String requiredSkills;

    private Integer teamSize;

    private Integer currentMembers = 1;

    private String duration;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private ProjectStatus status = ProjectStatus.OPEN;

    @Enumerated(EnumType.STRING)
    private CompletionStatus completionStatus = CompletionStatus.ONGOING;

    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<ProjectRequest> requests = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<ProjectMember> members = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<Feedback> feedbacks = new ArrayList<>();

}