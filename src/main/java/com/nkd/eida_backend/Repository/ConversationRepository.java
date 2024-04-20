package com.nkd.eida_backend.Repository;

import com.nkd.eida_backend.Entity.ConversationEntity;
import com.nkd.eida_backend.Entity.UserEntity;
import com.nkd.eida_backend.dto.ConversationDTO;
import com.nkd.eida_backend.dtoRequest.ConversationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<ConversationEntity, Long> {
    @Query("SELECT new com.nkd.eida_backend.dto.ConversationDTO(c.id, c.title, c.lastMessage, c.conversationType, otherUser.email, otherUser.firstName, otherUser.lastName, otherUser.imageUrl) " +
            "FROM ConversationEntity c " +
            "JOIN c.users currentUser " +
            "JOIN c.users otherUser " +
            "WHERE currentUser.email = :email AND otherUser.email != :email")
    Optional<List<ConversationDTO>> getAllByUserEmail(String email);
    @Query("SELECT c.users FROM ConversationEntity c WHERE c.id = :id")
    Optional<List<UserEntity>> getAllUsersByConversationId(Long id);
}
