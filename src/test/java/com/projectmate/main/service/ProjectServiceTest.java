package com.projectmate.main.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.projectmate.main.dto.ProjectCreateRequest;
import com.projectmate.main.dto.ProjectResponse;
import com.projectmate.main.entity.User;
import com.projectmate.main.repository.ProjectMemberRepository;
import com.projectmate.main.repository.ProjectRepository;
import com.projectmate.main.repository.ProjectRequestRepository;
import com.projectmate.main.repository.UserRepository;

@SpringBootTest(properties = "spring.config.location=classpath:/application-test.properties")
@Transactional
class ProjectServiceTest {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectMemberRepository projectMemberRepository;

    @Autowired
    private ProjectRequestRepository projectRequestRepository;

    private User owner;

    @BeforeEach
    void setUp() {
        projectRequestRepository.deleteAll();
        projectMemberRepository.deleteAll();
        projectRepository.deleteAll();
        userRepository.deleteAll();

        owner = new User();
        owner.setName("Owner");
        owner.setEmail("owner@example.com");
        owner.setPassword("secret");
        owner = userRepository.save(owner);
    }

    @Test
    void shouldPublishProjectAndCreateOwnerMembership() {
        ProjectCreateRequest request = new ProjectCreateRequest();
        request.setTitle("AI Study Group");
        request.setCategory("AI");
        request.setRequiredSkills("Java, Spring Boot");
        request.setTeamSize(3);
        request.setDuration("3 months");
        request.setDescription("We are building a collaborative AI study project for learners.");

        ProjectResponse response = projectService.createProject(request, owner);

        assertThat(response).isNotNull();
        assertThat(response.getTitle()).isEqualTo("AI Study Group");
        assertThat(projectRepository.findById(response.getId())).isPresent();
        assertThat(projectMemberRepository.existsByProjectAndUser(projectRepository.findById(response.getId()).get(), owner)).isTrue();
    }
}
