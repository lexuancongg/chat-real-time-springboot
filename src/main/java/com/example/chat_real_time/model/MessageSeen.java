package com.example.chat_real_time.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// bảng trung gian khi gởi tin nhắn vào group => gởi nhiều tn và tn gởi cho nhiều ng
@Entity
@Table(name = "message_seen")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageSeen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "message_id")
    private Message message;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime seenAt = LocalDateTime.now();
}