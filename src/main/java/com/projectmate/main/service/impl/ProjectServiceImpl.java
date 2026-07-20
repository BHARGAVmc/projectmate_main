package com.projectmate.main.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projectmate.main.dto.MemberResponse;
import com.projectmate.main.dto.ProjectCreateRequest;
import com.projectmate.main.dto.ProjectRequestCreateRequest;
import com.projectmate.main.dto.ProjectRequestResponse;
import com.projectmate.main.dto.ProjectResponse;
import com.projectmate.main.entity.Profile;
import com.projectmate.main.entity.Project;
import com.projectmate.main.entity.ProjectMember;
import com.projectmate.main.entity.ProjectRequest;
import com.projectmate.main.entity.User;
import com.projectmate.main.enums.ProjectStatus;
import com.projectmate.main.enums.RequestStatus;
import com.projectmate.main.exception.AlreadyMemberException;
import com.projectmate.main.exception.DuplicateRequestException;
import com.projectmate.main.exception.InvalidRequestException;
import com.projectmate.main.exception.ProjectClosedException;
import com.projectmate.main.exception.ProjectNotFoundException;
import com.projectmate.main.exception.TeamFullException;
import com.projectmate.main.exception.UnauthorizedProjectAccessException;
import com.projectmate.main.repository.ProfileRepository;
import com.projectmate.main.repository.ProjectMemberRepository;
import com.projectmate.main.repository.ProjectRepository;
import com.projectmate.main.repository.ProjectRequestRepository;
import com.projectmate.main.repository.UserRepository;
import com.projectmate.main.service.ProjectService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectRequestRepository projectRequestRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ProjectResponse createProject(ProjectCreateRequest request, User currentUser) {
        Project project = new Project();
        project.setOwner(currentUser);
        project.setTitle(request.getTitle());
        project.setCategory(request.getCategory());
        project.setRequiredSkills(request.getRequiredSkills());
        project.setTeamSize(request.getTeamSize());
        project.setDuration(request.getDuration());
        project.setDescription(request.getDescription());
        project.setStatus(ProjectStatus.OPEN);

        Project savedProject = projectRepository.save(project);

        ProjectMember ownerMember = new ProjectMember();
        ownerMember.setProject(savedProject);
        ownerMember.setUser(currentUser);
        projectMemberRepository.save(ownerMember);

        return toProjectResponse(savedProject);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProjectResponse> searchProjects(String category, String skill, String keyword, Pageable pageable) {
        Page<Project> projects = projectRepository.searchProjects(ProjectStatus.OPEN, category, skill, keyword, pageable);
        List<ProjectResponse> responses = new ArrayList<>();
        for (Project project : projects.getContent()) {
            responses.add(toProjectResponse(project));
        }
        return new PageImpl<>(responses, pageable, projects.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectResponse getProjectById(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found"));
        return toProjectResponse(project);
    }

    @Override
    @Transactional
    public void deleteProject(Long projectId, User currentUser) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found"));

        if (!project.getOwner().getId().equals(currentUser.getId())) {
            throw new UnauthorizedProjectAccessException("Only the project owner can delete this project");
        }

        projectRequestRepository.deleteAll(project.getRequests());
        projectMemberRepository.deleteAll(project.getMembers());
        projectRepository.delete(project);
    }

    @Override
    @Transactional
    public ProjectRequestResponse sendRequest(ProjectRequestCreateRequest request, User currentUser) {
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new ProjectNotFoundException("Project not found"));

        if (project.getOwner().getId().equals(currentUser.getId())) {
            throw new InvalidRequestException("Owner cannot send request to own project");
        }
        if (project.getStatus() != ProjectStatus.OPEN) {
            throw new ProjectClosedException("Project is closed for requests");
        }

        Optional<ProjectRequest> existing = projectRequestRepository.findByProjectAndSender(project, currentUser);
        if (existing.isPresent()) {
            throw new DuplicateRequestException("Request already exists for this project");
        }

        boolean alreadyMember = projectMemberRepository.existsByProjectAndUser(project, currentUser);
        if (alreadyMember) {
            throw new AlreadyMemberException("You are already a member of this project");
        }

        int currentMembers = (int) projectMemberRepository.countByProject(project);
        int remainingSeats = Math.max(project.getTeamSize() - currentMembers, 0);
        if (remainingSeats <= 0) {
            throw new TeamFullException("Team is already full");
        }

        ProjectRequest projectRequest = new ProjectRequest();
        projectRequest.setProject(project);
        projectRequest.setSender(currentUser);
        projectRequest.setStatus(RequestStatus.PENDING);
        ProjectRequest saved = projectRequestRepository.save(projectRequest);

        return toProjectRequestResponse(saved);
    }

    @Override
    @Transactional
    public ProjectRequestResponse acceptRequest(Long requestId, User currentUser) {
        ProjectRequest request = projectRequestRepository.findById(requestId)
                .orElseThrow(() -> new InvalidRequestException("Invalid request id"));

        Project project = request.getProject();
        if (!project.getOwner().getId().equals(currentUser.getId())) {
            throw new UnauthorizedProjectAccessException("Only the project owner can accept requests");
        }
        if (request.getStatus() != RequestStatus.PENDING) {
            throw new InvalidRequestException("Request is not pending");
        }

        int currentMembers = (int) projectMemberRepository.countByProject(project);
        int remainingSeats = Math.max(project.getTeamSize() - currentMembers, 0);
        if (remainingSeats <= 0) {
            throw new TeamFullException("Team is already full");
        }

        request.setStatus(RequestStatus.ACCEPTED);
        ProjectRequest savedRequest = projectRequestRepository.save(request);

        ProjectMember member = new ProjectMember();
        member.setProject(project);
        member.setUser(request.getSender());
        projectMemberRepository.save(member);

        long updatedMemberCount = projectMemberRepository.countByProject(project);
        if (updatedMemberCount >= project.getTeamSize()) {
            project.setStatus(ProjectStatus.CLOSED);
            projectRepository.save(project);
        }

        return toProjectRequestResponse(savedRequest);
    }

    @Override
    @Transactional
    public ProjectRequestResponse rejectRequest(Long requestId, User currentUser) {
        ProjectRequest request = projectRequestRepository.findById(requestId)
                .orElseThrow(() -> new InvalidRequestException("Invalid request id"));

        Project project = request.getProject();
        if (!project.getOwner().getId().equals(currentUser.getId())) {
            throw new UnauthorizedProjectAccessException("Only the project owner can reject requests");
        }
        if (request.getStatus() != RequestStatus.PENDING) {
            throw new InvalidRequestException("Request is not pending");
        }

        request.setStatus(RequestStatus.REJECTED);
        return toProjectRequestResponse(projectRequestRepository.save(request));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProjectResponse> getMyProjects(User currentUser, Pageable pageable) {
        List<Project> ownedProjects = projectRepository.findByOwner(currentUser);
        List<ProjectResponse> responses = new ArrayList<>();
        for (Project project : ownedProjects) {
            responses.add(toProjectResponse(project));
        }
        return new PageImpl<>(responses, pageable, responses.size());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProjectRequestResponse> getMySentRequests(User currentUser, Pageable pageable) {
        List<ProjectRequest> requests = projectRequestRepository.findBySender(currentUser);
        List<ProjectRequestResponse> responses = new ArrayList<>();
        for (ProjectRequest request : requests) {
            responses.add(toProjectRequestResponse(request));
        }
        return new PageImpl<>(responses, pageable, responses.size());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProjectRequestResponse> getReceivedRequests(User currentUser, Pageable pageable) {
        List<Project> ownedProjects = projectRepository.findByOwner(currentUser);
        List<ProjectRequest> requests = new ArrayList<>();
        if (!ownedProjects.isEmpty()) {
            requests = projectRequestRepository.findByProjectInAndStatus(ownedProjects, RequestStatus.PENDING);
        }
        List<ProjectRequestResponse> responses = new ArrayList<>();
        for (ProjectRequest request : requests) {
            responses.add(toProjectRequestResponse(request));
        }
        return new PageImpl<>(responses, pageable, responses.size());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MemberResponse> getProjectMembers(Long projectId, Pageable pageable) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found"));
        List<ProjectMember> members = projectMemberRepository.findByProject(project);
        List<MemberResponse> responses = new ArrayList<>();
        for (ProjectMember member : members) {
            responses.add(toMemberResponse(member));
        }
        return new PageImpl<>(responses, pageable, responses.size());
    }

    private ProjectResponse toProjectResponse(Project project) {
        int currentMembers = (int) projectMemberRepository.countByProject(project);
        int remainingSeats = Math.max(project.getTeamSize() - currentMembers, 0);
        int pendingRequestsCount = (int) projectRequestRepository.countByProjectAndStatus(project, RequestStatus.PENDING);

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
                .currentMembers(currentMembers)
                .remainingSeats(remainingSeats)
                .pendingRequestsCount(pendingRequestsCount)
                .createdAt(project.getCreatedAt())
                .build();
    }

    private ProjectRequestResponse toProjectRequestResponse(ProjectRequest request) {
        Project project = request.getProject();
        User sender = request.getSender();
        Profile profile = sender != null ? profileRepository.findByUser(sender).orElse(null) : null;

        return ProjectRequestResponse.builder()
                .requestId(request.getId())
                .projectId(project != null ? project.getId() : null)
                .projectTitle(project != null ? project.getTitle() : null)
                .senderId(sender != null ? sender.getId() : null)
                .senderName(sender != null ? sender.getName() : null)
                .college(profile != null ? profile.getCollege() : null)
                .skills(profile != null ? profile.getSkills() : null)
                .experience(profile != null ? profile.getExperience() : null)
                .status(request.getStatus())
                .requestDate(request.getRequestDate())
                .build();
    }

    private MemberResponse toMemberResponse(ProjectMember member) {
        User user = member.getUser();
        Profile profile = user != null ? profileRepository.findByUser(user).orElse(null) : null;
        return MemberResponse.builder()
                .userId(user != null ? user.getId() : null)
                .name(user != null ? user.getName() : null)
                .email(user != null ? user.getEmail() : null)
                .college(profile != null ? profile.getCollege() : null)
                .skills(profile != null ? profile.getSkills() : null)
                .experience(profile != null ? profile.getExperience() : null)
                .build();
    }
}
