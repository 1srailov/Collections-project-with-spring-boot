package com.itransition.final_task.services;

import com.itransition.final_task.models.Role;
import com.itransition.final_task.models.User;
import com.itransition.final_task.dto.request.LoginRequest;
import com.itransition.final_task.dto.request.SignupRequest;
import com.itransition.final_task.dto.response.JwtResponse;
import com.itransition.final_task.dto.response.MessageResponse;
import com.itransition.final_task.repository.RoleRepository;
import com.itransition.final_task.repository.UserRepository;
import com.itransition.final_task.security.jwt.JwtUtils;
import com.itransition.final_task.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtUtils jwtUtils;
    private String username;


    public ResponseEntity<?> login(LoginRequest loginRequest){
        try {
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                    loginRequest.getPassword(), new ArrayList<>());

            authenticationManager.authenticate(authentication);

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            UserDetailsImpl userDetails = UserDetailsImpl.build
                    (userRepository.findByUsername(authentication.getPrincipal().toString()).get());

            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority).collect(Collectors.toList());

            return ResponseEntity.ok(new JwtResponse(
                    jwt, userDetails.getId(), userDetails.getUsername(),
                    userDetails.getEmail(), roles
            ));
        }catch (Exception e){

            e.printStackTrace();

            return ResponseEntity.status(403).body("valid username or password");
        }
    }


    public ResponseEntity<?> signup(SignupRequest signUpRequest){

        List<String> errors = checkUserInfo(signUpRequest.getUsername(), signUpRequest.getEmail(), signUpRequest.getRole());

        if(errors.get(0) == null){
           User user = new User(signUpRequest.getUsername(),
                   signUpRequest.getEmail(),
                   passwordEncoder.encode(signUpRequest.getPassword()));
                   userRepository.save(user);
           return ResponseEntity.ok(new MessageResponse("User registered successfully!"));

       }

       return ResponseEntity.status(401).body(errors);
    }



    public List<String> checkUserInfo(String username, String email, String role){

        List<String> errors = new ArrayList<>();

        errors.add(userRepository.existsByUsername(username) ? "Error: Username is already in use!" :
                userRepository.existsByEmail(email) ? "Error: Email is already in use!" :
                        !roleRepository.existsByName(role)? "Error: Role is not found." : null);

        return errors;
    }

    public Boolean checkIsCanEditData(Long id, String jwt){
        User user = userRepository.getUserByUsername(jwtUtils.getUserNameFromJwtToken(jwt));
        if(id == user.getId()){
            return true;
        }
        for(Role role : user.getRoles()){
            if(role.equals("ROLE_ADMIN")){
                return true;
            }
        }
        return false;
    }

    public Long getUserIdFromJwt(String jwt){
        return userRepository.getIdByUsername(jwtUtils.getUserNameFromJwtToken(jwt));
    }

}
