package com.bfd.portrayalrpc.client;

import com.alibaba.fastjson.JSONObject;
import com.bfd.portrayalrpc.thrift.PortrayalService.Iface;
import com.bfd.portrayalrpc.thrift.ReqType;
import com.bfd.portrayalrpc.thrift.Result;
import org.apache.thrift.TException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by ronghua on 2015/7/29.
 */
public class ClientDemoHA  implements Runnable {
    private static final String SPRING_FILE_PATH = "demo-client.xml";
    private static String req = "";
    private static Iface portrayalService;

    public void run() {
        Result result = null;
        try {
            result = portrayalService.get_data(req);
            System.out.println(result);
        } catch (TException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            ApplicationContext context = new ClassPathXmlApplicationContext(SPRING_FILE_PATH);
            portrayalService = (Iface)context.getBean("portrayalService");
            String cid = "";
            String iid = "";
            ReqType req_type = ReqType.ITEM;
            JSONObject obj_request = new JSONObject();
            obj_request.put("cid", cid);
            obj_request.put("iid", iid);
            obj_request.put("req_type", req_type);
            req = obj_request.toJSONString();

            ExecutorService exec = Executors.newFixedThreadPool(5);
            for (int i = 0; i < 1; i++) {
                exec.execute(new ClientDemoHA());
            }
            exec.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
