package com.cafepos.websocket;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderEventPublisher {

    private final SimpMessagingTemplate messagingTemplate;

    public OrderEventPublisher(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void publishOrderUpdate(Object orderId, Object event) {
        messagingTemplate.convertAndSend("/topic/orders", event);
    }
}
