package com.example.notificationservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${spring.rabbitmq.exchange}")
    private String exchange;
    
    @Value("${spring.rabbitmq.routingkey.task-created}")
    private String taskCreatedRoutingKey;
    
    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(exchange);
    }
    
    @Bean
    public Queue taskCreatedQueue() {
        return new Queue("task.created.queue", true);
    }
    
    @Bean
    public Binding taskCreatedBinding(Queue taskCreatedQueue, DirectExchange exchange) {
        return BindingBuilder.bind(taskCreatedQueue)
                .to(exchange)
                .with(taskCreatedRoutingKey);
    }
    
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}
