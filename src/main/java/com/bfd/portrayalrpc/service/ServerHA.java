package com.bfd.portrayalrpc.service;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by ronghua on 2015/7/29.
 */
public class ServerHA {
    private static final String SPRING_FILE_PATH = "demo-server.xml";
    private static boolean running = true;

    public static void main(String[] args) {
        try {
            new ClassPathXmlApplicationContext(SPRING_FILE_PATH);
            synchronized (ServerHA.class) {
                while (running) {
                    try {
                        ServerHA.class.wait();
                    } catch (Throwable a) {
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
