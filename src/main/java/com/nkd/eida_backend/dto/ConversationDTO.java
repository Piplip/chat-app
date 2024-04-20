package com.nkd.eida_backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nkd.eida_backend.Enumeration.ConversationType;
import lombok.*;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class ConversationDTO {
    private Long id;
    private String title;
    private String lastMessage;
    private String friendName;
    private String friendEmail;
    private String friendImageUrl;
    private ConversationType conversationType;

    public ConversationDTO(Long id, String title, String lastMessage, ConversationType conversationType, String friendEmail, String firstName, String lastName, String friendImageUrl) {
        this.id = id;
        this.title = title;
        this.lastMessage = lastMessage;
        this.friendName = lastName + " " + firstName;
        this.friendEmail = friendEmail;
        this.friendImageUrl = friendImageUrl;
        this.conversationType = conversationType;
    }
}
