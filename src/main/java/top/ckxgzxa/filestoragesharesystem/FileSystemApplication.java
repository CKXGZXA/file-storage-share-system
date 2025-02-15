package top.ckxgzxa.filestoragesharesystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static top.ckxgzxa.filestoragesharesystem.cmsdk.InitClient.initChainClient;

@SpringBootApplication
@MapperScan("top.ckxgzxa.filestoragesharesystem.mapper")
public class FileSystemApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(FileSystemApplication.class, args);
        initChainClient();
    }

}
