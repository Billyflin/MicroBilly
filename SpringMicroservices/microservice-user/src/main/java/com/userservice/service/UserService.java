package com.userservice.service;

import com.userservice.configuration.security.jwt.JwtUtil;
import com.userservice.exceptions.AuthException;
import com.userservice.model.Role;
import com.userservice.model.User;
import com.userservice.model.dto.LoginRequest;
import com.userservice.model.dto.LoginResponse;
import com.userservice.model.dto.UserDTO;
import com.userservice.model.dto.UserSignUpDTO;
import com.userservice.repository.RoleRepository;
import com.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public List<UserDTO> findAll(){
        return userRepository.findAll().stream()
                .map(user ->
                        UserDTO.builder()
                                .username(user.getUsername())
                                .roles(user.getRoles().stream()
                                        .map(Role::getName).collect(Collectors.toSet()))
                                .build())
                .toList();
    }

    public UserDTO save(UserSignUpDTO user){
        Role basicUserRole = roleRepository.findByName("USER").orElse(null);
        assert basicUserRole != null;
        User newUser =
                userRepository.save(
                        User.builder()
                                .username(user.getUsername())
                                .password(passwordEncoder.encode(user.getPassword()))
                                .roles(Set.of(basicUserRole))
                                .build()
                );
        return UserDTO.builder()
                .username(newUser.getUsername())
                .roles(newUser.getRoles().stream()
                        .map(Role::getName).collect(Collectors.toSet()))
                .build();
    }

    public LoginResponse login(LoginRequest loginRequest){
        try {
            UsernamePasswordAuthenticationToken userLogin =
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
            UserDetails authenticatedUser = (UserDetails) authenticationManager.authenticate(userLogin).getPrincipal();
            return LoginResponse.builder()
                            .access_token(jwtUtil.generateToken(authenticatedUser))
                            .build();
        } catch (AuthenticationException ex) {
            throw new AuthException("Login failed. wrong credentials");
        }
    }

    public UserDTO validateToken(String token) {
        User user = jwtUtil.validateToken(token);
        return UserDTO.builder()
                .username(user.getUsername())
                .roles(user.getRoles().stream()
                        .map(Role::getName).collect(Collectors.toSet()))
                .build();
    }
}
