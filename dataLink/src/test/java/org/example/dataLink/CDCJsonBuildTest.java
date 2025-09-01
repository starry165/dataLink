package org.example.dataLink;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.example.dataLink.pojo.Column;
import org.example.dataLink.pojo.Source;
import org.example.dataLink.tools.ChunJunCDCJobBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Slf4j
public class CDCJsonBuildTest {

    @Autowired
    private ChunJunCDCJobBuilder chunJunCDCJobBuilder;

    @Test
    void test(){
        JSONObject job=new JSONObject();
        JSONObject reader=new JSONObject();
        reader.put("dbType","mysql");
        reader.put("jdbcUrl","jdbc:mysql://localhost:3306/test");
        reader.put("userName","root");
        reader.put("passWord","root");
        reader.put("table","base");
        job.put("reader",reader);

        JSONObject writer=new JSONObject();
        writer.put("dbType","mysql");
        writer.put("jdbcUrl","jdbc:mysql://localhost:3306/test");
        writer.put("userName","root");
        writer.put("passWord","root");
        writer.put("table","base_sync");
        job.put("writer",writer);

        JSONArray readerColum=new JSONArray();
        JSONObject col1=new JSONObject();
        col1.put("name","id");
        col1.put("type","int");
        JSONObject col2=new JSONObject();
        col2.put("name","name");
        col2.put("type","string");
        readerColum.add(col1);
        readerColum.add(col2);
        JSONArray writerColum = new JSONArray();
        writerColum.add(col1);
        writerColum.add(col2);
        job.put("readerColum",readerColum);
        job.put("writerColum",writerColum);

        log.info("param:{}",job.toJSONString());
        testA(job.toJSONString());

    }
    void testA(String param){
        JSONObject json= JSONObject.parseObject(param);
        Source reader=json.getObject("reader",Source.class);
        Source writer=json.getObject("writer",Source.class);
        List<Column> readerCols=new ArrayList<>();
        for(Object col:json.getJSONArray("readerColum")){
            JSONObject c=(JSONObject) col;
            readerCols.add(new Column(c.getString("name"),c.getString("type")));
        }
        List<Column> writerCols=new ArrayList<>();
        for(Object col:json.getJSONArray("writerColum")){
            JSONObject c=(JSONObject) col;
            writerCols.add(new Column(c.getString("name"),c.getString("type")));
        }
        String jobJson= chunJunCDCJobBuilder.buildChunJunJobJson(reader,writer,readerCols,writerCols);
        log.info("ChunJunJson:{}", jobJson);
    }
}
