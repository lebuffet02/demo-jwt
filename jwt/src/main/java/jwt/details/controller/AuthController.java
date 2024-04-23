package jwt.details.controller;

import jwt.details.dto.LoginRequestDTO;
import jwt.details.dto.RegisterRequestDTO;
import jwt.details.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDTO registerDTO) {
        return ResponseEntity.ok(authService.registerService(registerDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginDTO) {
        return ResponseEntity.ok(authService.loginService(loginDTO) != null ? authService.loginService(loginDTO)
                : ResponseEntity.badRequest().build());
    }
}