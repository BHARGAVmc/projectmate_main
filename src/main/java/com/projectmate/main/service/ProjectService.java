package com.projectmate.main.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.projectmate.main.dto.ProjectCreateRequest;
import com.projectmate.main.dto.ProjectResponse;
import com.projectmate.main.dto.ProjectRequestCreateRequest;
import com.projectmate.main.dto.ProjectRequestResponse;
import com.projectmate.main.dto.MemberResponse;
import com.projectmate.main.entity.User;

public interface ProjectService {
    ProjectResponse createProject(ProjectCreateRequest request, User currentUser);
    Page<ProjectResponse> searchProjects(String category, String skill, String keyword, Pageable pageable);
    ProjectResponse getProjectById(Long projectId);
    void deleteProject(Long projectId, User currentUser);
    ProjectRequestResponse sendRequest(ProjectRequestCreateRequest request, User currentUser);
    ProjectRequestResponse acceptRequest(Long requestId, User currentUser);
    ProjectRequestResponse rejectRequest(Long requestId, User currentUser);
    Page<ProjectResponse> getMyProjects(User currentUser, Pageable pageable);
    Page<ProjectRequestResponse> getMySentRequests(User currentUser, Pageable pageable);
    Page<ProjectRequestResponse> getReceivedRequests(User currentUser, Pageable pageable);
    Page<MemberResponse> getProjectMembers(Long projectId, Pageable pageable);
}
