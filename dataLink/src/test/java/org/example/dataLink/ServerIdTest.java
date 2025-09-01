package org.example.dataLink;

import lombok.extern.slf4j.Slf4j;
import org.example.dataLink.mapper.ServerIdMapper;
import org.example.dataLink.pojo.ServerId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class ServerIdTest {

    @Autowired
    private ServerIdMapper serverIdMapper;

    @Test
    void test(){
        ServerId serverId=new ServerId("b");
        serverIdMapper.insert(serverId);
        log.info("serverId:{}",serverId.getId());
    }
}
