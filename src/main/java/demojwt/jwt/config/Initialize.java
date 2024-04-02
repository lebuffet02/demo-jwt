package demojwt.jwt.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Date;
import java.util.Locale;

@Configuration
public class Initialize {

    private static final Logger LOGGER = LoggerFactory.getLogger(Initialize.class);


    @Value("${spring.security.user.password}")
    private String passwd;

    @Bean
    ApplicationRunner runner(PasswordEncoder passwordEncoder) {
        LOGGER.info("-------------SUBIU-------------");
        LOGGER.info(String.valueOf(new Date()).concat(new Locale(" pt", "BR").toString()));
        return args -> System.out.println(passwordEncoder.encode(passwd));
    }
}
