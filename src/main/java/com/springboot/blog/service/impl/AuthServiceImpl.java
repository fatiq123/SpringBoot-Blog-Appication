//package com.springboot.blog.service.impl;
//
//import com.springboot.blog.entity.Role;
//import com.springboot.blog.entity.User;
//import com.springboot.blog.exception.BlogAPIException;
//import com.springboot.blog.payload.LoginDto;
//import com.springboot.blog.payload.RegisterDto;
//import com.springboot.blog.repository.RoleRepository;
//import com.springboot.blog.repository.UserRepository;
//import com.springboot.blog.security.JwtTokenProvider;
//import com.springboot.blog.service.AuthService;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import java.util.HashSet;
//import java.util.Set;
//
//@Service
//public class AuthServiceImpl implements AuthService {
//
//
//    // Remember we are coding our own authentication for auth.
//    // that is why we are injecting authenticationManager interface
//    // and it calls DAO authentication provider for database authentication.
//    // this is for login as we are matching existing data from database to new User entered data.
//    // and check if matches or not
//    private AuthenticationManager authenticationManager;
//
//    // Note for registering a new User we need following dependencies to inject
//    private UserRepository userRepository;
//    private RoleRepository roleRepository;
//    private PasswordEncoder passwordEncoder;
//
//    // for jwt
//    private JwtTokenProvider jwtTokenProvider;
//
//    public AuthServiceImpl(AuthenticationManager authenticationManager,
//                           UserRepository userRepository,
//                           RoleRepository roleRepository,
//                           PasswordEncoder passwordEncoder,
//                           JwtTokenProvider jwtTokenProvider) {
//        this.authenticationManager = authenticationManager;
//        this.userRepository = userRepository;
//        this.roleRepository = roleRepository;
//        this.passwordEncoder = passwordEncoder;
//        this.jwtTokenProvider = jwtTokenProvider;
//    }
//
//    @Override
//    public String login(LoginDto loginDto) {
//
//        Authentication authentication = authenticationManager.authenticate(new
//                UsernamePasswordAuthenticationToken(
//                        loginDto.getUsernameOrEmail(), loginDto.getPassword())
//        );
//
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        String token = jwtTokenProvider.generateToken(authentication);
//
//        return token;
//    }
//
//
//    @Override
//    public String register(RegisterDto registerDto) {
//
//        // add check for username already exists in database
//        if (userRepository.existsByUsername(registerDto.getUsername())) {
//            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "email already exists");
//        }
//
//        // add check for email already exists in database
//        if (userRepository.existsByEmail(registerDto.getEmail())) {
//            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "email already exists");
//        }
//
//        // Make new Object of User and get data from registerDto and set it to User Object
//        User user = new User();
//        user.setName(registerDto.getName());
//        user.setUsername(registerDto.getUsername());
//        user.setEmail(registerDto.getEmail());
//        // encode password to Base64 and set it to database
//        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
//
//        Set<Role> roles = new HashSet<>();
//        // Here is logic that when a new User Registers then by default he will get role as ROLE_USER
//        Role userRole = roleRepository.findByName("ROLE_USER").get();
//        roles.add(userRole);
//        user.setRoles(roles);
//
//        userRepository.save(user);
//
//        return "User registered successfully";
//    }
//}




package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Role;
import com.springboot.blog.entity.User;
import com.springboot.blog.exception.BlogAPIException;
import com.springboot.blog.payload.LoginDto;
import com.springboot.blog.payload.RegisterDto;
import com.springboot.blog.repository.RoleRepository;
import com.springboot.blog.repository.UserRepository;
import com.springboot.blog.security.JwtTokenProvider;
import com.springboot.blog.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider jwtTokenProvider;

    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public String login(LoginDto loginDto) {
        // Input validation for login credentials
        if (loginDto.getUsernameOrEmail() == null || loginDto.getPassword() == null) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Username/email and password are required");
        }

        try {
            Authentication authentication = authenticationManager.authenticate(new
                    UsernamePasswordAuthenticationToken(
                    loginDto.getUsernameOrEmail(), loginDto.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtTokenProvider.generateToken(authentication);
            return token;
        } catch (AuthenticationException e) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Invalid username/email or password");
        }
    }

    @Override
    public String register(RegisterDto registerDto) {
        // Input validation for required fields
        if (registerDto.getUsername() == null || registerDto.getEmail() == null ||
                registerDto.getPassword() == null || registerDto.getName() == null) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "All fields (username, email, password, name) are required");
        }

        // Check for username already exists in database
        if (userRepository.existsByUsername(registerDto.getUsername())) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Username already exists!");
        }

        // Check for email already exists in database
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Email already exists!");
        }

        // Create new User object and set data from registerDto
        User user = new User();
        user.setName(registerDto.getName());
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

// Create a new HashSet to store user roles
        Set<Role> roles = new HashSet<>();

// Default role is set to "ROLE_USER"
// If registerDto contains ADMIN role and matches case-insensitive "ADMIN",
// then set role to "ROLE_ADMIN" instead
        String roleName = "ROLE_USER";
        if (registerDto.getRole() != null && "ADMIN".equalsIgnoreCase(registerDto.getRole())) {
            roleName = "ROLE_ADMIN";
        }

// Look up the role in the database by name
// If role is not found, throw an error
        Optional<Role> userRole = roleRepository.findByName(roleName);
        if (!userRole.isPresent()) {
            throw new BlogAPIException(HttpStatus.INTERNAL_SERVER_ERROR, "Role not found: " + roleName);
        }

// Add the found role to the roles set
        roles.add(userRole.get());

// Set the roles on the user object
        user.setRoles(roles);

// Save the user to the database
        userRepository.save(user);
        return "User registered successfully";
    }
}