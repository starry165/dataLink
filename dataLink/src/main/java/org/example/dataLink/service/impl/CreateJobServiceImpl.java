package org.example.dataLink.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.example.dataLink.mapper.DataSourceMapper;
import org.example.dataLink.mapper.JobInfoMapper;
import org.example.dataLink.pojo.Column;
import org.example.dataLink.pojo.CreateJobRequest;
import org.example.dataLink.pojo.DbConfig;
import org.example.dataLink.service.CreateJobService;
import org.example.dataLink.service.XxlJobService;
import org.example.dataLink.tools.PassWordTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

@Slf4j
@Service
public class CreateJobServiceImpl implements CreateJobService {

    @Autowired
    private DataSourceMapper dataSourceMapper;

    @Autowired
    private XxlJobService xxlJobService;

    @Autowired
    private JobInfoMapper jobInfoMapper;

    @Override
    public void createJob(CreateJobRequest request) {
        String param=createParams(request);
        MultiValueMap<String,String> jobInfo=createJobInfo(request,param);
        Integer jobId=xxlJobService.addJob(jobInfo);
        storageJobInfo(jobId,request);
    }

    private String createParams(CreateJobRequest request){
        DbConfig readSource=dataSourceMapper.findUrl(request.getReadUrl());
        DbConfig writeSource=dataSourceMapper.findUrl(request.getWriterUrl());
        List<Column> readCol=request.getReadCol();
        List<Column> writeCol=request.getWriteCol();

        JSONObject job=new JSONObject();
        job.put("jobType",request.getJobType());
        job.put("isCDC",request.getIsCDC());
        job.put("useMQ",request.getUseMQ());
        JSONObject reader=new JSONObject();
        reader.put("dbType",readSource.getDbType());
        reader.put("jdbcUrl",readSource.getUrl());
        reader.put("userName",readSource.getUserName());
        reader.put("passWord", PassWordTools.decrypt(readSource.getPassWord()));
        reader.put("table",request.getReadTable());
        job.put("reader",reader);

        JSONObject writer=new JSONObject();
        writer.put("dbType",writeSource.getDbType());
        writer.put("jdbcUrl",writeSource.getUrl());
        writer.put("userName",writeSource.getUserName());
        writer.put("passWord",PassWordTools.decrypt(writeSource.getPassWord()));
        writer.put("table",request.getWriteTable());
        job.put("writer",writer);

        JSONArray readerCol=new JSONArray();
        for(Column col:readCol){
            JSONObject colObj=new JSONObject();
            colObj.put("name",col.getName());
            colObj.put("type",col.getType());
            readerCol.add(colObj);
        }
        job.put("readerColum",readerCol);

        JSONArray writerCol=new JSONArray();
        for(Column col: writeCol){
            JSONObject colObj=new JSONObject();
            colObj.put("name",col.getName());
            colObj.put("type",col.getType());
            writerCol.add(colObj);
        }
        job.put("writerColum",writerCol);
        return job.toJSONString();
    }

    private MultiValueMap<String,String> createJobInfo(CreateJobRequest request,String param){
        MultiValueMap<String,String> jobInfo=new LinkedMultiValueMap<>();
        jobInfo.add("jobGroup","4");
        jobInfo.add("jobDesc",request.getJobName());
        jobInfo.add("author","admin");
        jobInfo.add("scheduleType", "CRON");
        jobInfo.add("scheduleConf",request.getCron());
        jobInfo.add("misfireStrategy", "DO_NOTHING");
        jobInfo.add("executorRouteStrategy", "ROUND");
        jobInfo.add("executorHandler","chunJunJobHandler");
        jobInfo.add("executorParam",param);
        jobInfo.add("executorBlockStrategy", "SERIAL_EXECUTION");
        jobInfo.add("executorTimeout", "0");
        jobInfo.add("executorFailRetryCount", "0");
        jobInfo.add("glueType", "BEAN");
        jobInfo.add("triggerStatus", "0");
        return jobInfo;
    }

    private void storageJobInfo(Integer jobId,CreateJobRequest request){
        String readerTable=request.getReadUrl()+" : "+request.getReadTable();
        String writerTable=request.getWriterUrl()+" : "+request.getWriteTable();
        jobInfoMapper.insert(jobId,request.getJobName(),readerTable,writerTable,"运行中");
    }
}
