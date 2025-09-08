package com.example.chat_real_time.model;

import com.example.chat_real_time.model.enums.MessageStatus;
import com.example.chat_real_time.model.enums.MessageType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "messages",
        indexes = {
                @Index(name = "idx_group_createdAt", columnList = "group_id, createdAt")
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    // một tin nhắn do 1 ng gởi 1 ng cos thể gơ nhiều tn
    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;


    // một tn có một ng nhân , một ng cos thể nhận nhiều tn
    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;

    @Column(nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    private MessageType type = MessageType.TEXT;

    @Enumerated(EnumType.STRING)
    private MessageStatus status = MessageStatus.SENT;

    private LocalDateTime createdAt = LocalDateTime.now();

    // trường hợp gởi vào group nên dung Set
    @OneToMany(mappedBy = "message")
    private Set<MessageSeen> seenBy;
}