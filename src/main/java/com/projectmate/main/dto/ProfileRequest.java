package com.projectmate.main.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProfileRequest {

    @NotBlank(message = "College is required")
    private String college;

    @NotBlank(message = "Branch is required")
    private String branch;

    private String year;

    @NotBlank(message = "Skills are required")
    private String skills;

    private String education;

    private String experience;

    private String location;

    private String resume;

    @Size(max = 1000, message = "About must be at most 1000 characters")
    private String about;

    private String profilePhoto;
}
