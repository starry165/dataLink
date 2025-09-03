package org.example.dataLink.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CdcKafkaRequest {
    private String readerUrl;
    private String writerUrl;
    private String readerTable;
    private String writerTable;
    private String kafkaServer;
    private String historyTopic;
}