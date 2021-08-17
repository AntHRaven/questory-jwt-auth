package com.example.questory.service;
import com.example.questory.entity.RoleEntity;
import com.example.questory.entity.UserEntity;
import com.example.questory.repository.RoleEntityRepository;
import com.example.questory.repository.UserEntityRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserEntityRepository userEntityRepository;
    private final RoleEntityRepository roleEntityRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserEntityRepository userEntityRepository, RoleEntityRepository roleEntityRepository, PasswordEncoder passwordEncoder) {
        this.userEntityRepository = userEntityRepository;
        this.roleEntityRepository = roleEntityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Object saveUser(UserEntity userEntity) {
        UserEntity userFromDB = userEntityRepository.findByLogin(userEntity.getLogin());

        if (userFromDB != null) {
            return "There is already exists one";
        } else {
            RoleEntity userRole = roleEntityRepository.findByName("ROLE_USER");
            userEntity.setRoleEntity(userRole);
            userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
            return userEntityRepository.save(userEntity);
        }
    }

    public UserEntity findByLogin(String login) {
        return userEntityRepository.findByLogin(login);
    }

    public UserEntity findByLoginAndPassword(String login, String password) {
        UserEntity userEntity = findByLogin(login);
        if (userEntity != null) {
            if (passwordEncoder.matches(password, userEntity.getPassword())) {
                return userEntity;
            }
        }
        return null;
    }
}