package org.example.dataLink.pojo;

import lombok.Data;

@Data
public class DbConfig {
    private String dbType;
    private String url;
    private String userName;
    private String passWord;
    private String dataBaseName;
    private PoolConfig poolConfig;
}
