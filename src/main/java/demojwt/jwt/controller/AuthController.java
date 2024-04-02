package demojwt.jwt.controller;

import demojwt.jwt.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("authenticate")
    public String authenticate(Authentication auth) {
        return authService.generateToken(auth);
    }
}
