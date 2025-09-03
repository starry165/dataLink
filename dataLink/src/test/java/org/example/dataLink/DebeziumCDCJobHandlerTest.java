package org.example.dataLink;

import org.example.dataLink.tools.DebeziumCDCJobHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DebeziumCDCJobHandlerTest {

    @Autowired
    private DebeziumCDCJobHandler debeziumCDCJobHandler;

    @Test
    void test(){
        String param="{\"reader\":{\"password\":\"root\",\"jdbcUrl\":\"jdbc:mysql://localhost:3306/test\",\"dbType\":\"mysql\",\"serverId\":14,\"table\":\"base\",\"username\":\"root\"},\"kafka\":{\"servers\":\"localhost:9092\",\"historyTopic\":\"cdc\"},\"writer\":{\"password\":\"root\",\"jdbcUrl\":\"jdbc:mysql://localhost:3306/test\",\"dbType\":\"mysql\",\"table\":\"base_sync\",\"username\":\"root\"}}";
        debeziumCDCJobHandler.runChunJunJob(param);
    }
}
