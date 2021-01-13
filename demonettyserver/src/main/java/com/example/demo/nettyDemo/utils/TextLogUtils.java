package com.example.demo.nettyDemo.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class TextLogUtils {
  private static final Logger logger = LoggerFactory.getLogger(TextLogUtils.class);

    public synchronized static void writeLog(String log) {
        logger.info(log);
        try {
            String logStr = DateUtil.date2String(new Date()) + "\n" + log + "\n";
            File file = new File("log.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWritter = new FileWriter(file.getName(), true);
            fileWritter.write(logStr);
            System.err.println(logStr);
            fileWritter.close();
        } catch (IOException e) {

        }
    }
}