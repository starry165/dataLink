package org.example.dataLink.pojo;

import lombok.Data;

@Data
public class PoolConfig{
    private Integer maxPoolSize=10;
    private Integer minIdleSize=5;
    private Long connectTimeout=30000L;
    private Long idleTimeout=600000L;
}
