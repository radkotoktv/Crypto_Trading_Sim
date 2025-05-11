package com.example.demo.account.profile;

import com.example.demo.account.balance.Balance;
import com.example.demo.account.balance.BalanceRepository;
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
    @Autowired
    BalanceRepository balanceRepository;

    @PostMapping("/create")
    public ProfileDTO createProfile(@RequestBody ProfileCreateRequestDTO dto) {
        ProfileDTO createdProfile = profileService.createProfile(dto);
        if (createdProfile != null) {
            Balance balance = new Balance(createdProfile.getId(), 10000.0);
            balanceRepository.save(balance);
            return createdProfile;
        } else {
            return null;
        }
    }

    @GetMapping("/id/{id}")
    public ProfileDTO getProfileById(@PathVariable Long id) {
        Optional<ProfileDTO> profile = profileService.getProfileById(id);
        return profile.orElse(null);
    }

    @GetMapping("/name/{name}")
    public ProfileDTO getProfileByName(@PathVariable String name) {
        Optional<ProfileDTO> profile = profileService.getProfileByName(name);
        return profile.orElse(null);
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody LoginRequest request) {
        Optional<Profile> profileOpt = profileRepository.findByName(request.getUsername());

        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            if (profile.getPassword_hash().equals(request.getPassword())) {
                Map<String, Object> response = new HashMap<>();
                response.put("username", profile.getUsername());
                response.put("email", profile.getEmail());
                response.put("id", profile.getId());
                response.put("token", profile.getUsername() + "-" + System.currentTimeMillis());
                return response;
            }
        }
        return null;
    }
}
