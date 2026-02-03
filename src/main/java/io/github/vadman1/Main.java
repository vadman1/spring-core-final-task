package io.github.vadman1;

import io.github.vadman1.config.AppConfig;
import io.github.vadman1.service.OperationsConsoleListener;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        OperationsConsoleListener operationsConsoleListener = context.getBean(OperationsConsoleListener.class);
        Thread thread = new Thread(operationsConsoleListener);
        thread.start();
    }
}