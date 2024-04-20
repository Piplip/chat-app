package com.nkd.eida_backend.Entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nkd.eida_backend.Enumeration.FriendStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@Entity
@Table(name = "friendship")
public class FriendshipEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private UserEntity user;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "friend_id")
    private UserEntity friend;
    private LocalDateTime creationTime;
    @Enumerated(EnumType.STRING)
    private FriendStatus friendStatus;
}
