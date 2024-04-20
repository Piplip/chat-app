package com.nkd.eida_backend.Service;

import com.nkd.eida_backend.Entity.UserEntity;
import com.nkd.eida_backend.dto.ConversationDTO;
import com.nkd.eida_backend.dtoRequest.ConversationRequest;
import com.nkd.eida_backend.dtoRequest.MessageRequest;
import org.springframework.data.util.Pair;

import java.util.List;

public interface ConversationService {
    List<ConversationDTO> getAllByUserEmail(String email);
    Pair<ConversationDTO, ConversationDTO> createConversationResponse(UserEntity sender, UserEntity receiver);
    void updateConversation(ConversationRequest conversationRequest);
    List<UserEntity> getAllUsersByConversationID(Long id);
    List<ConversationRequest> getConversationContent(Long conversationID);
}
