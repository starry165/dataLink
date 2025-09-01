package org.example.dataLink.tools;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.example.dataLink.mapper.ServerIdMapper;
import org.example.dataLink.pojo.Column;
import org.example.dataLink.pojo.ServerId;
import org.example.dataLink.pojo.Source;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class ChunJunCDCJobBuilder {

    @Autowired
    private ServerIdMapper serverIdMapper;

    public String buildChunJunJobJson(Source readerSource, Source writerSource, List<Column> readerColum, List<Column> writerColum){
        JSONObject job=new JSONObject();
        JSONObject jobObj=new JSONObject();
        JSONArray connect=new JSONArray();
        JSONObject conObj=new JSONObject();

        JSONObject reader=new JSONObject();
        reader.put("name","mysqlcdcreader");
        JSONObject param=new JSONObject();
        String[] readerUrl=readerSource.getJdbcUrl().split("/");
        param.put("host",readerUrl[2].split(":")[0]);
        param.put("port",Integer.parseInt(readerUrl[2].split(":")[1]));
        ServerId serverId=new ServerId(readerSource.getJdbcUrl());
        serverIdMapper.insert(serverId);
        param.put("serverId",serverId.getId());
        JSONArray databaseList=new JSONArray();
        databaseList.add(readerUrl[readerUrl.length-1]);
        param.put("databaseList",databaseList);
        JSONArray tableList=new JSONArray();
        tableList.add(readerUrl[readerUrl.length-1]+"."+readerSource.getTable());
        param.put("tableList",tableList);
        param.put("username",readerSource.getUserName());
        param.put("password",readerSource.getPassWord());
        JSONArray cols=new JSONArray();
        for(Column col: readerColum){
            JSONObject obj=new JSONObject();
            obj.put("name",col.getName());
            obj.put("type",col.getType());
            cols.add(col);
        }
        param.put("column",cols);
        param.put("writeMode","update");
        reader.put("parameter",param);
        JSONObject readerTable=new JSONObject();
        readerTable.put("tableName",readerSource.getTable());
        reader.put("table",readerTable);
        conObj.put("reader",reader);

        JSONObject writer=new JSONObject();
        JSONObject wParam=new JSONObject();
        wParam.put("username",writerSource.getUserName());
        wParam.put("password",writerSource.getPassWord());
        JSONArray connection=new JSONArray();
        JSONObject connectionObj=new JSONObject();
        connectionObj.put("jdbcUrl",writerSource.getJdbcUrl());
        JSONArray writerTable=new JSONArray();
        writerTable.add(writerSource.getTable());
        connectionObj.put("table",writerTable);
        connection.add(connectionObj);
        wParam.put("connection",connection);
        JSONArray clos=new JSONArray();
        for(Column col:writerColum){
            JSONObject obj=new JSONObject();
            obj.put("name",col.getName());
            obj.put("type",col.getType());
            clos.add(obj);
        }
        wParam.put("column",clos);
        wParam.put("writeMode","update");
        writer.put("parameter",wParam);
        JSONObject table=new JSONObject();
        table.put("tableName",writerSource.getTable());
        writer.put("table",table);
        writer.put("name","mysqlwriter");
        conObj.put("writer",writer);

        JSONObject transformer=new JSONObject();
        transformer.put("transformSql","select id,name from base");
        conObj.put("transformer",transformer);
        connect.add(conObj);
        jobObj.put("content",connect);

        JSONObject setting=new JSONObject();
        JSONObject errorLimit=new JSONObject();
        errorLimit.put("record",100);
        setting.put("errorLimit",errorLimit);
        JSONObject speed=new JSONObject();
        speed.put("bytes",0);
        speed.put("channel",1);
        speed.put("readerChannel",1);
        speed.put("writerChannel",1);
        setting.put("speed",speed);
        jobObj.put("setting",setting);
        job.put("job",jobObj);

        return job.toJSONString();
    }
}
