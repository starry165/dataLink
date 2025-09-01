package org.example.dataLink.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.dataLink.pojo.Result;
import org.example.dataLink.service.XxlJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/setJob")
public class SetJobController {

    @Autowired
    private XxlJobService xxlJobService;

    @GetMapping("/startJob")
    public Result startJob(@RequestParam Integer jobId){
        xxlJobService.startJob(jobId);
        return Result.success();
    }

    @GetMapping("/stopJob")
    public Result stopJob(@RequestParam Integer jobId){
        xxlJobService.stopJob(jobId);
        return Result.success();
    }

    @PostMapping("/updateCron")
    public Result updateCron(@RequestParam Integer jobId,@RequestParam String cron){
        xxlJobService.updateJob(jobId,cron);
        return Result.success();
    }


}
