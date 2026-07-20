package com.projectmate.main.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String college;

    private String branch;

    @Column(name = "study_year")
    private String studyYear;

    @Column(columnDefinition = "TEXT")
    private String skills;

    private String education;

    @Column(columnDefinition = "TEXT")
    private String experience;

    private String location;

    private String resume;

    @Column(columnDefinition = "TEXT")
    private String about;

    private String profilePhoto;

    private Integer projectsCompleted = 0;

    private Double averageRating = 0.0;

    private Double averageContribution = 0.0;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

}