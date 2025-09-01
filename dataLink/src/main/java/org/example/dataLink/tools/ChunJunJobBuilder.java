package org.example.dataLink.tools;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.example.dataLink.pojo.Column;
import org.example.dataLink.pojo.Source;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ChunJunJobBuilder {

    public String buildChunJunJobJson(Source readerSource, Source writerSource, List<Column> readerColum, List<Column> writerColum){
        JSONObject job=new JSONObject();

        JSONObject reader=new JSONObject();
        reader.put("name",readerSource.getDbType()+"reader");
        JSONObject readerParameter=new JSONObject();
        JSONArray readerCols=new JSONArray();
        for(Column col: readerColum){
            JSONObject j=new JSONObject();
            j.put("name",col.getName());
            j.put("type",col.getType());
            readerCols.add(j);
        }
        readerParameter.put("column",readerCols);
        readerParameter.put("where","");
        //readerParameter.put("splitPk","id");
        readerParameter.put("polling",true);
        readerParameter.put("pollingInterval",3000);
        readerParameter.put("queryTimeOut",1000);
        readerParameter.put("username",readerSource.getUserName());
        readerParameter.put("password",readerSource.getPassWord());

        JSONArray readerCon=new JSONArray();
        JSONObject conObj=new JSONObject();
        JSONArray jdbcUrl=new JSONArray();
        JSONArray table=new JSONArray();
        jdbcUrl.add(readerSource.getJdbcUrl());
        table.add(readerSource.getTable());
        conObj.put("jdbcUrl",jdbcUrl);
        conObj.put("table",table);
        readerCon.add(conObj);
        readerParameter.put("connection",readerCon);
        reader.put("parameter",readerParameter);

        JSONObject writer=new JSONObject();
        writer.put("name",writerSource.getDbType()+"writer");
        JSONObject writerParameter=new JSONObject();
        writerParameter.put("username",writerSource.getUserName());
        writerParameter.put("password",writerSource.getPassWord());
        JSONArray writerCon=new JSONArray();
        JSONObject writerConObj=new JSONObject();
        JSONArray writerTable=new JSONArray();
        writerTable.add(writerSource.getTable());
        writerConObj.put("jdbcUrl",writerSource.getJdbcUrl());
        writerConObj.put("table",writerTable);
        writerCon.add(writerConObj);
        writerParameter.put("connection",writerCon);
        writerParameter.put("writeMode","update");
        writerParameter.put("flushIntervalMills",3000);
        JSONArray writerCols=new JSONArray();
        for(Column col: writerColum){
            JSONObject j=new JSONObject();
            j.put("name",col.getName());
            j.put("type",col.getType());
            writerCols.add(j);
        }
        writerParameter.put("column",writerCols);
        writer.put("parameter",writerParameter);

        JSONArray content=new JSONArray();
        JSONObject contentObj=new JSONObject();
        contentObj.put("reader",reader);
        contentObj.put("writer",writer);
        content.add(contentObj);

        JSONObject setting=new JSONObject();
        JSONObject speed=new JSONObject();
        speed.put("channel",1);
        speed.put("bytes",0);
        setting.put("speed",speed);

        JSONObject jobObj=new JSONObject();
        jobObj.put("content",content);
        jobObj.put("setting",setting);
        job.put("job",jobObj);

        return job.toJSONString();
    }
}
