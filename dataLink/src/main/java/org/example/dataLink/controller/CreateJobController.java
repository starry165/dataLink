package org.example.dataLink.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.dataLink.pojo.Column;
import org.example.dataLink.pojo.CreateJobRequest;
import org.example.dataLink.pojo.Result;
import org.example.dataLink.service.CreateJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/createJob")
public class CreateJobController {

    @Autowired
    private CreateJobService createJobService;

    @PostMapping
    public Result createJob(@RequestBody CreateJobRequest request){
        createJobService.createJob(request);
        return Result.success();
    }
}
