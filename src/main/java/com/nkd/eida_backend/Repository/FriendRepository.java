package com.nkd.eida_backend.Repository;

import com.nkd.eida_backend.Entity.FriendshipEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRepository extends JpaRepository<FriendshipEntity, Long>{
    @Query("SELECT f FROM FriendshipEntity f WHERE f.user.id = ?1 AND f.friend.id = ?2 OR f.user.id = ?2 AND f.friend.id = ?1")
    FriendshipEntity findByUserIdAndFriendId(Long userId, Long friendId);
    @Transactional
    @Query("SELECT f FROM FriendshipEntity f WHERE (f.user.email = ?1 OR f.friend.email = ?1) AND f.friendStatus = 'ACCEPTED'")
    Optional<List<FriendshipEntity>> findUserFriends(String email);
}
