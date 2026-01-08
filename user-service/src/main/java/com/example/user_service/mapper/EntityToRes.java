package com.example.user_service.mapper;

import com.example.user_service.entity.User;
import com.example.user.UserResponse;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
public class EntityToRes {

    public static UserResponse convertEntityToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .cabinetId(user.getCabinetId())
                .login(user.getLogin())
                .nom(user.getNom())
                .prenom(user.getPrenom())
                .numTel(user.getNumTel())
                .signature(user.getSignature())
                .role(user.getRole().toString())
                .build();
    }

}
