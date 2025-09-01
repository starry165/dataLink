package org.example.dataLink.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Source {
    private String userName;
    private String passWord;
    private String jdbcUrl;
    private String dbType;
    private String table;
}
