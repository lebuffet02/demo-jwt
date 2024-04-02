package demojwt.jwt.jwt.user.details.Impl;

import demojwt.jwt.jwt.user.details.UserDetailsAuth;
import demojwt.jwt.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(UserDetailsAuth::new)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não existente"));
    }
}
