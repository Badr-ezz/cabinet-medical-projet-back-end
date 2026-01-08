package com.example.user_service.service.loginServices;

import com.example.user_service.config.CustomUserDetails;
import com.example.user_service.entity.User;
import com.example.user_service.exception.AppException;
import com.example.user_service.mapper.EntityToRes;
import com.example.user_service.mapper.ReqToEntity;
import com.example.user_service.repository.UserRepo;
import com.example.user_service.request.UserRequest;
import com.example.user_service.response.AuthResponse;
import com.example.user.UserResponse;
import com.example.user_service.service.jwtServices.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;


@Slf4j
@Service
public class LoginServicesImpl implements LoginServices {

    private final UserRepo userRepository;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public LoginServicesImpl(UserRepo userRepository,
                             PasswordEncoder passwordEncoder,
                             JwtUtils jwtUtils,
                             AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder1) {

        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder1;
    }

    @Override
    public AuthResponse authenticate(String login, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, password));
        } catch (BadCredentialsException e) {
            return AuthResponse.builder()
                    .token(null)
                    .tokenExpired(false)
                    .error("Login ou mot de passe incorrect")
                    .userRole(null)
                    .build();
        }

        User user = userRepository.findByLogin(login).orElse(null);
        if (user == null) {
            return AuthResponse.builder()
                    .token(null)
                    .tokenExpired(false)
                    .error("Utilisateur introuvable")
                    .userRole(null)
                    .build();
        }

        // must add the cabinet check (actif / inactif)
//        if (user.getStatus() != Status.ACTIF) {
//            throw new AppException("Compte non activé", HttpStatus.FORBIDDEN, "ERR_ACCOUNT_NOT_ACTIVE");
//        }
        UserDetails userDetails = new CustomUserDetails(user);
        String token = jwtUtils.generateToken(userDetails, user.getId());

        return AuthResponse.builder()
                .token(token)
                .tokenExpired(false)
                .error(null)
                .userRole(user.getRole().name())
                .build();
    }

    @Override
    public UserResponse register(UserRequest request) {
        if (userRepository.findByLogin(request.getLogin()).isPresent()) {
            throw new AppException("Utilisateur déjà existant ", HttpStatus.CONFLICT, "ERR_EMAIL_EXISTS");
        }

        User user = ReqToEntity.convertUserRequestToUser(request);
        user.setPwd(passwordEncoder.encode(request.getPwd()));

        return EntityToRes.convertEntityToResponse(userRepository.save(user));
    }

//    @Override
//    public AuthResponse checkTokenValidity(String token) {
//        AuthResponse authResponse = new AuthResponse();
//        try {
//            if (!jwtUtils.isTokenExpired(token)) {
//                String role = jwtUtils.extractRole(token);
//
//                if (List.of("MEDECIN", "ADMIN", "SECRETARY").contains(role)) {
//                    authResponse.setToken(token);
//                    authResponse.setTokenExpired(false);
//                    authResponse.setError(null);
//                    authResponse.setUserRole(role);
//                    return authResponse;
//                }
//            }
//            return authResponse;
//        } catch (Exception e) {
//            return authResponse;
//        }
//    }

    @Override
    public AuthResponse checkTokenValidity(String token) {
        try {
            // Vérifier si token expiré
            if (jwtUtils.isTokenExpired(token)) {
                return AuthResponse.builder()
                        .token(token)
                        .tokenExpired(true)
                        .error("Token expiré")
                        .userRole(null)
                        .build();
            }

            // Token valide → extraire le rôle
            String role = jwtUtils.extractRole(token);

                // Retourner le rôle (PAS de validation du rôle ici)
            return AuthResponse.builder()
                    .token(token)
                    .tokenExpired(false)
                    .error(null)
                    .userRole(role)  // Retourne le rôle, peu importe lequel
                    .build();

        } catch (Exception e) {
            log.error("Erreur validation token: {}", e.getMessage());
            return AuthResponse.builder()
                    .token(token)
                    .tokenExpired(false)
                    .error("Token invalide ou malformé: " + e.getMessage())
                    .userRole(null)
                    .build();
        }
    }

}
