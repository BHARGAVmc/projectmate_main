package com.projectmate.main.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "feedback")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer rating;

    private Integer contribution;

    @Column(columnDefinition = "TEXT")
    private String feedback;

    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "from_user_id")
    private User fromUser;

    @ManyToOne
    @JoinColumn(name = "to_user_id")
    private User toUser;

}