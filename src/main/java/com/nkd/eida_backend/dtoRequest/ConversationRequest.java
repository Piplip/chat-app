package com.nkd.eida_backend.dtoRequest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nkd.eida_backend.Enumeration.ConversationType;
import lombok.*;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor @AllArgsConstructor
public class ConversationRequest {
    private Long conversationID;
    private String from;
    private String to;
    private String content;
    private ConversationType type;
}
