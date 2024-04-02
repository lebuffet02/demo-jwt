package demojwt.jwt.service;

import demojwt.jwt.jwt.JwtService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final JwtService jwtService;

    public AuthService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    public String generateToken(Authentication auth) {
        return jwtService.generateToken(auth);
    }
}
