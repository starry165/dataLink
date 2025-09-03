package org.example.dataLink.service.impl;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.example.dataLink.mapper.CdcKafkaMapper;
import org.example.dataLink.mapper.DataSourceMapper;
import org.example.dataLink.mapper.ServerIdMapper;
import org.example.dataLink.pojo.*;
import org.example.dataLink.service.CdcKafkaJobService;
import org.example.dataLink.tools.DebeziumConnectorManager;
import org.example.dataLink.tools.PassWordTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CdcKafkaJobServiceImpl implements CdcKafkaJobService {

    @Autowired
    private DebeziumConnectorManager debeziumConnectorManager;

    @Autowired
    private DataSourceMapper dataSourceMapper;

    @Autowired
    private ServerIdMapper serverIdMapper;

    @Autowired
    private CdcKafkaMapper cdcKafkaMapper;

    @Override
    public Result creatJob(CdcKafkaRequest request) {
        JSONObject param=new JSONObject();
        JSONObject kafka=new JSONObject();
        kafka.put("servers",request.getKafkaServer());
        kafka.put("historyTopic",request.getHistoryTopic());
        param.put("kafka",kafka);

        JSONObject reader=new JSONObject();
        DbConfig readerConfig=dataSourceMapper.findUrl(request.getReaderUrl());
        ServerId serverId=new ServerId(request.getReaderUrl());
        serverIdMapper.insert(serverId);
        reader.put("username",readerConfig.getUserName());
        reader.put("password", PassWordTools.decrypt(readerConfig.getPassWord()));
        reader.put("jdbcUrl",request.getReaderUrl());
        reader.put("dbType",readerConfig.getDbType());
        reader.put("database",readerConfig.getDataBaseName());
        reader.put("table",request.getReaderTable());
        reader.put("serverId",String.valueOf(serverId.getId()));
        param.put("reader",reader);

        JSONObject writer=new JSONObject();
        DbConfig writerConfig=dataSourceMapper.findUrl(request.getWriterUrl());
        writer.put("username",writerConfig.getUserName());
        writer.put("password",PassWordTools.decrypt(writerConfig.getPassWord()));
        writer.put("jdbcUrl",request.getWriterUrl());
        writer.put("dbType",writerConfig.getDbType());
        writer.put("database",writerConfig.getDataBaseName());
        writer.put("table",request.getWriterTable());
        param.put("writer",writer);

        Result kafkaResult=debeziumConnectorManager.createConnector(param,"kafka");
        if(kafkaResult.getCode()==1){
            Result sqlResult=debeziumConnectorManager.createConnector(param,"sql");
            if(sqlResult.getCode()==1){
                String jobName=readerConfig.getDbType()+"cdc:"+reader.getString("database")+":"+request.getReaderTable()+"->"+writerConfig.getDbType()+":"+writer.getString("database")+":"+request.getWriterTable();
                String cdcKafkaName=reader.getString("dbType")+"cdc:"+reader.getString("database")+":"+reader.getString("table")+"-kafka-connector";
                String kafkaSqlName="kafka-"+writer.getString("dbType")+":"+writer.getString("database")+":"+writer.getString("table")+"-connector";
                cdcKafkaMapper.insert(jobName,cdcKafkaName,kafkaSqlName);
                return Result.success(jobName+"任务创建成功");
            }else {
                return sqlResult;
            }
        }else {
            return kafkaResult;
        }
    }

    @Override
    public Result stopJob(Integer id) {
        CdcKafkaJob job=cdcKafkaMapper.list(id);
        Result kafkaResult=debeziumConnectorManager.deleteConnector(job.getCdcKafkaName());
        Result sqlResult=debeziumConnectorManager.deleteConnector(job.getKafkaSqlName());
        String message="";
        if(kafkaResult.getCode()==0){
            message+=kafkaResult.getMsg();
        }
        if(sqlResult.getCode()==0){
            message+=sqlResult.getMsg();
        }
        if(message.isEmpty()){
            message="任务删除成功";
            cdcKafkaMapper.delete(id);
            return Result.success(message);
        }else {
            return Result.error(message);
        }
    }
}
