package org.example.dataLink.tools;

import com.alibaba.fastjson.JSONObject;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ChunJunCDCJobHandler {

    @Autowired
    private DebeziumConnectorManager debeziumConnectorManager;


    @XxlJob("chunJunCDCJobHandler")
    public void runChunJunJob(){
        String param= XxlJobHelper.getJobParam();
        JSONObject json=JSONObject.parseObject(param);

        JSONObject debeziumConfig=json.getJSONObject("debezium");
        String serverName = debeziumConfig.getString("serverName");

        try{
            debeziumConnectorManager.createConnector(debeziumConfig);

        }finally {
            debeziumConnectorManager.deleteConnector(serverName);
        }
    }
}
