package com.gamehouse.project;

import com.gamehouse.project.services.APICallService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;


@SpringBootApplication()
public class ProjectApplication {

    public static void main(String[] args) throws Exception {
//        APICallService test = new APICallService();
//        test.getCover(352610);
//        test.getGames("metal gear solid");
        SpringApplication.run(ProjectApplication.class, args);
    }

}
