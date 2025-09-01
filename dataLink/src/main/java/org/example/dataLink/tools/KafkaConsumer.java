package org.example.dataLink.tools;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaConsumer {


    @KafkaListener(topics = "dbserver1.test.base")
    public void consumer(String message){
        JSONObject json=JSONObject.parseObject(message);

        String op=json.getString("__op");
        boolean delete=json.getBoolean("__deleted");
        Integer id=json.getInteger("id");
        String name=json.getString("name");

        if(op.equals("u")){

        }
    }
}
