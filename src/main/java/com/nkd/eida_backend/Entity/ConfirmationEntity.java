package com.nkd.eida_backend.Entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.UUID;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "confirmations")
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class ConfirmationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String token;
    @OneToOne(targetEntity = UserEntity.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id") // this mean that the id of the object will be used as the identity
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("user_id")
    private UserEntity user;

    public ConfirmationEntity(UserEntity user) {
        this.user = user;
        this.token = UUID.randomUUID().toString();
    }
}
