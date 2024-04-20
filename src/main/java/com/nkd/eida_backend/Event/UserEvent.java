package com.nkd.eida_backend.Event;

import com.nkd.eida_backend.Entity.UserEntity;
import com.nkd.eida_backend.Enumeration.EventType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter @Setter
@AllArgsConstructor
public class UserEvent {
    private UserEntity user;
    private EventType eventType;
    private Map<?,?> data;
}
