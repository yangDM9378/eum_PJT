package com.eumpyo.eum.api.controller;

import com.eumpyo.eum.api.response.UserResponse;
import com.eumpyo.eum.config.oauth2.PrincipalDetails;
import com.eumpyo.eum.db.entity.User;
import com.eumpyo.eum.db.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api")
public class TestController {
    @GetMapping("/user")
    public ResponseEntity<User> getUser(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(user);
    }
}
