package com.projectmate.main.service.impl;

import org.springframework.stereotype.Service;

import com.projectmate.main.dto.ProfileRequest;
import com.projectmate.main.dto.ProfileResponse;
import com.projectmate.main.entity.Profile;
import com.projectmate.main.entity.User;
import com.projectmate.main.exception.ResourceNotFoundException;
import com.projectmate.main.mapper.ProfileMapper;
import com.projectmate.main.repository.ProfileRepository;
import com.projectmate.main.service.ProfileService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;

    @Override
    public ProfileResponse createProfile(ProfileRequest request, User currentUser) {
        if (profileRepository.existsByUser(currentUser)) {
            throw new IllegalStateException("Profile already exists");
        }
        Profile profile = profileMapper.toEntity(request, currentUser);
        Profile savedProfile = profileRepository.save(profile);
        return profileMapper.toResponse(savedProfile);
    }

    @Override
    public ProfileResponse getProfile(User currentUser) {
        Profile profile = profileRepository.findByUser(currentUser)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));
        return profileMapper.toResponse(profile);
    }

    @Override
    public ProfileResponse updateProfile(ProfileRequest request, User currentUser) {
        Profile profile = profileRepository.findByUser(currentUser)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));
        profileMapper.updateEntity(profile, request);
        return profileMapper.toResponse(profileRepository.save(profile));
    }

    @Override
    public void deleteProfile(User currentUser) {
        Profile profile = profileRepository.findByUser(currentUser)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));
        profileRepository.delete(profile);
    }
}
