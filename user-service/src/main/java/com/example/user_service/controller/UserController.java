package com.example.user_service.controller;

import com.example.user_service.entity.User;
import com.example.user_service.request.UserRequest;
import com.example.user.UserResponse;
import com.example.user_service.service.userServices.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin("*")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> addUser(@RequestBody UserRequest userReq) {
        try {
            UserResponse res = userService.addUser(userReq);
            return ResponseEntity.status(HttpStatus.CREATED).body(res);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erreur dans recuperation de l'utilisateur : " + e.getMessage());
        }
    }

    @GetMapping("/byLogin/{login}")
    public ResponseEntity<UserResponse> getUserByLogin(@PathVariable("login") String login) {
        try {
            return ResponseEntity.ok(userService.getUserByLogin(login));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping()
    public ResponseEntity<?> updateUser(@RequestBody UserRequest userReq) {
        try {
            UserResponse res = userService.updateUser(userReq);
            return ResponseEntity.status(HttpStatus.OK).body(res);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erreur dans la modification  de l'utilisateur" + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Erreur dans la supression  de l'utilisateur" + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable long id) {
        try {
            UserResponse res = userService.getUser(id);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Erreur dans la recherche  de l'utilisateur" + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        try {
            List<UserResponse> res = userService.getAllUsers();
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur dans la recherche  des l'utilisateurs" + e.getMessage());
        }
    }


    @GetMapping("/byCabinet/{cabinetId}")
    public ResponseEntity<?> getUsersByCabinetId(@PathVariable Long cabinetId) {
            try {
                List<UserResponse> res = userService.getUsersByCabinetId(cabinetId);
                return ResponseEntity.ok(res);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Erreur dans la recherche  des l'utilisateurs par cabinetId" + e.getMessage());
            }
    }
}

