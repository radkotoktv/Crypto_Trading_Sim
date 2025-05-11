package com.example.demo.account.profile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ProfileController {
    @Autowired
    ProfileService profileService;
    @Autowired
    ProfileRepository profileRepository;

    @PostMapping("/create")
    public ResponseEntity<ProfileDTO> createProfile(@RequestBody ProfileCreateRequestDTO dto) {
        ProfileDTO createdProfile = profileService.createProfile(dto);
        if (createdProfile != null) {
            return ResponseEntity.ok(createdProfile);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<ProfileDTO> getProfileById(@PathVariable Long id) {
        Optional<ProfileDTO> profile = profileService.getProfileById(id);
        return profile.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ProfileDTO> getProfileByName(@PathVariable String name) {
        Optional<ProfileDTO> profile = profileService.getProfileByName(name);
        return profile.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest request) {
        Optional<Profile> profileOpt = profileRepository.findByName(request.getUsername());

        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            if (profile.getPassword_hash().equals(request.getPassword())) {
                Map<String, Object> response = new HashMap<>();
                response.put("username", profile.getUsername());
                response.put("email", profile.getEmail());
                response.put("id", profile.getId());
                response.put("token", profile.getUsername() + "-" + System.currentTimeMillis());
                return ResponseEntity.ok(response);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
