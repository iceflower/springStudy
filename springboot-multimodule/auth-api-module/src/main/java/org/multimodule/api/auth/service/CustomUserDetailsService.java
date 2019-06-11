package org.multimodule.api.auth.service;


import java.util.Optional;

import org.apache.log4j.Logger;
import org.multimodule.common.entity.CustomUserDetails;
import org.multimodule.common.entity.User;
import org.multimodule.common.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = Logger.getLogger(CustomUserDetailsService.class);
    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User dbUser = Optional.ofNullable(userRepository.findByEmail(email))
        		.orElseThrow(() -> new UsernameNotFoundException("Couldn't find a matching user email in the database for " + email));
        logger.info("Fetched user : " + dbUser + " by " + email);
        return new CustomUserDetails(dbUser);
                
    }

    public UserDetails loadUserById(Long id) {
        Optional<User> dbUser = userRepository.findById(id);
        logger.info("Fetched user : " + dbUser + " by " + id);
        return dbUser.map(CustomUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("Couldn't find a matching user id in the database for " + id));
    }
}
