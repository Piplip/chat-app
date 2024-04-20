package com.nkd.eida_backend.Service.Impl;

import com.nkd.eida_backend.Domain.UserDetails;
import com.nkd.eida_backend.Entity.ConfirmationEntity;
import com.nkd.eida_backend.Entity.CredentialEntity;
import com.nkd.eida_backend.Entity.UserEntity;
import com.nkd.eida_backend.Enumeration.EventType;
import com.nkd.eida_backend.Enumeration.LoginType;
import com.nkd.eida_backend.Enumeration.UserStatus;
import com.nkd.eida_backend.Event.UserEvent;
import com.nkd.eida_backend.Exception.ApiException;
import com.nkd.eida_backend.Repository.ConfirmationRepository;
import com.nkd.eida_backend.Repository.CredentialRepository;
import com.nkd.eida_backend.Repository.UserRepository;
import com.nkd.eida_backend.Security.JwtTokenProvider;
import com.nkd.eida_backend.Service.UserService;
import com.nkd.eida_backend.cache.CacheStore;
import com.nkd.eida_backend.dtoRequest.UserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.nkd.eida_backend.Utils.UserUtils.createUserEntity;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final CredentialRepository credentialRepository;
    private final ConfirmationRepository confirmationRepository;
    private final ApplicationEventPublisher publisher;
    private final CacheStore<String, Integer> userCache;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void createUser(UserRequest userRequest) {
        var newUser = userRepository.save(createUserEntity(userRequest.getFirstName(), userRequest.getLastName(), userRequest.getEmail()));
        var newCredential = CredentialEntity.builder()
                .user(newUser)
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .isCredentialNonExpired(true)
                .build();
        credentialRepository.save(newCredential);
        var newConfirmation = new ConfirmationEntity(newUser);
        confirmationRepository.save(newConfirmation);
        publisher.publishEvent(new UserEvent(newUser, EventType.REGISTRATION, Map.of("token", newConfirmation.getToken())));
    }

    @Override
    public void verifyUserAccount(String token) {
        var confirmation = getConfirmation(token);
        var user = getUserByEmail(confirmation.getUser().getEmail());
        user.setEnabled(true);
        userRepository.save(user);
        confirmationRepository.delete(confirmation);
    }

    @Override
    public void updateLoginAttempt(String email, LoginType loginType) {
        var user = getUserByEmail(email);
        if(user == null) throw new ApiException("User not found");
        if(!user.getEnabled()) throw new ApiException("Account not verified");
        switch (loginType){
            case LOGIN_ATTEMPT -> {
                if(userCache.get(user.getEmail()) == null){
                    user.setLoginAttempts(0);
                    user.setAccountNonLocked(true);
                }
                user.setLoginAttempts(user.getLoginAttempts() + 1);
                userCache.put(user.getEmail(), user.getLoginAttempts());
                if(user.getLoginAttempts() > 5){
                    user.setAccountNonLocked(false);
                }
            }
            case LOGIN_SUCCESS -> {
                user.setAccountNonLocked(true);
                user.setLoginAttempts(0);
                user.setLastLogin(LocalDateTime.now());
                user.setUserStatus(UserStatus.ACTIVE);
                userCache.evict(user.getEmail());
            }
            default -> {}
        }
        userRepository.save(user);
    }

    @Override
    public String handleLogin(String email, String password) {
        if(userCache.get(email) != null && userCache.get(email) > 5){
            throw new ApiException("Account locked");
        }
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(email, password);
        Authentication authentication = authenticationManager.authenticate(token);
        updateLoginAttempt(email, LoginType.LOGIN_SUCCESS);
        return jwtTokenProvider.generateToken((UserDetails) authentication.getPrincipal());
    }

    @Override
    public List<UserEntity> findFriends(String query) {
        return userRepository.findFriends(query)
                .orElseThrow(() -> new ApiException("No friends found"));
    }

    @Override
    public UserEntity getUserByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ApiException("User not found"));
    }

    private ConfirmationEntity getConfirmation(String token) {
        return confirmationRepository.findByToken(token)
                .orElseThrow(() -> new ApiException("Confirmation token not found"));
    }

    private CredentialEntity getCredential(String email) {
        return credentialRepository.findByUser_Email(email)
                .orElseThrow(() -> new ApiException("Credential not found"));
    }
}
