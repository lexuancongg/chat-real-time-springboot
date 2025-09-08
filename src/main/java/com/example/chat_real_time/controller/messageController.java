package com.example.chat_real_time.controller;

import com.example.chat_real_time.model.Message;
import com.example.chat_real_time.service.MessageService;
import com.example.chat_real_time.viewmodel.conversation.ConversationVm;
import com.example.chat_real_time.viewmodel.message.MessageVm;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class messageController {
    private final MessageService messageService;

    // đầu tiên cần lây thông tin ds người dùng đã nt sx theo thời gian
    @GetMapping({"/conversations"})
    public ResponseEntity<List<ConversationVm>> getConversations() {
        return ResponseEntity.ok(this.messageService.getConversations());
    }

    @GetMapping({"/conversation/messages/{id}"})
    public ResponseEntity<List<MessageVm>> getConversation(@PathVariable Long id) {
        return ResponseEntity.ok(this.messageService.getConversation(id));
    }



}
