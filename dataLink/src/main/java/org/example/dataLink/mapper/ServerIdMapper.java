package org.example.dataLink.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.example.dataLink.pojo.ServerId;

@Mapper
public interface ServerIdMapper {
    @Insert("INSERT INTO serverid (tag) VALUES (#{tag})")
    @Options(useGeneratedKeys = true,keyColumn ="id",keyProperty = "id")
    int insert(ServerId serverId);
}
