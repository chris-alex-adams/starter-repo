package au.com.rmcc.dataservice.controller;

import java.net.URI;
import java.util.Collections;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.google.gson.Gson;

import au.com.anymoove.common.model.Role;
import au.com.anymoove.common.model.User;
import au.com.anymoove.common.model.Role.RoleName;
import au.com.anymoove.common.repository.UserRepository;
import au.com.rmcc.dataservice.model.ApiResponse;
import au.com.rmcc.dataservice.model.JwtAuthenticationResponse;
import au.com.rmcc.dataservice.model.LoginRequest;
import au.com.rmcc.dataservice.model.SignUpRequest;
import au.com.rmcc.dataservice.model.UserInfoResponse;
import au.com.rmcc.dataservice.model.UserUpdateRequest;
import au.com.rmcc.dataservice.repository.RoleRepository;
import au.com.rmcc.dataservice.security.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/auth")
@CrossOrigin
@Slf4j
public class AuthenticationController {
	
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider tokenProvider;
    
	@RequestMapping("/health")
	public String getHealth() {
		return "Everything is fine!";
	}
    
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
    	log.info("login request: {}", loginRequest);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );
        
        log.info("authentication details: {}", authentication.getDetails());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }
    
    @PostMapping("/updateUser")
    public ResponseEntity<?> updateUserDetails(@Valid @RequestBody UserUpdateRequest updateRequest) {
    	log.info("user update request: {}", updateRequest);
        if(!userRepository.existsByUsername(updateRequest.getOldUsername())) {
        	log.info("Username doesn't exist!");
            return new ResponseEntity(new ApiResponse(false, "Username doesn't exist!"),
                    HttpStatus.BAD_REQUEST);
        }
        
        User user = userRepository.findByUsername(updateRequest.getOldUsername());
        user.setUsername(updateRequest.getUsername());
        user.setPassword(passwordEncoder.encode(updateRequest.getNewPassword()));
        user.setEmail(updateRequest.getEmail());
        userRepository.save(user);
        
	    Gson gson = new Gson();
	    String json = gson.toJson("Successfully updated the profile!");
		
		return ResponseEntity.ok(json);
    }
    
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
    	log.info("signup request: {}", signUpRequest);
        if(userRepository.existsByUsername(signUpRequest.getUsername())) {
        	log.info("Username is already taken!");
            return new ResponseEntity(new ApiResponse(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
        	log.info("Email Address already in use!");
            return new ResponseEntity(new ApiResponse(false, "Email Address already in use!"),
                    HttpStatus.BAD_REQUEST);
        }

        // Creating user's account
        User user = new User(signUpRequest.getName(), signUpRequest.getUsername(),
                signUpRequest.getEmail(), signUpRequest.getPassword());

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        //Role userRole = roleRepository.findByName(RoleName.ROLE_USER.name());
        //if(userRole == null) {
        //	throw new AppExcpetion("User Role not set.");
        //}

        user.setRoles(Collections.singleton(new Role(12345L, RoleName.ROLE_USER)));

        User result = userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/users/{username}")
                .buildAndExpand(result.getUsername()).toUri();
        log.info("User registered successfully");
        LoginRequest lr = new LoginRequest(signUpRequest.getUsername(), signUpRequest.getPassword());
        ResponseEntity<?> loginResponse = authenticateUser(lr);
        
        
        return ResponseEntity.created(location).body(loginResponse.getBody());
    }
    
    @PostMapping("/username")
    public ResponseEntity<?> getUsername(@RequestBody JwtAuthenticationResponse jwtResponse) {
    	if(jwtResponse.getAccessToken().trim().isEmpty()) {
    		return new ResponseEntity(new ApiResponse(false, "access token is blank"),
                    HttpStatus.BAD_REQUEST);
    	} else {
    		log.info("access token: {}", jwtResponse);
    		String username = tokenProvider.getUserIdFromJWT(jwtResponse.getAccessToken());
    		if(username.trim().isEmpty()) {
    			return new ResponseEntity(new ApiResponse(false, "username is invalid"),
                        HttpStatus.BAD_REQUEST);
    		}
    	    Gson gson = new Gson();
    	    String json = gson.toJson(username);
    		
    		return ResponseEntity.ok(json);
    	}
    }
    
    @PostMapping("/user")
    public ResponseEntity<?> getUserInformation(@RequestBody JwtAuthenticationResponse jwtResponse) {
    	if(jwtResponse.getAccessToken().trim().isEmpty()) {
    		return new ResponseEntity(new ApiResponse(false, "access token is blank"),
                    HttpStatus.BAD_REQUEST);
    	} else {
    		log.info("access token: {}", jwtResponse);
    		String username = tokenProvider.getUserIdFromJWT(jwtResponse.getAccessToken());
    		if(username.trim().isEmpty()) {
    			return new ResponseEntity(new ApiResponse(false, "username is invalid"),
                        HttpStatus.BAD_REQUEST);
    		}
    		Optional<User> user = userRepository.findById(username);
    		if(user.isPresent()) {
        		UserInfoResponse res = new UserInfoResponse();
        		res.setEmail(user.get().getEmail());
        		res.setUsername(user.get().getUsername());
        	    Gson gson = new Gson();
        	    String json = gson.toJson(res);
        		
        		return ResponseEntity.ok(json);
    		}
    		return new ResponseEntity(new ApiResponse(false, "couldn't find the user"),
                    HttpStatus.BAD_REQUEST);
    	}
    }

}
