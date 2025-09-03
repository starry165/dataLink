package org.example.dataLink.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.example.dataLink.pojo.CdcKafkaJob;

@Mapper
public interface CdcKafkaMapper {

    @Insert("INSERT INTO cdcKafkaJob (jobName, cdcKafkaName, kafkaSqlName) VALUES (#{jobName},#{cdcKafkaName},#{kafkaSqlName})")
    void insert(String jobName, String cdcKafkaName,String kafkaSqlName);

    CdcKafkaJob list(Integer id);

    @Delete("DELETE FROM cdcKafkaJob WHERE id=#{id}")
    void delete(Integer id);
}
