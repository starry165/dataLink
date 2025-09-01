package org.example.dataLink.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.dataLink.service.JobConfigService;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

@Slf4j
@Service
public class JobConfigServiceImpl implements JobConfigService {
    public void createJob (DataSource source, DataSource target){

    }
}
