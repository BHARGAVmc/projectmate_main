package com.projectmate.main.mapper;

import org.springframework.stereotype.Component;

import com.projectmate.main.dto.ProfileRequest;
import com.projectmate.main.dto.ProfileResponse;
import com.projectmate.main.entity.Profile;
import com.projectmate.main.entity.User;

@Component
public class ProfileMapper {

    public Profile toEntity(ProfileRequest request, User user) {
        Profile profile = new Profile();
        profile.setCollege(request.getCollege());
        profile.setBranch(request.getBranch());
        profile.setStudyYear(request.getYear());
        profile.setSkills(request.getSkills());
        profile.setEducation(request.getEducation());
        profile.setExperience(request.getExperience());
        profile.setLocation(request.getLocation());
        profile.setResume(request.getResume());
        profile.setAbout(request.getAbout());
        profile.setProfilePhoto(request.getProfilePhoto());
        profile.setUser(user);
        return profile;
    }

    public void updateEntity(Profile profile, ProfileRequest request) {
        profile.setCollege(request.getCollege());
        profile.setBranch(request.getBranch());
        profile.setStudyYear(request.getYear());
        profile.setSkills(request.getSkills());
        profile.setEducation(request.getEducation());
        profile.setExperience(request.getExperience());
        profile.setLocation(request.getLocation());
        profile.setResume(request.getResume());
        profile.setAbout(request.getAbout());
        profile.setProfilePhoto(request.getProfilePhoto());
    }

    public ProfileResponse toResponse(Profile profile) {
        return ProfileResponse.builder()
                .id(profile.getId())
                .college(profile.getCollege())
                .branch(profile.getBranch())
                .year(profile.getStudyYear())
                .skills(profile.getSkills())
                .education(profile.getEducation())
                .experience(profile.getExperience())
                .location(profile.getLocation())
                .resume(profile.getResume())
                .about(profile.getAbout())
                .profilePhoto(profile.getProfilePhoto())
                .build();
    }
}
