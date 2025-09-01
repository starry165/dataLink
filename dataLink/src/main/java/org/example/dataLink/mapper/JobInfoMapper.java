package org.example.dataLink.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface JobInfoMapper {
    @Insert("INSERT INTO jobinfo (jobId, jobName, readerTable, writerTable, state) VALUES (#{jobId},#{jobName},#{readerTable},#{writerTable},#{state})")
    void insert(Integer jobId, String jobName, String readerTable, String writerTable, String state);

    @Update("UPDATE jobinfo SET state=#{state} WHERE jobId=#{jobId}")
    void updateState(Integer jobId, String state);
}
