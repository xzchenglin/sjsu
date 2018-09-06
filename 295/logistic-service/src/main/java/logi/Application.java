package logi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Application {

    static ApplicationContext ctx;

    public static ApplicationContext getCtx() {
        return ctx;
    }

    public static void main(String[] args) {
        ctx = SpringApplication.run(Application.class, args);
    }
}