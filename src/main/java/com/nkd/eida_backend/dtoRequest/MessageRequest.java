package com.nkd.eida_backend.dtoRequest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nkd.eida_backend.Enumeration.NotificationType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageRequest {
    private String from;
    private String to;
    private String content;
    private NotificationType type;
}
