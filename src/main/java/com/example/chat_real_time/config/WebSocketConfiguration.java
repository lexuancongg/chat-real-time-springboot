package com.example.chat_real_time.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // /portfolio is the HTTP URL for the endpoint to which a WebSocket (or SockJS)
        // client needs to connect for the WebSocket handshake
        registry.addEndpoint("/ws").withSockJS()
                .setInterceptors(this.httpSessionHandshakeInterceptor());;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // STOMP messages whose destination header begins with /app are routed to
        // @MessageMapping methods in @Controller classes
        config.setApplicationDestinationPrefixes("/app");
        // Use the built-in message broker for subscriptions and broadcasting and
        // route messages whose destination header begins with /topic or /queue to the broker
        config.setUserDestinationPrefix("/user");
        config.enableSimpleBroker("/topic", "/queue");
    }
    @Bean
    public HandshakeInterceptor httpSessionHandshakeInterceptor() {
        return new HttpSessionHandshakeInterceptor();
    }
}

// docs
// .withSockJS
// SockJS giống như một lớp “bảo vệ” cho WebSocket.
//
//Khi client kết nối bằng SockJS:
//
//Nếu WebSocket được hỗ trợ → dùng WebSocket thật.
//
//Nếu không → SockJS fake WebSocket bằng cách:
//
//Long-polling
//
//XHR streaming
//
//JSONP polling
//
//Nhìn từ client → vẫn giống WebSocket, bạn code bình thường.
//
//Nhìn từ server → bạn vẫn handle message qua SimpMessagingTemplate / WebSocketHandler, không cần quan tâm fallback.
//
//Client
//  |
//  |--- WebSocket (nếu được)
//  |--- hoặc Long-Polling / XHR (nếu WebSocket không hỗ trợ)
//  |
//Server
