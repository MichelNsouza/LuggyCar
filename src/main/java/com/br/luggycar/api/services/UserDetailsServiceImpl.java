package com.br.luggycar.api.services;

import com.br.luggycar.api.entities.user.SystemUser;
import com.br.luggycar.api.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SystemUser systemUser = userRepository.findByName(username).
                orElseThrow( () -> new UsernameNotFoundException("Usuario não enconatrado!"));
        return new org.springframework.security.core.userdetails.User(systemUser.getUsername(), systemUser.getPassword(),true, true,true,true,systemUser.getAuthorities());
    }
}
