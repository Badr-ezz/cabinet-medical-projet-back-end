package com.example.user_service.mapper;


import com.example.user_service.entity.User;
import com.example.user_service.request.UserRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ReqToEntity {
    public static User convertUserRequestToUser(UserRequest userReq) {
        return User.builder()
                .id(userReq.getId())
                .login(userReq.getLogin())
                .pwd(userReq.getPwd())
                .nom(userReq.getNom())
                .prenom(userReq.getPrenom())
                .numTel(userReq.getNumTel())
                .signature(userReq.getSignature())
                .role(userReq.getRole())
                .build();
    }
}
