package com.nkd.eida_backend.Utils;

import com.nkd.eida_backend.Entity.UserEntity;
import com.nkd.eida_backend.Enumeration.ConversationType;
import com.nkd.eida_backend.dto.ConversationDTO;

public class ConversationUtils {
    public static ConversationDTO createConversationDTO(UserEntity friend, Long conversationID){
        return ConversationDTO.builder()
                .id(conversationID)
                .title(null)
                .friendImageUrl(friend.getImageUrl())
                .friendEmail(friend.getEmail())
                .lastMessage(null)
                .friendName(friend.getLastName() + " " + friend.getFirstName())
                .conversationType(ConversationType.SINGLE)
                .build();
    }
}
