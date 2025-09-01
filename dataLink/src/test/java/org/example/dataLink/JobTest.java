package org.example.dataLink;

import lombok.extern.slf4j.Slf4j;
import org.example.dataLink.tools.XXLJobTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class JobTest {

    @Autowired
    private XXLJobTest job;

    @Test
    void test(){
        job.testJob();
    }

    @Test
    void jobT(){
        String url="jdbc:mysql://localhost:3306/mall";
        String[] str=url.split("/");
        for(String s:str){
            log.info("Url:{}",s);
        }
    }
}
