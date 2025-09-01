package org.example.dataLink;

import lombok.extern.slf4j.Slf4j;
import org.example.dataLink.mapper.DataSourceMapper;
import org.example.dataLink.pojo.DbConfig;
import org.example.dataLink.service.DataSourceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class DataSourceMapperTest {

    @Autowired
    private DataSourceMapper dataSourceMapper;

    @Test
    void test(){
        DbConfig db=dataSourceMapper.findUrl("jdbc:mysql://localhost:3306/mall");
        log.info("DbConfig:{}",db);
    }
}
