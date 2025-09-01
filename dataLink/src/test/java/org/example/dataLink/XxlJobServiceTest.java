package org.example.dataLink;

import lombok.extern.slf4j.Slf4j;
import org.example.dataLink.service.XxlJobService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;


@SpringBootTest
@Slf4j
public class XxlJobServiceTest {

    @Autowired
    private XxlJobService xxlJobService;

    @Test
    void addTest(){
        MultiValueMap<String,String> jobInfo=new LinkedMultiValueMap<>();
        jobInfo.add("jobGroup","4");
        jobInfo.add("jobDesc", "测试数据库同步任务");
        jobInfo.add("author", "system");
        jobInfo.add("scheduleType", "CRON");
        jobInfo.add("scheduleConf", "* 0/5 * * * ?"); // 每5分钟执行
        jobInfo.add("misfireStrategy", "DO_NOTHING");
        jobInfo.add("executorRouteStrategy", "ROUND");
        jobInfo.add("executorHandler", "TestJob"); // 你写的 Bean JobHandler 名称
        //jobInfo.add("executorParam", "{\"syncType\":\"mysql2pg\"}"); // ChunJun 参数
        jobInfo.add("executorBlockStrategy", "SERIAL_EXECUTION");
        jobInfo.add("executorTimeout", "0");
        jobInfo.add("executorFailRetryCount", "0");
        jobInfo.add("glueType", "BEAN");
        jobInfo.add("triggerStatus", "1"); // 默认运行
        Integer jobId=xxlJobService.addJob(jobInfo);
        log.info("jobId:{}",jobId);
    }

    @Test
    void startTest(){
        xxlJobService.startJob(13);
    }

    @Test
    void stopTest(){
        xxlJobService.stopJob(13);
    }

    @Test
    void update(){
        xxlJobService.updateJob(13,"* 0/5 * * * ?");
    }
}
