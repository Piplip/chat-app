package com.nkd.eida_backend.Service.Impl;

import com.nkd.eida_backend.Entity.CredentialEntity;
import com.nkd.eida_backend.Entity.UserEntity;
import com.nkd.eida_backend.Enumeration.Role;
import com.nkd.eida_backend.Repository.CredentialRepository;
import com.nkd.eida_backend.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final CredentialRepository credentialRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("Loading user by email: {}", email);
        Optional<UserEntity> user = userRepository.findByEmailIgnoreCase(email);
        if(user.isPresent()){
            Optional<CredentialEntity> credential = credentialRepository.findByUser_Email(email);
            if(credential.isPresent()){
                return com.nkd.eida_backend.Domain.UserDetails.builder()
                        .user(user.get())
                        .credential(credential.get())
                        .role(Role.USER)
                        .build();
            }else{
                log.error("User not found: {}", email);
                throw new UsernameNotFoundException("User not found");
            }
        }
        log.error("User not found: {}", email);
        throw new UsernameNotFoundException("User not found");
    }
}