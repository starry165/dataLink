package org.example.dataLink.tools;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
public class XXLJobTest {

    @XxlJob("TestJob")
    public void testJob(){
        XxlJobHelper.log("start");
    }
}
