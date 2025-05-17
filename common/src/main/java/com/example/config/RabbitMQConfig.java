package com.example.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Bean
    public Queue taskNotificationQueue() {
        return new Queue("task.notification.queue", true);
    }

    @Bean
    public TopicExchange taskExchange() {
        return new TopicExchange("task.exchange");
    }

    @Bean
    public Binding taskBinding(Queue taskNotificationQueue, TopicExchange taskExchange) {
        return BindingBuilder.bind(taskNotificationQueue).to(taskExchange).with("task.created");
    }
}