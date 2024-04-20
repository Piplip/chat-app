package com.nkd.eida_backend.Repository;

import com.nkd.eida_backend.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmailIgnoreCase(String email);
    @Query("SELECT u FROM UserEntity u WHERE u.firstName LIKE %:query% OR u.lastName LIKE %:query% OR u.lastName || ' ' || u.firstName LIKE %:query%")
    Optional<List<UserEntity>> findFriends(String query);
}
