package org.example.dataLink.service.impl;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.example.dataLink.mapper.DataSourceMapper;
import org.example.dataLink.pojo.DbConfig;
import org.example.dataLink.pojo.PoolConfig;
import org.example.dataLink.service.DataSourceService;
import org.example.dataLink.tools.PassWordTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

@Slf4j
@Service
public class DataSourceServiceImpl implements DataSourceService {

    @Autowired
    private DataSourceMapper dataSourceMapper;


    private static final Map<String ,String> DRIVER_MAP=Map.of(
            "mysql", "com.mysql.cj.jdbc.Driver",
            "oracle", "oracle.jdbc.OracleDriver",
            "postgresql", "org.postgresql.Driver",
            "sqlserver", "com.microsoft.sqlserver.jdbc.SQLServerDriver"
    );

    private String turnDriver(String dbType) throws Exception {
        String driver=DRIVER_MAP.get(dbType.toLowerCase());
        if(driver == null){
            throw new Exception("不支持的数据库类型：" + dbType);
        }
        return driver;
    }

    private void storageDataSource(DbConfig dbConfig){
        String passWord = PassWordTools.encrypt(dbConfig.getPassWord());
        PoolConfig pool=dbConfig.getPoolConfig();
        dataSourceMapper.insert(dbConfig.getDbType(),dbConfig.getUrl(),dbConfig.getUserName(),passWord,dbConfig.getDataBaseName(),pool.getMaxPoolSize(),pool.getMinIdleSize(),pool.getConnectTimeout(),pool.getIdleTimeout());
    }

    private void testConnect(DataSource dataSource) throws SQLException {
        try(Connection connection=dataSource.getConnection();
            Statement statement=connection.createStatement()){
            statement.executeQuery("SELECT 1");
        }catch (Exception e){
            log.error("数据源创建失败：{}",dataSource,e);
            throw new SQLException("数据库连接测试失败：",e.getMessage());
        }
    }

    public DataSource createDataSource(DbConfig dbConfig) throws Exception {
        String driver=turnDriver(dbConfig.getDbType());
        HikariConfig hikariConfig=new HikariConfig();
        hikariConfig.setDriverClassName(driver);
        hikariConfig.setJdbcUrl(dbConfig.getUrl());
        hikariConfig.setUsername(dbConfig.getUserName());
        hikariConfig.setPassword(dbConfig.getPassWord());

        if(dbConfig.getPoolConfig()!=null){
            PoolConfig pool=dbConfig.getPoolConfig();
            hikariConfig.setMaximumPoolSize(pool.getMaxPoolSize());
            hikariConfig.setMinimumIdle(pool.getMinIdleSize());
            hikariConfig.setIdleTimeout(pool.getIdleTimeout());
            hikariConfig.setConnectionTimeout(pool.getConnectTimeout());
        }
        DataSource dataSource=new HikariDataSource(hikariConfig);
        testConnect(dataSource);
        storageDataSource(dbConfig);
        return dataSource;
    }
}
