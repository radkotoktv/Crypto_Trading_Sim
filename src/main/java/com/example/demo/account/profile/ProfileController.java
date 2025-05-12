package com.example.demo.account.profile;

import com.example.demo.account.balance.Balance;
import com.example.demo.account.balance.BalanceRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.example.demo.account.profile.ProfileConstants.STARTING_BALANCE;

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
            Balance balance = new Balance(createdProfile.getId(), STARTING_BALANCE);
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
