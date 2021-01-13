package com.example.demo.init;

import com.example.demo.nettyDemo.NettyServerMain;
import com.example.demo.nettyDemo.utils.ThreadUtils;
import com.example.demo.zk.ZKClientInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class InitServer implements CommandLineRunner {
    @Autowired
    private ZKClientInfo zkClientInfo;
    @Autowired
    private NettyServerMain nettyServerMain;

    @Override
    public void run(String... args) throws Exception {
        ThreadUtils.execute(new Runnable() {
            @Override
            public void run() {
                zkClientInfo.initZkClient();
            }
        });
        ThreadUtils.execute(new Runnable() {
            @Override
            public void run() {
                nettyServerMain.start();
            }
        });
    }
}
