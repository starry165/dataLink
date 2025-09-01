package org.example.dataLink;

import org.example.dataLink.pojo.CreateJobRequest;
import org.example.dataLink.service.CreateJobService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

@SpringBootTest
public class CreateJobTest {

    @Autowired
    private CreateJobService createJobService;

    @Test
    void test(){
        CreateJobRequest request=new CreateJobRequest("test","stonle","jdbc:mysql://localhost:3306/test","jdbc:mysql://localhost:3306/test","1","2","3",new ArrayList<>(),new ArrayList<>());
        createJobService.createJob(request);
    }
}
