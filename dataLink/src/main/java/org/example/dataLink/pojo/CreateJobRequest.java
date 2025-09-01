package org.example.dataLink.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateJobRequest {
    private String jobName;
    private String jobType;
    private String isCDC;
    private String useMQ;
    private String readUrl;
    private String writerUrl;
    private String readTable;
    private String writeTable;
    private String cron;
    private List<Column> readCol;
    private List<Column> writeCol;
}
