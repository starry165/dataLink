package org.example.dataLink.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CdcKafkaJob {
    String jobName;
    String cdcKafkaName;
    String kafkaSqlName;
}
