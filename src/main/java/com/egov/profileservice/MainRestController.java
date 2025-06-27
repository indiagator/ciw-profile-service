package com.egov.profileservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@RestController
@RequestMapping("api/v1")
public class MainRestController
{
    private static final Logger logger = LoggerFactory.getLogger(MainRestController.class);

    @Autowired
    ProfileRepository profileRepository;

    @Autowired
    TokenService tokenService;

    @PostMapping("save/profile")
    public ResponseEntity<?> saveProfile(@RequestHeader("Authorization") String token,
                                         @RequestBody Profile profile)
    {
        logger.info("Received request to save profile: " + profile.toString());
        String phone = null;

        try
        {
             phone =  tokenService.validateToken(token);
        }
        catch (WebClientResponseException e)
        {
            logger.info("Token validation failed: " + e.getMessage());
            return ResponseEntity.status(401).body("Invalid token");
        }


        logger.info("Phone number from token: " + phone);
        if(!phone.equals(profile.getPhone()))
        {
            logger.info("Phone number mismatch");
            return ResponseEntity.status(401).body("Invalid token or phone number mismatch");
        }

        logger.info("Phone number matches, proceeding to save profile");
        profileRepository.save(profile);
        logger.info("Profile saved successfully");
        // Logic to save the profile
        return ResponseEntity.ok("Profile saved successfully");
    }
}
