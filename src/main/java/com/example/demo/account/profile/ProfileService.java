package com.example.demo.account.profile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfileService {
    @Autowired
    ProfileRepository profileRepository;

    public ProfileDTO createProfile(ProfileCreateRequestDTO dto) {
        Profile profile = new Profile(dto.getUsername(), dto.getPassword_hash(), dto.getEmail(), dto.getCreated_at());
        boolean isSaved = profileRepository.save(profile);
        if (isSaved) {
            return new ProfileDTO(dto.getUsername(), dto.getEmail(), dto.getCreated_at());
        } else {
            return null;
        }
    }

    public Optional<ProfileDTO> getProfileById(Long id) {
        Optional<Profile> profile = profileRepository.findById(id);
        return profile.map(p -> {
            ProfileDTO dto = new ProfileDTO();
            dto.setId(p.getId());
            dto.setUsername(p.getUsername());
            dto.setEmail(p.getEmail());          // Map only safe fields
            dto.setCreated_at(p.getCreated_at());
            return dto;
        });
    }

    public Optional<ProfileDTO> getProfileByName(String name) {
        Optional<Profile> profile = profileRepository.findByName(name);
        return profile.map(p -> {
            ProfileDTO dto = new ProfileDTO();
            dto.setId(p.getId());
            dto.setUsername(p.getUsername());
            dto.setEmail(p.getEmail());
            dto.setCreated_at(p.getCreated_at());
            return dto;
        });
    }
}
