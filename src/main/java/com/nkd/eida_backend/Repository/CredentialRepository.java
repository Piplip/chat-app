package com.nkd.eida_backend.Repository;

import com.nkd.eida_backend.Entity.CredentialEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CredentialRepository extends JpaRepository<CredentialEntity, Long> {
    Optional<CredentialEntity> findByUser_Email(String email);
}
