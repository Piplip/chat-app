package com.nkd.eida_backend.Repository;

import com.nkd.eida_backend.Entity.MessageEntity;
import com.nkd.eida_backend.dtoRequest.ConversationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Long> {
    @Query("SELECT new com.nkd.eida_backend.dtoRequest.ConversationRequest(c.id, m.sender, CASE WHEN m.sender = :email THEN u.email ELSE :email END, m.message, c.conversationType) " +
            "FROM MessageEntity m " +
            "JOIN m.conversation c " +
            "JOIN c.users u " +
            "WHERE c.id = :id " +
            "ORDER BY m.sendAt ASC")
    Optional<List<ConversationRequest>> getAllByConversationID(Long id);
}
