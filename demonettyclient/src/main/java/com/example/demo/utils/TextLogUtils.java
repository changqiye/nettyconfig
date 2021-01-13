package com.example.demo.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

public class TextLogUtils {

    private static final Logger logger = LoggerFactory.getLogger(TextLogUtils.class);

    public synchronized static void writeLog(String log) {
        try {
            logger.info(log);
            log = DateUtil.date2String(new Date()) + "\n" + log + "\n";
            File file = new File("log.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWritter = new FileWriter(file.getName(), true);
            fileWritter.write(log);
            fileWritter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
