package org.example.dataLink.tools;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xxl.job.core.context.XxlJobHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.Set;

@Component
@Slf4j
public class GetFlinkLog {

    private final RestTemplate restTemplate=new RestTemplate();

    public void getLog(String jobId,Long timeOut){
        Set<String> logHistory =new HashSet<>();
        try{
            while(true){
                String flinkUrl = "http://localhost:8081";
                String jobDetailedUrl= flinkUrl +"/jobs/"+jobId;
                ResponseEntity<String> jobResp=restTemplate.getForEntity(jobDetailedUrl,String.class);
                if(!jobResp.getStatusCode().is2xxSuccessful()){
                    XxlJobHelper.log("获取job信息失败:{}",jobResp);
                    break;
                }
                JSONObject jobObject=JSONObject.parseObject(jobResp.getBody());
                String jobState=jobObject.getString("state");
                XxlJobHelper.log("[Flink-{}] 当前状态:{}",jobId,jobState);

                String taskUrl= flinkUrl +"/taskmanagers";
                ResponseEntity<String> taskResp=restTemplate.getForEntity(taskUrl,String.class);
                JSONObject taskJson=JSONObject.parseObject(taskResp.getBody());
                JSONArray taskArr=taskJson.getJSONArray("taskmanagers");
                if(taskArr==null||taskArr.isEmpty()){
                    XxlJobHelper.log("未找到 TaskManagers");
                    break;
                }
                String taskId=taskArr.getJSONObject(0).getString("id");

                String logUrl= flinkUrl +"/taskmanagers/"+taskId+"/log";
                ResponseEntity<String> logResp=restTemplate.getForEntity(logUrl,String.class);
                if(logResp.getStatusCode().is2xxSuccessful()){
                    String logConnect=logResp.getBody();
                    for(String line: logConnect.split("\n")){
                        if(line.contains(jobId)&&!logHistory.contains(line)){
                            logHistory.add(line);
                            XxlJobHelper.log("[Flink-{}] {}",jobId,line);
                        }
                    }
                }

                if("FINISHED".equalsIgnoreCase(jobState)||"FAILED".equalsIgnoreCase(jobState)||"CANCELED".equalsIgnoreCase(jobState)){
                    XxlJobHelper.log("[Flink-{}] 任务已结束",jobId);
                    break;
                }
                Thread.sleep(timeOut);
            }
        }catch (Exception e){
            XxlJobHelper.log("[Flink-{}] Flink日志拉取异常:{}",jobId,e.getMessage());
        }
    }
}
