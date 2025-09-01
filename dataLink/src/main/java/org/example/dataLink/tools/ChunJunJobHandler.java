package org.example.dataLink.tools;

import com.alibaba.fastjson.JSONObject;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.example.dataLink.pojo.Column;
import org.example.dataLink.pojo.Source;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class ChunJunJobHandler {

    @Autowired
    private ChunJunJobBuilder chunJunJobBuilder;

    @Autowired
    private ChunJunCDCJobBuilder chunJunCDCJobBuilder;

    @XxlJob("chunJunJobHandler")
    public void runChunJunJob() throws Exception {
        String param = XxlJobHelper.getJobParam();
        JSONObject json= JSONObject.parseObject(param);
        Source reader=json.getObject("reader",Source.class);
        Source writer=json.getObject("writer",Source.class);
        List<Column> readerCols=new ArrayList<>();
        for(Object col:json.getJSONArray("readerColum")){
            JSONObject c=(JSONObject) col;
            readerCols.add(new Column(c.getString("name"),c.getString("type")));
        }
        List<Column> writerCols=new ArrayList<>();
        for(Object col:json.getJSONArray("writerColum")){
            JSONObject c=(JSONObject) col;
            writerCols.add(new Column(c.getString("name"),c.getString("type")));
        }

        String jobType=json.getObject("jobType",String.class);
        String useMQ=json.getObject("useMQ",String.class);
        String isCDC=json.getObject("isCDC",String.class);
        String jobJson;
        if(isCDC.equals("normal"))
            jobJson= chunJunJobBuilder.buildChunJunJobJson(reader,writer,readerCols,writerCols);
        else
            jobJson= chunJunCDCJobBuilder.buildChunJunJobJson(reader,writer,readerCols,writerCols);

        commitJob(jobJson,jobType);
    }

    private void commitJob(String jobJson,String jobType) throws Exception{
        String jobFile="/tmp/chunJun_job_"+System.currentTimeMillis()+".json";
        Files.write(Paths.get(jobFile),jobJson.getBytes());

        String chunJunHome=ChunJunInit.CHUNJUN_HOME;
        String shPath=chunJunHome+"/bin/chunjun-"+jobType+"-1.8.sh";
        log.info("shPath:{}",shPath);
        ProcessBuilder pb=new ProcessBuilder(
                "sh",shPath,
                "-job",jobFile,
                "-mode",jobType,
                "-jobType","sync"
        );
        pb.directory(new File(chunJunHome));
        pb.redirectErrorStream(true);

        String jobId;
        Process process=pb.start();
        try(BufferedReader buffer=new BufferedReader(new InputStreamReader(process.getInputStream()))){
            String line;
            while((line=buffer.readLine())!=null){
                XxlJobHelper.log("[ChunJun] {}",line);
                if(line.contains("jobID =")){
                    jobId=line.substring(line.indexOf("jobID =")+7).trim();
                    XxlJobHelper.log("创建flink任务成功，jobId={}",jobId);
                }
            }
        }

        int exitNode=process.waitFor();
        XxlJobHelper.log(">>>ChunJun进程结束，退出码={}",exitNode);
        if(exitNode!=0){
            throw new RuntimeException("ChunJun任务执行失败,exitNode="+exitNode);
        }
    }
}
