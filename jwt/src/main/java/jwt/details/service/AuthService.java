package jwt.details.service;

import jwt.details.dto.LoginRequestDTO;
import jwt.details.dto.RegisterRequestDTO;
import jwt.details.dto.ResponseDTO;
import jwt.details.entity.UserEntity;
import jwt.details.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    UserRepository repository;

    private final PasswordEncoder passwordEncoder;
    private final SecurityFilterService filter;

    public AuthService(PasswordEncoder passwordEncoder, SecurityFilterService filter) {
        this.passwordEncoder = passwordEncoder;
        this.filter = filter;
    }

    public ResponseDTO registerService(RegisterRequestDTO registerDTO) {
        if (repository.findByEmail(registerDTO.email()).isEmpty()) {
            UserEntity userEntitySaved = saveUser(registerDTO);
            return entityToDto(userEntitySaved.getName(), filter.generateToken(userEntitySaved));
        }
        return null;
    }

    public ResponseDTO loginService(LoginRequestDTO loginDTO) {
        UserEntity userEntity = repository.findByEmail(loginDTO.email()).orElseThrow(() -> new RuntimeException("User not found"));
        if (passwordEncoder.matches(loginDTO.password(), userEntity.getPassword())) {
            return new ResponseDTO(userEntity.getName(), filter.generateToken(userEntity));
        }
        return null;
    }

    private UserEntity saveUser(RegisterRequestDTO registerDTO) {
        UserEntity newUserEntity = new UserEntity();
        newUserEntity.setPassword(passwordEncoder.encode(registerDTO.password()));
        newUserEntity.setEmail(registerDTO.email());
        newUserEntity.setName(registerDTO.name());
        repository.save(newUserEntity);
        return newUserEntity;
    }

    private ResponseDTO entityToDto(String name, String token) {
        return ResponseDTO.builder()
                .name(name)
                .token(token)
                .build();
    }
}