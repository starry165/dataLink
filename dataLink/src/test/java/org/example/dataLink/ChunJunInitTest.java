package org.example.dataLink;

import lombok.extern.slf4j.Slf4j;
import org.example.dataLink.tools.ChunJunInit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class ChunJunInitTest {

    @Test
    void test(){
        String path=ChunJunInit.CHUNJUN_HOME;
        log.info("jjjjjjjj"+ path);
    }

}
