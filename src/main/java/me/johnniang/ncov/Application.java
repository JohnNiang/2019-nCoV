package me.johnniang.ncov;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static me.johnniang.ncov.model.NcovConst.WORKSPACE_FOLDER;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        System.setProperty("spring.config.additional-location", "file:${user.home}/" + WORKSPACE_FOLDER + "/");

        SpringApplication.run(Application.class, args);
    }

}
