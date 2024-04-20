package com.nkd.eida_backend.Service.Impl;

import com.nkd.eida_backend.Entity.ConversationEntity;
import com.nkd.eida_backend.Entity.MessageEntity;
import com.nkd.eida_backend.Entity.UserEntity;
import com.nkd.eida_backend.Enumeration.ConversationType;
import com.nkd.eida_backend.Exception.ApiException;
import com.nkd.eida_backend.Repository.ConversationRepository;
import com.nkd.eida_backend.Repository.MessageRepository;
import com.nkd.eida_backend.Service.ConversationService;
import com.nkd.eida_backend.dto.ConversationDTO;
import com.nkd.eida_backend.dtoRequest.ConversationRequest;
import com.nkd.eida_backend.dtoRequest.MessageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.nkd.eida_backend.Utils.ConversationUtils.createConversationDTO;

@RequiredArgsConstructor
@Service
public class ConversationServiceImpl implements ConversationService {
    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;

    public Long createBlankConversation(UserEntity sender, UserEntity receiver) {
        ConversationEntity blankConversation = ConversationEntity.builder()
                .title(null)
                .lastMessage(null)
                .messages(null)
                .users(List.of(sender, receiver))
                .conversationType(ConversationType.SINGLE)
                .build();
        conversationRepository.save(blankConversation);
        return blankConversation.getId();
    }

    @Override
    public List<ConversationDTO> getAllByUserEmail(String email) {
        return conversationRepository.getAllByUserEmail(email).orElseThrow(
            () -> new ApiException("No conversation found"));
    }

    @Override
    public Pair<ConversationDTO, ConversationDTO> createConversationResponse(UserEntity sender, UserEntity receiver) {
        Long conversationID = createBlankConversation(sender, receiver);
        ConversationDTO senderConversation = createConversationDTO(sender, conversationID);
        ConversationDTO receiverConversation = createConversationDTO(receiver, conversationID);
        return Pair.of(senderConversation, receiverConversation);
    }

    private ConversationEntity findConversationById(Long id) {
        return conversationRepository.findById(id).orElseThrow(
            () -> new ApiException("No conversation found"));
    }

    @Override
    public void updateConversation(ConversationRequest conversationRequest) {
        ConversationEntity conversation = findConversationById(conversationRequest.getConversationID());
        MessageEntity message = MessageEntity.builder()
                .message(conversationRequest.getContent())
                .sender(conversationRequest.getFrom())
                .conversation(conversation)
                .build();
        messageRepository.save(message);
        conversation.setLastMessage(conversationRequest.getContent());
        conversationRepository.save(conversation);
    }

    @Override
    public List<UserEntity> getAllUsersByConversationID(Long id) {
        return conversationRepository.getAllUsersByConversationId(id).orElseThrow(
            () -> new ApiException("No user found"));
    }

    @Override
    public List<ConversationRequest> getConversationContent(Long conversationID) {
        return messageRepository.getAllByConversationID(conversationID).orElseThrow(
            () -> new ApiException("No message found"));
    }
}
