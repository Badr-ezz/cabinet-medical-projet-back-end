package com.example.user_service.service.jwtServices;

import com.example.user_service.config.CustomUserDetails;
import com.example.user_service.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepo userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepo userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        System.out.println(">> Recherche de l'utilisateur avec email: " + login);

        return userRepository.findByLogin(login)
                .map(user -> {
                    System.out.println(">> Utilisateur trouvé : " + user.getLogin());
                    return new CustomUserDetails(user);
                })
                .orElseThrow(() -> {
                    System.out.println(">> UTILISATEUR NON TROUVÉ pour : " + login);
                    return new UsernameNotFoundException("User not found with login: " + login);
                });
    }

}