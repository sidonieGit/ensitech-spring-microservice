package com.project.ensitech.authentication_service.service.implementation;

import com.project.ensitech.authentication_service.config.security.jwt.JwtUtil;
import com.project.ensitech.authentication_service.model.dto.AuthenticationResponse;
import com.project.ensitech.authentication_service.model.dto.LoginRequest;
import com.project.ensitech.authentication_service.model.dto.RegisterRequest;
import com.project.ensitech.authentication_service.model.entity.User;
import com.project.ensitech.authentication_service.model.enumeration.Role;
import com.project.ensitech.authentication_service.repository.UserRepository;
import com.project.ensitech.authentication_service.service.common.IAuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements IAuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    @Override
   // public AuthenticationResponse register(RegisterRequest request) {
    public String register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email attribué à un autre utilisateur");

        }
        var user = User.builder()
                .email(request.getEmail())
                .role(Role.valueOf(request.getRole()))
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        userRepository.save(user);
        /*var jwtToken = jwtUtil.generateToken(user);
        return AuthenticationResponse.builder()
                .id(r)
                .token(jwtToken)
                .build();*/
        return "Utilisateur enregistré avec succès";
    }

    @Override
    public AuthenticationResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Email incorrect"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Mot de passe incorrect");
        }
        //user.get
        var jwtToken = jwtUtil.generateToken(user);
        return AuthenticationResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .token(jwtToken)
                .build();
    }
}
