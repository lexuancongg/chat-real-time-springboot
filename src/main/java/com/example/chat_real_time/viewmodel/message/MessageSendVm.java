package com.example.chat_real_time.viewmodel.message;

public record MessageSendVm(
        String content,
        Long receiverId
) {
}
