package com.nkd.eida_backend.Entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@Entity
@Table(name = "messages")
public class MessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String message;
    private LocalDateTime sendAt;
    private String sender;
    @ManyToOne
    @JoinColumn(name = "conversation_id")
    private ConversationEntity conversation;
}
