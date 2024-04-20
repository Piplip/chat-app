package com.nkd.eida_backend.Repository;

import com.nkd.eida_backend.Entity.ConfirmationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfirmationRepository extends JpaRepository<ConfirmationEntity, Long> {
    Optional<ConfirmationEntity> findByToken(String token);
}
