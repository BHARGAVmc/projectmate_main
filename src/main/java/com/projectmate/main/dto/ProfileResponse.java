package com.projectmate.main.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponse {
    private Long id;
    private String college;
    private String branch;
    private String year;
    private String skills;
    private String education;
    private String experience;
    private String location;
    private String resume;
    private String about;
    private String profilePhoto;
}
