package com.nkd.eida_backend.Service.Impl;

import com.nkd.eida_backend.Entity.FriendshipEntity;
import com.nkd.eida_backend.Entity.UserEntity;
import com.nkd.eida_backend.Enumeration.FriendStatus;
import com.nkd.eida_backend.Enumeration.NotificationType;
import com.nkd.eida_backend.Exception.ApiException;
import com.nkd.eida_backend.Repository.FriendRepository;
import com.nkd.eida_backend.Repository.NotificationRepository;
import com.nkd.eida_backend.Service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class FriendServiceImpl implements FriendService {
    private final FriendRepository friendRepository;
    private final NotificationRepository notificationRepository;

    @Override
    public void createFriendship(UserEntity sender, UserEntity receiver) {
        FriendshipEntity friendship = FriendshipEntity.builder()
                .user(sender)
                .friend(receiver)
                .creationTime(LocalDateTime.now())
                .friendStatus(FriendStatus.PENDING)
                .build();
        friendRepository.save(friendship);
    }

    @Override
    @Transactional
    public void handleFriendRequest(UserEntity sender, UserEntity receiver, NotificationType type) {
        FriendshipEntity friendship = friendRepository.findByUserIdAndFriendId(sender.getId(), receiver.getId());
        if(friendship == null) throw new ApiException("Friendship not found");
        if(type.equals(NotificationType.ACCEPT_FRIEND_REQUEST)){
            friendship.setFriendStatus(FriendStatus.ACCEPTED);
            friendRepository.save(friendship);
        }
        notificationRepository.deleteBySenderEmailAndReceiverEmailAndMessageType(sender.getEmail(), receiver.getEmail(), NotificationType.FRIEND_REQUEST);
    }

    @Override
    public List<UserEntity> getUserFriends(String email) {
        List<FriendshipEntity> friendList = friendRepository.findUserFriends(email).orElseThrow(() -> new ApiException("No friends found"));
        return friendList.stream().map(friendship -> {
            if(friendship.getUser().getEmail().equals(email)) return friendship.getFriend();
            return friendship.getUser();
        }).toList();
    }
}
