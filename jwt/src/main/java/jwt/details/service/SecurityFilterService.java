package jwt.details.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import jwt.details.entity.UserEntity;
import jwt.details.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;

@Service
public class SecurityFilterService extends OncePerRequestFilter implements UserDetailsService {

    @Autowired
    UserRepository repository;

    @Value("${spring.application.name}")
    private String name;
    @Value("${api.security.token.secret}")
    private String secret;


    public String generateToken(UserEntity userEntity) {
        try {
            return JWT.create().withIssuer(name).withSubject(userEntity.getEmail())
                    .withExpiresAt(LocalDateTime.now().plusMinutes(5).toInstant(ZoneOffset.of("-03:00")))
                    .sign(Algorithm.HMAC256(secret));
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro enquanto autentica.");
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filter) throws ServletException, IOException {
        var login = validateToken(recoverToken(req));
        if(login != null) {
            UserEntity userEntity = repository.findByEmail(login).orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userEntity, null,
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))));
        }
        filter.doFilter(req, res);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = repository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));
        return new org.springframework.security.core.userdetails.User(userEntity.getEmail(), userEntity.getPassword(), new ArrayList<>());
    }

    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        return (authHeader != null) ? authHeader.replace("Bearer ", ""): null;
    }

    private String validateToken(String token) {
        try {
            return JWT.require(Algorithm.HMAC256(secret)).withIssuer(name).build().verify(token).getSubject();
        } catch (JWTVerificationException exception) {
            return null;
        }
    }
}