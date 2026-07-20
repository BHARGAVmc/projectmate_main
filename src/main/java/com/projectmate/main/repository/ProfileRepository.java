package com.projectmate.main.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projectmate.main.entity.Profile;
import com.projectmate.main.entity.User;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findByUser(User user);
    boolean existsByUser(User user);
}
