package org.example.dataLink.tools;

import org.example.dataLink.pojo.Column;
import org.example.dataLink.pojo.Source;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CDCToMQBuilder {

    public String buildChunJunJobJson(Source readerSource, Source writerSource, List<Column> readerColum, List<Column> writerColum){

    }
}
