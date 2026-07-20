package com.projectmate.main.mapper;

import org.springframework.stereotype.Component;

import com.projectmate.main.dto.ProjectCreateRequest;
import com.projectmate.main.dto.ProjectResponse;
import com.projectmate.main.entity.Project;
import com.projectmate.main.entity.User;

@Component
public class ProjectMapper {

    public Project toEntity(ProjectCreateRequest request, User owner) {
        Project project = new Project();
        project.setTitle(request.getTitle());
        project.setCategory(request.getCategory());
        project.setRequiredSkills(request.getRequiredSkills());
        project.setTeamSize(request.getTeamSize());
        project.setDuration(request.getDuration());
        project.setDescription(request.getDescription());
        project.setOwner(owner);
        return project;
    }

    public ProjectResponse toResponse(Project project) {
        return ProjectResponse.builder()
                .id(project.getId())
                .title(project.getTitle())
                .category(project.getCategory())
                .requiredSkills(project.getRequiredSkills())
                .teamSize(project.getTeamSize())
                .description(project.getDescription())
                .duration(project.getDuration())
                .status(project.getStatus())
                .ownerId(project.getOwner() != null ? project.getOwner().getId() : null)
                .ownerName(project.getOwner() != null ? project.getOwner().getName() : null)
                .createdAt(project.getCreatedAt())
                .build();
    }
}
