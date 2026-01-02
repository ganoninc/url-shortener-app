package com.ganoninc.viteurlshortener.urlservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {
    @Bean
    public NewTopic urlCreatedTopic() {
        return TopicBuilder
                .name("url-created")
                .partitions(3)
                .replicas(3)
                .build();
    }
}
