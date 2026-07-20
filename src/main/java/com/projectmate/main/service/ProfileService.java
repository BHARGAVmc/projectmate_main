package com.projectmate.main.service;

import com.projectmate.main.dto.ProfileRequest;
import com.projectmate.main.dto.ProfileResponse;
import com.projectmate.main.entity.User;

public interface ProfileService {
    ProfileResponse createProfile(ProfileRequest request, User currentUser);
    ProfileResponse getProfile(User currentUser);
    ProfileResponse updateProfile(ProfileRequest request, User currentUser);
    void deleteProfile(User currentUser);
}
