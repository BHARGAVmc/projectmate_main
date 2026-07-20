package com.projectmate.main.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.projectmate.main.entity.Project;
import com.projectmate.main.entity.User;
import com.projectmate.main.enums.ProjectStatus;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findByStatus(ProjectStatus status);

    Page<Project> findByStatus(ProjectStatus status, Pageable pageable);

    List<Project> findByOwner(User owner);

    @Query("SELECT p FROM Project p WHERE p.status = :status AND (:category IS NULL OR p.category = :category) AND (:skill IS NULL OR LOWER(p.requiredSkills) LIKE LOWER(CONCAT('%', :skill, '%'))) AND (:keyword IS NULL OR LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) ORDER BY p.createdAt DESC")
    Page<Project> searchProjects(@Param("status") ProjectStatus status,
            @Param("category") String category,
            @Param("skill") String skill,
            @Param("keyword") String keyword,
            Pageable pageable);

    @Query("SELECT COUNT(m) FROM ProjectMember m WHERE m.project = :project")
    int countMembersByProject(@Param("project") Project project);

    Optional<Project> findByIdAndOwner(Long id, User owner);
}
