package org.fiodorov.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.fiodorov.app.options.auth.UserRole;
import org.fiodorov.app.options.auth.UserToken;
import org.fiodorov.model.User;
import org.fiodorov.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by User-MD037 on 24.11.2015.
 */
@Service("userDetailsService")
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    private HttpServletRequest request;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserToken loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findOneByEmail(username);
        if ( user != null) {
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(UserRole.getAuthority(user));
            authorities.add(authority);
            return new UserToken(user.getEmail(), user.getPassword(), user.isEnabled(),
                    true, true, true, authorities);
        } else {
            return new UserToken(username, "", true, true, true, true,
                    Arrays.asList(new SimpleGrantedAuthority("ANONYMOUS")));
        }

    }

    public HttpServletRequest getRequest() {
        return request;
    }
}
