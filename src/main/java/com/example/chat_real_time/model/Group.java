package com.example.chat_real_time.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "groups")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String avatarUrl;
    private Boolean isPrivate = false;
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "group")
    private List<GroupMember> members;

    @OneToMany(mappedBy = "group")
    private List<Message> messages;
}