package application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.Generated;

@SpringBootApplication
@ComponentScan("web")
@ComponentScan("application")
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
