package org.example.dataLink.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.dataLink.pojo.DbConfig;
import org.example.dataLink.pojo.Result;
import org.example.dataLink.service.DataSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;

@Slf4j
@RestController
@RequestMapping("/dataSource")
public class DataSourceController {

    @Autowired
    private DataSourceService dataSourceService;

    @PostMapping
    public Result createDataSource(@RequestBody DbConfig dbConfig) {
        try{
            DataSource dataSource= dataSourceService.createDataSource(dbConfig);
            return Result.success();
        }catch(Exception e){
            return Result.error("创建失败:"+e.getMessage());
        }
    }
}
