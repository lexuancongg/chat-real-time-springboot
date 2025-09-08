package com.example.chat_real_time.viewmodel.conversation;

import com.example.chat_real_time.model.enums.ConvertionType;
import lombok.Builder;

import java.time.LocalDateTime;

    @Builder
    public record ConversationVm(
            Long id,
            ConvertionType convertionType, // PRIVATE / GROUP
            String nameConversation,       // tên user hoặc group
            String avtarUrl,               // avatar user hoặc group
            String lastMessage,            // nội dung tin nhắn cuối
            Long lastMessageUserId,        // id người gửi tin cuối
            LocalDateTime lastMessageAt    // thời điểm gửi
    ) {}
