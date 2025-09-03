package org.example.dataLink.tools;

import com.alibaba.fastjson.JSONObject;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DebeziumCDCJobHandler {

    @Autowired
    private DebeziumConnectorManager debeziumConnectorManager;


    /*{
        kafka:{
            servers: localhost:9092,
            historyTopic: cdc
        },
        reader:{
            username: root,
            password: root,
            jdbcUrl: jdbc:mysql://localhost:3306/test
            dbType: mysql,
            table: base,
            serverId: 6
        },
        writer:{
            username: root,
            password: root,
            jdbcUrl: jdbc:mysql://localhost:3306/test,
            dbType: mysql,
            table: base_sync
        }
    }
    * */
    @XxlJob("chunJunCDCJobHandler")
    public void runChunJunJob(String param){
        //String param= XxlJobHelper.getJobParam();
        JSONObject json=JSONObject.parseObject(param);

        String readerName=json.getJSONObject("reader").getString("dbType")+"-kafka-connector";
        String writerName="kafka-"+ json.getJSONObject("writer").getString("dbType")+"-connector";

        try{
            debeziumConnectorManager.createConnector(json,"kafka");
            try{
                debeziumConnectorManager.createConnector(json,"sql");
            }finally {
                debeziumConnectorManager.deleteConnector(writerName);
            }
        }finally {
            debeziumConnectorManager.deleteConnector(readerName);
        }
    }
}
