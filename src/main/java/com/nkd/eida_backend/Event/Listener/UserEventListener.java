package com.nkd.eida_backend.Event.Listener;

import com.nkd.eida_backend.Event.UserEvent;
import com.nkd.eida_backend.Service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserEventListener {
    private final EmailService emailService;

    @EventListener
    public void onUserEvent(UserEvent userEvent){
        switch (userEvent.getEventType()){
            case REGISTRATION -> emailService.sendRegistrationEmail(userEvent.getUser().getFirstName(), userEvent.getUser().getEmail(), userEvent.getData().get("token").toString());
            case RESET_PASSWORD -> emailService.sendPasswordResetEmail(userEvent.getUser().getFirstName(), userEvent.getUser().getEmail(), userEvent.getData().get("token").toString());

            default -> {}
        }
    }
}
