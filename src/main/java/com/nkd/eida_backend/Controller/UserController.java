package com.nkd.eida_backend.Controller;

import com.nkd.eida_backend.Enumeration.LoginType;
import com.nkd.eida_backend.Service.ConversationService;
import com.nkd.eida_backend.Service.FriendService;
import com.nkd.eida_backend.Service.NotificationService;
import com.nkd.eida_backend.Service.UserService;
import com.nkd.eida_backend.Utils.RequestUtils;
import com.nkd.eida_backend.dtoRequest.LoginRequest;
import com.nkd.eida_backend.dtoRequest.UserRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Collections;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final NotificationService notificationService;
    private final FriendService friendService;
    private final ConversationService conversationService;

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserRequest userRequest, HttpServletRequest request){
        userService.createUser(userRequest);
        return ResponseEntity
                .created(getUri()).body(RequestUtils.createResponse(request, HttpStatus.CREATED, Collections.emptyMap(), "Account created. Check your email to verify your account."));
    }

    private URI getUri() {
        return URI.create("");
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestParam("token") String token){
        userService.verifyUserAccount(token);
        String redirectUrl = "http://localhost:3000" + "/verify/success";
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(java.net.URI.create(redirectUrl))
                .build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest request){
        userService.updateLoginAttempt(loginRequest.getEmail(), LoginType.LOGIN_ATTEMPT);
        String jwt = userService.handleLogin(loginRequest.getEmail(), loginRequest.getPassword());
        return ResponseEntity.ok().body(RequestUtils.createResponse(request, HttpStatus.OK, Map.of("jwt", jwt), "Login successful"));
    }

    @GetMapping("/search/{query}")
    public ResponseEntity<?> findFriends(@PathVariable String query){
        return ResponseEntity.ok().body(userService.findFriends(query));
    }

    @GetMapping("/fetch/notifications")
    public ResponseEntity<?> getNotifications(@RequestParam("email") String email){
        return ResponseEntity.ok().body(notificationService.getUserNotifications(email));
    }

    @GetMapping("/fetch/friends")
    public ResponseEntity<?> getUserFriends(@RequestParam("email") String email){
        return ResponseEntity.ok().body(friendService.getUserFriends(email));
    }

    @GetMapping("/fetch/conversations")
    public ResponseEntity<?> getUserConversations(@RequestParam("email") String email){
        return ResponseEntity.ok().body(conversationService.getAllByUserEmail(email));
    }

    @GetMapping("/conversation/content")
    public ResponseEntity<?> getConversationContent(@RequestParam("id") Long conversationID){
        return ResponseEntity.ok().body(conversationService.getConversationContent(conversationID));
    }
}
