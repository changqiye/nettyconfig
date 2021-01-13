package com.ly.demo.web.demoweb;

import com.example.demo.nettyDemo.ClientMain;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Init implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        System.err.println("start");
        try {
            new Thread(() -> {
                try {
                    Class.forName("com.example.demo.nettyDemo.ClientMain");
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }).start();
            new Thread(() -> {
                try {
                    Class.forName("com.example.demo.ehcatchServer.EhCatchHelper");
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }).start();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
