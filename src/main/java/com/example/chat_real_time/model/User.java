package com.example.chat_real_time.model;

import com.example.chat_real_time.model.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "users")
public class User{
    @Id
    private Long id;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    private String displayName;
    private String avatarUrl;
    @Enumerated(EnumType.STRING)
    private Status status = Status.OFFLINE;


    private LocalDateTime createdAt = LocalDateTime.now();
    @OneToMany(mappedBy = "user")
    private List<Friend> friends;

    @OneToMany(mappedBy = "friend")
    private List<Friend> friendOf;

    @OneToMany(mappedBy = "user")
    private List<Device> devices;

    @OneToMany(mappedBy = "sender")
    private List<Message> messagesSent;

    @OneToMany(mappedBy = "receiver")
    private List<Message> messagesReceived;

    @OneToMany(mappedBy = "user")
    private List<GroupMember> groupMembers;


}
