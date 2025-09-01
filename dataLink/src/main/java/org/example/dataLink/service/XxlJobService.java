package org.example.dataLink.service;

import org.springframework.util.MultiValueMap;

import java.util.Map;

public interface XxlJobService {
    Integer addJob(MultiValueMap<String,String> jobInfo);
    void startJob(Integer jobId);

    void stopJob(Integer jobId);

    Map<String,Object> loadJobInfo(Integer jobId);

    void updateJob(Integer jobId, String newCron);
    //public Integer getJobGroupId(String name, String title);
}
