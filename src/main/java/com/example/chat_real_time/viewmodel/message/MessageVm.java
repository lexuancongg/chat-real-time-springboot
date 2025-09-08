package com.example.chat_real_time.viewmodel.message;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record MessageVm(
        Long id,
        Long senderId,
        String senderName,
        String senderAvatar,
        String content,
        LocalDateTime createdAt,
        boolean isOwn // true nếu là user hiện tại gửi
) {}
