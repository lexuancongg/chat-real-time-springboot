package com.example.chat_real_time.repository;

import com.example.chat_real_time.model.Message;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends CrudRepository<Message, Long> {
    @Query(value = """
            SELECT m.* 
            FROM messages m
            JOIN (
               SELECT 
                  LEAST(sender_id, receiver_id) AS user1,
                  GREATEST(sender_id, receiver_id) AS user2,
                  MAX(created_at) AS last_time
               FROM messages
               WHERE :userId IN (sender_id, receiver_id)
               GROUP BY LEAST(sender_id, receiver_id), GREATEST(sender_id, receiver_id)
            ) t ON (
               (LEAST(m.sender_id, m.receiver_id) = t.user1 AND 
                GREATEST(m.sender_id, m.receiver_id) = t.user2 AND 
                m.created_at = t.last_time)
            )
            """, nativeQuery = true)
    List<Message> getConversationByUserId(@Param("userId") Long userId);


    @Query(value = """
    SELECT m.* 
    FROM messages m
    JOIN (
        SELECT group_id, MAX(created_at) AS last_time
        FROM messages
        WHERE group_id IS NOT NULL
        GROUP BY group_id
    ) t 
        ON m.group_id = t.group_id 
       AND m.created_at = t.last_time
    JOIN group_members gm 
        ON gm.group_id = m.group_id
    WHERE gm.user_id = :userId
    """, nativeQuery = true)
    List<Message> findLastGroupMessagesForUser(@Param("userId") Long userId);



    List<Message> findBySenderIdAndReceiverIdOrSenderIdAndReceiverIdOrderByCreatedAtAsc(
            Long senderId1, Long receiverId1,
            Long senderId2, Long receiverId2
    );


}
