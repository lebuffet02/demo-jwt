package demojwt.jwt.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.stream.Collectors;


@Service
public class JwtService {

    @Autowired
    JwtEncoder encoder;

    public String generateToken(Authentication auth) {
        JwtClaimsSet claims = mapperParaJwtClaimsSet(auth, scope(auth));
        return encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    private String scope(Authentication auth) {
        return auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(" "));
    }

    private JwtClaimsSet mapperParaJwtClaimsSet(Authentication auth, String scope) {
        Instant now = Instant.now();
        return JwtClaimsSet.builder()
                .issuer("demo-jwt")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(300L)) //5 minutos
                .subject(auth.getName())
                .claim("scope", scope)
                .build();
    }
}