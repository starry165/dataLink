package org.example.dataLink.tools;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Stream;

@Component
@Slf4j
public class ChunJunInit {
    public static String CHUNJUN_HOME;

    @PostConstruct
    public void init() throws IOException, URISyntaxException {
        File targetDir=new File(System.getProperty("java.io.tmpdir"),"ChunJun");
        if(targetDir.exists()){
            CHUNJUN_HOME=targetDir.getAbsolutePath();
            log.info("ChunJun已存在，跳过复制{}",CHUNJUN_HOME);
            return;
        }else
            targetDir.mkdirs();

        try(FileSystem fs= FileSystems.newFileSystem(Objects.requireNonNull(getClass().getClassLoader().getResource("ChunJun")).toURI(), Collections.emptyMap())){
            Path jarPath=fs.getPath("ChunJun");
            copy(jarPath,targetDir.toPath());
        }catch (Exception e){
            Path src= Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource("ChunJun")).toURI());
            copy(src,targetDir.toPath());
        }
        CHUNJUN_HOME=targetDir.getAbsolutePath();
        log.info("ChunJun初始化路径：{}", CHUNJUN_HOME);
    }

    private void copy(Path source,Path target) throws IOException{
        try(Stream<Path> stream = Files.walk(source)){
            stream.forEach(path -> {
                try{
                    Path dest=target.resolve(source.relativize(path).toString());
                    if(Files.isDirectory(path)){
                        if(!Files.exists(dest)){
                            Files.createDirectories(dest);
                        }
                    }else {
                        Files.copy(path,dest,StandardCopyOption.REPLACE_EXISTING);
                    }
                }catch (IOException e){
                    throw new RuntimeException(e);
                }
            });
        }
    }
}
