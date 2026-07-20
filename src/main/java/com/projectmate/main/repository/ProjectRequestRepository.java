package com.projectmate.main.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projectmate.main.entity.Project;
import com.projectmate.main.entity.ProjectRequest;
import com.projectmate.main.entity.User;
import com.projectmate.main.enums.RequestStatus;

@Repository
public interface ProjectRequestRepository extends JpaRepository<ProjectRequest, Long> {

    List<ProjectRequest> findBySender(User sender);

    List<ProjectRequest> findByProjectOwner(User owner);

    List<ProjectRequest> findByProjectAndStatus(Project project, RequestStatus status);

    List<ProjectRequest> findByProjectInAndStatus(List<Project> projects, RequestStatus status);

    Optional<ProjectRequest> findByProjectAndSender(Project project, User sender);

    boolean existsByProjectAndSenderAndStatus(Project project, User sender, RequestStatus status);

    long countByProjectAndStatus(Project project, RequestStatus status);
}
