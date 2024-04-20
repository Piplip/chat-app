package com.nkd.eida_backend.Entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nkd.eida_backend.Enumeration.NotificationType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@Builder
@NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class NotificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String senderName;
    private String senderEmail;
    private String senderImageUrl;
    private String content;
    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;
    private boolean isRead;
    private LocalDateTime timestamp;
    @ManyToOne(fetch = FetchType.EAGER)
    private UserEntity receiver;
}
