package com.example.user_service.service.userServices;

import com.example.user_service.entity.User;
import com.example.user_service.mapper.EntityToRes;
import com.example.user_service.mapper.ReqToEntity;
import com.example.user_service.repository.UserRepo;
import com.example.user_service.request.UserRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.user.UserResponse;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public UserResponse addUser(UserRequest userReq) {
        try {
            User newUser = ReqToEntity.convertUserRequestToUser(userReq);
            Optional<User> user = userRepo.findByLogin(userReq.getLogin());
            if (user.isPresent()) {
                throw new RuntimeException("Utilisateur déj<UNK> enregistr<UNK>");
            }
            newUser.setPwd(passwordEncoder.encode(userReq.getPwd()));
            newUser = userRepo.save(newUser);
            log.info("user added: " + newUser.toString());
            return EntityToRes.convertEntityToResponse(newUser);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public UserResponse updateUser(UserRequest userReq) {
        try {

            User targetUser = ReqToEntity.convertUserRequestToUser(userReq);
            Optional<User> user = userRepo.findById(targetUser.getId());
            if (user.isPresent()) {
                targetUser.setPwd(passwordEncoder.encode(userReq.getPwd()));
                targetUser = userRepo.save(targetUser);
                return EntityToRes.convertEntityToResponse(targetUser);
            } else {
                // add an exception for notfoundUser
                return null;
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public void deleteUser(long id) {
        try {
            Optional<User> user = userRepo.findById(id);
            user.ifPresent(userRepo::delete);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserResponse getUser(long id) {
        try {
            Optional<User> user = userRepo.findById(id);
            return user.map(EntityToRes::convertEntityToResponse).orElse(new UserResponse());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<UserResponse> getAllUsers() {
        try {
            List<User> users = userRepo.findAll();
            return users.stream()
                    .map(EntityToRes::convertEntityToResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserResponse getUserByLogin(String login) {

        try {
            User user = userRepo.findByLogin(login).orElse(User.builder().build());
            return EntityToRes.convertEntityToResponse(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
