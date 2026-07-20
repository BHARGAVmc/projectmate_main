package com.projectmate.main.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProjectRequestCreateRequest {

    @NotNull(message = "Project ID is required")
    private Long projectId;
}
