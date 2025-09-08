package com.example.chat_real_time.controller;

import com.example.chat_real_time.model.Message;
import com.example.chat_real_time.model.User;
import com.example.chat_real_time.repository.UserRepository;
import com.example.chat_real_time.service.MessageService;
import com.example.chat_real_time.untils.AuthenticationUtils;
import com.example.chat_real_time.viewmodel.conversation.ConversationVm;
import com.example.chat_real_time.viewmodel.message.MessageSendVm;
import com.example.chat_real_time.viewmodel.message.MessageVm;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class messageController {
    private final MessageService messageService;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    // đầu tiên cần lây thông tin ds người dùng đã nt sx theo thời gian
    @GetMapping({"/conversations"})
    public ResponseEntity<List<ConversationVm>> getConversations() {
        return ResponseEntity.ok(this.messageService.getConversations());
    }

    @GetMapping({"/conversation/messages/{id}"})
    public ResponseEntity<List<MessageVm>> getConversation(@PathVariable Long id) {
        return ResponseEntity.ok(this.messageService.getConversation(id));
    }



    @MessageMapping("/message/send")
    public Message sendMessage(@Payload MessageSendVm messageSendVm) {
        User userReceiver = this.userRepository.findById(messageSendVm.receiverId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        String usernameReceiver = userReceiver.getUsername();


               String usernameCurrent = AuthenticationUtils.getUsername();
        User userCurrent = this.userRepository.findByUsername(usernameCurrent)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Message message = Message.builder()
                .content(messageSendVm.content())
                .receiver(userReceiver)
                .sender(userCurrent)
                .build();



        this.messagingTemplate.convertAndSendToUser(usernameReceiver, "/queue/messages", messageSendVm.content());

    }







}
