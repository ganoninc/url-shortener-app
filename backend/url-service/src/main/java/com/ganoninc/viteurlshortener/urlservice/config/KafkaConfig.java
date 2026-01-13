package com.ganoninc.viteurlshortener.urlservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Value("${kafka.topic.creation.partition-count}")
    private int topicCreationPartitionCount;

    @Value("${kafka.topic.creation.replica-count}")
    private int topicCreationReplicaCount;

    @Bean
    public NewTopic urlCreatedTopic() {
        return TopicBuilder
                .name("url-created")
                .partitions(topicCreationPartitionCount)
                .replicas(topicCreationReplicaCount)
                .build();
    }
}
