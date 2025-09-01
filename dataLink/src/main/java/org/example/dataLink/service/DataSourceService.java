package org.example.dataLink.service;

import org.example.dataLink.pojo.DbConfig;

import javax.sql.DataSource;
import java.sql.SQLException;

public interface DataSourceService {
    DataSource createDataSource(DbConfig dbConfig)throws Exception;
}
