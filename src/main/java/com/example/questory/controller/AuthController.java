package com.example.questory.controller;
import com.example.questory.config.jwt.JwtProvider;
import com.example.questory.entity.UserEntity;
import com.example.questory.repository.UserEntityRepository;
import com.example.questory.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private UserEntityRepository userEntityRepository;

    @PostMapping("/register")
    public String registerUser(@RequestBody RegistrationRequest registrationRequest) {
        UserEntity u = new UserEntity();
        u.setPassword(registrationRequest.getPassword());
        u.setLogin(registrationRequest.getLogin());
        userService.saveUser(u);
        return "OK";
    }

    @PostMapping("/auth")
    public Object auth(@RequestBody AuthRequest request) {

        UserEntity userEntity = userService.findByLoginAndPassword(request.getLogin(), request.getPassword());
        if (userEntity == null) {
            return "There is no account with these parameters";
        } else {
            String token = jwtProvider.generateToken(userEntity.getLogin());
            return new AuthResponse(token);
        }
    }
}