package org.example.dataLink.service;

import javax.sql.DataSource;

public interface JobConfigService {
    void createJob (DataSource source, DataSource target);
}
