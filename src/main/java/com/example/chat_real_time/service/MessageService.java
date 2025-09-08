package com.example.chat_real_time.service;

import com.example.chat_real_time.exception.NotFoundException;
import com.example.chat_real_time.model.Message;
import com.example.chat_real_time.model.User;
import com.example.chat_real_time.model.enums.ConvertionType;
import com.example.chat_real_time.repository.MessageRepository;
import com.example.chat_real_time.repository.UserRepository;
import com.example.chat_real_time.untils.AuthenticationUtils;
import com.example.chat_real_time.viewmodel.conversation.ConversationVm;
import com.example.chat_real_time.viewmodel.message.MessageVm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public List<ConversationVm> getConversations() {
        String userName = AuthenticationUtils.getUsername();
        User user = this.userRepository.findByUsername(userName)
                .orElseThrow(() -> new NotFoundException("User not found"));

        // 1. Private conversations
        List<Message> privateMessages = this.messageRepository.getConversationByUserId(user.getId());
        List<ConversationVm> privateConversations = privateMessages.stream()
                .map(msg -> {
                    User partner = msg.getSender().getId().equals(user.getId())
                            ? msg.getReceiver()
                            : msg.getSender();

                    return ConversationVm.builder()
                            .id(partner.getId())
                            .convertionType(ConvertionType.PRIVATE)
                            .nameConversation(partner.getDisplayName() != null ? partner.getDisplayName() : partner.getUsername())
                            .avtarUrl(partner.getAvatarUrl())
                            .lastMessage(msg.getContent())
                            .lastMessageUserId(msg.getSender().getId())
                            .lastMessageAt(msg.getCreatedAt())
                            .build();
                })
                .toList();

        // 2. Group conversations
        List<Message> lastGroupMessages = messageRepository.findLastGroupMessagesForUser(user.getId());
        List<ConversationVm> groupConversations = lastGroupMessages.stream()
                .map(msg -> ConversationVm.builder()
                        .id(msg.getGroup().getId())
                        .convertionType(ConvertionType.GROUP)
                        .nameConversation(msg.getGroup().getName())
                        .avtarUrl(msg.getGroup().getAvatarUrl())
                        .lastMessage(msg.getContent())
                        .lastMessageUserId(msg.getSender().getId())
                        .lastMessageAt(msg.getCreatedAt())
                        .build())
                .toList();

        return Stream.concat(privateConversations.stream(), groupConversations.stream())
                .sorted(Comparator.comparing(ConversationVm::lastMessageAt).reversed())
                .toList();
    }


    public List<MessageVm> getConversation(Long  partnerId){
        String userName = AuthenticationUtils.getUsername();
        User user = this.userRepository.findByUsername(userName)
                .orElseThrow(() -> new NotFoundException("User not found"));
        List<Message> messages = messageRepository
                .findBySenderIdAndReceiverIdOrSenderIdAndReceiverIdOrderByCreatedAtAsc(
                        user.getId(), partnerId,
                        partnerId, user.getId()
                );

        return messages.stream()
                .map(msg -> new MessageVm(
                        msg.getId(),
                        msg.getSender().getId(),
                        msg.getSender().getDisplayName(),
                        msg.getSender().getAvatarUrl(),
                        msg.getContent(),
                        msg.getCreatedAt(),
                        msg.getSender().getId().equals(user.getId())
                ))
                .toList();

    }
}
