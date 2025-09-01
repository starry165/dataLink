package org.example.dataLink.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServerId {
    private Integer id;
    private String tag;
    public ServerId(String s){
        tag=s;
    }
}
