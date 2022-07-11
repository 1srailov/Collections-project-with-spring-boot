package com.itransition.final_task.controllers;
import com.itransition.final_task.dto.request.LoginRequest;
import com.itransition.final_task.dto.request.SignupRequest;
import com.itransition.final_task.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/sign-in")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        System.out.println(loginRequest);
        return userService.login(loginRequest);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        return userService.signup(signUpRequest);
    }

    @GetMapping("/check-jwt")
    public ResponseEntity<Boolean> checkUserJwt(HttpServletRequest request){
        if(request.getHeader("Authorization") == null || request.getHeader("Authorization").length() < 10){
            ResponseEntity.status(403).body(false);
        }
        return userService.getUserIdFromJwt(request.getHeader("Authorization").substring(7)) != null ?
                ResponseEntity.ok().body(true) :
                ResponseEntity.status(403).body(false);
    }
}