package com.nkd.eida_backend.Repository;

import com.nkd.eida_backend.Entity.NotificationEntity;
import com.nkd.eida_backend.Enumeration.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
    Optional<List<NotificationEntity>> findAllByReceiverEmail(String email);
    @Modifying
    @Transactional
    @Query("DELETE FROM NotificationEntity n " +
            "WHERE n.receiver.email = ?1 AND n.senderEmail = ?2 AND n.notificationType = ?3 " +
            "OR n.receiver.email = ?2 AND n.senderEmail = ?1 AND n.notificationType = ?3")
    void deleteBySenderEmailAndReceiverEmailAndMessageType(String senderEmail, String receiverEmail, NotificationType type);
}
