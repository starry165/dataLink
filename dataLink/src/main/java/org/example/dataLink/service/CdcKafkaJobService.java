package org.example.dataLink.service;

import org.example.dataLink.pojo.CdcKafkaRequest;
import org.example.dataLink.pojo.Result;

public interface CdcKafkaJobService {
    Result creatJob(CdcKafkaRequest request);
    Result stopJob(Integer id);
}
