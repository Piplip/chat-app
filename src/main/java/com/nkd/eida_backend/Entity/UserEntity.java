package com.nkd.eida_backend.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.nkd.eida_backend.Enumeration.Role;
import com.nkd.eida_backend.Enumeration.UserStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
@Table(name = "users")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_sequence")
    @SequenceGenerator(name = "user_sequence", sequenceName = "user_sequence", allocationSize = 1)
    private Long id;
    private String firstName;
    private String lastName;
    @Column(nullable = false, unique = true)
    private String email;
    private String imageUrl;
    @JsonIgnore
    private Integer loginAttempts;
    private LocalDateTime lastLogin;
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;
    private Boolean enabled;
    private Boolean accountNonLocked;
    private Boolean accountNonExpired;
    @Enumerated(EnumType.STRING)
    private Role role;
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "receiver")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<NotificationEntity> notifications;
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<FriendshipEntity> friends;
    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "users")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<ConversationEntity> conversations;
}
