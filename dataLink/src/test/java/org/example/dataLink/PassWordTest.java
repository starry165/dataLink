package org.example.dataLink;

import lombok.extern.slf4j.Slf4j;
import org.example.dataLink.tools.PassWordTools;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class PassWordTest {

    @Test
    void tes(){
        String test="abc123";
        String eTest=PassWordTools.encrypt(test);
        String dTest=PassWordTools.decrypt(eTest);
        log.info("后:{}",eTest);
        log.info("前:{}",dTest);
    }
}
