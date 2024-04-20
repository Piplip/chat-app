package com.nkd.eida_backend.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nkd.eida_backend.Enumeration.ConversationType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
@Entity
public class ConversationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String lastMessage;
    @JsonIgnore
    @OneToMany(mappedBy = "conversation")
    private List<MessageEntity> messages;
    @Enumerated(EnumType.STRING)
    private ConversationType conversationType;
    @ManyToMany
    @JoinTable(
        name = "conversation_user",
        joinColumns = @JoinColumn(name = "conversation_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<UserEntity> users;
}
