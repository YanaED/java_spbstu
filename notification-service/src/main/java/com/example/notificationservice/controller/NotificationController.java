package com.example.notificationservice.controller;

import com.example.common.dto.NotificationDto;
import com.example.notificationservice.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);
    
    private final NotificationService notificationService;
    
    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationDto>> getNotificationsByUserId(@PathVariable Long userId) {
        logger.info("REST request to get all notifications for user ID: {}", userId);
        return ResponseEntity.ok(notificationService.getNotificationsByUserId(userId));
    }
    
    @GetMapping("/user/{userId}/unread")
    public ResponseEntity<List<NotificationDto>> getUnreadNotificationsByUserId(@PathVariable Long userId) {
        logger.info("REST request to get unread notifications for user ID: {}", userId);
        return ResponseEntity.ok(notificationService.getUnreadNotificationsByUserId(userId));
    }
    
    @PutMapping("/{id}/read")
    public ResponseEntity<NotificationDto> markNotificationAsRead(@PathVariable Long id) {
        logger.info("REST request to mark notification with ID: {} as read", id);
        return ResponseEntity.ok(notificationService.markAsRead(id));
    }
}
