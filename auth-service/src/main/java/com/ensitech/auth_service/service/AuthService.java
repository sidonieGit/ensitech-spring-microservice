package com.ensitech.auth_service.service;

import com.ensitech.auth_service.model.dto.UserLoginDto;
import com.ensitech.auth_service.model.dto.UserRegisterDto;
import com.ensitech.auth_service.model.dto.UpdatePasswordDto;
import com.ensitech.auth_service.model.dto.UserDto;
import com.ensitech.auth_service.model.entity.Role;
import com.ensitech.auth_service.model.entity.User;
import com.ensitech.auth_service.model.mapper.UserMapper;
import com.ensitech.auth_service.repository.UserRepository;
import com.ensitech.auth_service.security.jwt.JwtUtil;
import com.ensitech.auth_service.service.common.IAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;

    public String register(UserRegisterDto req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new RuntimeException("Email attribué à un autre utilisateur");
            // return "Email déjà utilisé";
        }


        User user = new User();
        // user.setUsername(req.getUsername());
        user.setEmail(req.getEmail());
        user.setPassword(encoder.encode(req.getPassword()));
        user.setRole(Role.valueOf(req.getRole()));

        userRepository.save(user);
        return "Utilisateur enregistré avec succès";
    }


    /* public Optional<UserDto> login(UserLoginDto req) {
        return userRepository.findByEmail(req.getEmail())
                .filter(user -> encoder.matches(req.getPassword(), user.getPassword()))
                .map(userMapper::toUserDto);
    } */
    // public String login(UserLoginDto userLoginDto) {
    public Optional<UserDto>  login(UserLoginDto userLoginDto) {
        // User user = userRepository.findByEmail(userLoginDto.getEmail()).orElseThrow();
        User user = userRepository.findByEmail(userLoginDto.getEmail()).orElseThrow(() -> new UsernameNotFoundException("Email incorrect: "));

        if (!encoder.matches(userLoginDto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Mot de passe incorrect");
        }
       String token=jwtUtil.generateToken(user);
        user.setTokenUser(token);

        return Optional.ofNullable(userMapper.toUserDto(user));
    }


    public String updateEmail(Long userId, String newEmail) {
        if (userRepository.existsByEmail(newEmail))  throw new RuntimeException("Email déjà attribué à un utilisateur");// return "Email déjà pris";
        User user = userRepository.findById(userId).orElseThrow();
        // User user = userRepository.findById(userId)   .orElseThrow(() -> new RuntimeException("Identifiants incorrects"));;
        user.setEmail(newEmail);
        userRepository.save(user);
        return "Email mis à jour";
    }

    public String updatePassword(Long userId, UpdatePasswordDto request) {
        User user = userRepository.findById(userId).orElseThrow(()->  new RuntimeException("Utilisateur introuvable"));

        if (!encoder.matches(request.getOldPassword(), user.getPassword())) {
           // return "Ancien mot de passe incorrect";
            throw new RuntimeException( "Ancien mot de passe incorrect");
        }
        user.setPassword(encoder.encode(request.getNewPassword()));
        userRepository.save(user);
        return "Mot de passe mis à jour";
    }
}
