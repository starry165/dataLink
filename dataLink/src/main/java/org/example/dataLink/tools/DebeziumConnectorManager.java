package org.example.dataLink.tools;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class DebeziumConnectorManager {

    private final RestTemplate restTemplate=new RestTemplate();
    private final String connectUrl="http://localhost:8083/connectors";

    public void createConnector(JSONObject config){
        String connectorName=config.getString("serverName")+"-connector";
        String url=connectUrl+"/"+connectorName+"/config";

        JSONObject payload=new JSONObject();
        payload.put("connector.class", "io.debezium.connector.mysql.MySqlConnector");
        payload.put("database.hostname", config.getString("hostname"));
        payload.put("database.port", config.getString("port"));
        payload.put("database.user", config.getString("user"));
        payload.put("database.password", config.getString("password"));
        payload.put("database.server.id", config.getString("serverId"));
        payload.put("database.server.name", config.getString("serverName"));
        payload.put("database.include.list", config.getString("databaseList"));
        payload.put("topic.prefix",config.getString("serverName"));
        payload.put("table.include.list", config.getString("tableList"));
        payload.put("schema.history.internal.kafka.bootstrap.servers", config.getString("kafkaServers"));
        payload.put("schema.history.internal.kafka.topic", config.getString("historyTopic"));

        HttpHeaders headers=new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        try{
            ResponseEntity<String> resp=restTemplate.exchange(url, HttpMethod.PUT,new HttpEntity<>(payload.toJSONString(),headers),String.class);
            log.info("Debezium Connector [{}] 创建/更新成功: {}", connectorName, resp.getBody());
        }catch (Exception e){
            log.error("创建/更新 Debezium Connector [{}] 失败", connectorName, e);
            throw new RuntimeException(e);
        }
    }

    public void deleteConnector(String serverName){
        String connectorName=serverName+"-connector";
        String url=connectUrl+"/"+connectorName;

        try{
            restTemplate.delete(url);
            log.info("Debezium Connector [{}] 已删除", connectorName);
        }catch (Exception e){
            log.warn("删除 Debezium Connector [{}] 失败: {}", connectorName, e.getMessage());
        }
    }
}
