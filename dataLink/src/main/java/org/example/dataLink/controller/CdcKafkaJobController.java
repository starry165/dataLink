package org.example.dataLink.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.dataLink.pojo.CdcKafkaRequest;
import org.example.dataLink.pojo.Result;
import org.example.dataLink.service.CdcKafkaJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/cdcKafkaJob")
public class CdcKafkaJobController {

    @Autowired
    private CdcKafkaJobService cdcKafkaJobService;

    @PostMapping("/create")
    public Result createJob(@RequestBody CdcKafkaRequest request){
        return cdcKafkaJobService.creatJob(request);
    }

    @PostMapping("/stop")
    public Result stopJob(@RequestParam Integer id){
        return cdcKafkaJobService.stopJob(id);
    }
}
