package com.ly.demo.web.demoweb;

import com.example.demo.ehcatchServer.EhCatchHelper;
import com.example.demo.nettyDemo.ClientMain;
import com.example.demo.utils.TextLogUtils;
import com.ly.demo.web.utils.ThreadUtils;
import com.ly.demo.web.utils.ToolsClass;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;

@RestController
@EnableAutoConfiguration
@RequestMapping("/demoWeb")
public class DemoController {

    @RequestMapping("/sendMessageToServer")
    public String sendMessageToServer(@RequestParam("message") String message) {
        try {
            ThreadUtils.execute(new Runnable() {
                @Override
                public void run() {
                    ClientMain.sendMessageToServer(message);
                }
            });
            return "your message = " + message;
        } catch (Exception e) {
            return e.getMessage();
        }
    }


    @RequestMapping("/getValueByKey")
    public String getValueByKey(@RequestParam("key") String key) {
        try {
            return EhCatchHelper.get(key);
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
