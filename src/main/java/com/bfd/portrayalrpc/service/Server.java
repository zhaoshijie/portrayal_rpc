package com.bfd.portrayalrpc.service;

import com.bfd.portrayalrpc.thrift.PortrayalService;
import com.bfd.portrayalrpc.util.SysConfig;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ronghua on 2015/7/14.
 */
public class Server {
    private static final Logger log = LoggerFactory.getLogger(Server.class);

    public void startServer() {
        try {
            log.info("ResultService TSimpleServer start ....");
            //设置传输通道，普通通道
            TServerTransport serverTransport = new TServerSocket(SysConfig.THRIFT_SERVICE_PORT);
            System.out.println("server port:" + SysConfig.THRIFT_SERVICE_PORT);

            //使用高密度二进制协议
            TProtocolFactory proFactory = new TBinaryProtocol.Factory();

            //设置处理器PortrayalServiceImpl
            TProcessor processor = new PortrayalService.Processor(new PortrayalServiceImpl());

            //创建服务器
            TServer server = new TThreadPoolServer(
                    new TThreadPoolServer.Args(serverTransport)
                            .protocolFactory(proFactory)
                            .processor(processor)
            );

            log.info("Start server on port " + SysConfig.THRIFT_SERVICE_PORT + "...");
            server.serve();

        } catch (Exception e) {
            log.error("Server start error!!!", e);
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.startServer();
    }
}