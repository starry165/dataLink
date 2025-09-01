package org.example.dataLink.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.dataLink.pojo.DbConfig;

@Mapper
public interface DataSourceMapper {

    @Insert("INSERT INTO datasource (dbType, url, userName, passWord, dataBaseName, maxPoolSize, minIdleSize, connectTimeout, idleTimeout) VALUES (#{dbType},#{url},#{userName},#{passWord},#{dataBaseName},#{maxPoolSize},#{minIdleSize},#{connectTimeout},#{idleTimeout})")
    void insert(String dbType,String url,String userName,String passWord,String dataBaseName,Integer maxPoolSize,Integer minIdleSize,Long connectTimeout,Long idleTimeout);

    @Select("SELECT * FROM datasource WHERE url=#{url}")
    DbConfig findUrl(String url);
}
