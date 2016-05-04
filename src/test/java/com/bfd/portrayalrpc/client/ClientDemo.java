package com.bfd.portrayalrpc.client;

import com.bfd.portrayalrpc.protobuf.ItemInfo.ItemBase;
import com.bfd.portrayalrpc.protobuf.ItemInfo.ItemProfile;
import com.bfd.portrayalrpc.thrift.PortrayalService;
import com.bfd.portrayalrpc.thrift.ReqType;
import com.bfd.portrayalrpc.thrift.Result;
import com.bfd.portrayalrpc.util.SysConfig;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.alibaba.fastjson.JSONObject;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ClientDemo {
    private static final Logger log = LoggerFactory
            .getLogger(ClientDemo.class);
  
    public static PortrayalService.Client client;  
    public static TTransport transport;
    public static TProtocol protocol;
    public ClientDemo(String ip, int port){
    		transport = new TSocket(ip, port);
            try {
                transport.open();
            } catch (TTransportException e) {
                log.error("transport open error!", e);
            }
            //使用高密度二进制协议
            protocol = new TBinaryProtocol(transport);
            client = new PortrayalService.Client(protocol);
            }
    	
    public Result get_data(String request) throws TException {
    	return client.get_data(request);
    	}
    	
    public Result get_info_data(String cid, String iid, ReqType req_type) throws TException {
    	return client.get_info_data(cid, iid, req_type);
    	}
    	
    public String get_json_data(String request) throws TException {
    	return client.get_json_data(request);
    }
    	
    public void close(){
    	if (transport.isOpen()){
    		transport.close();
    		}
    	}

    public static void main(String[] args) {
    	
        ClientDemo demo = new ClientDemo("localhost", SysConfig.THRIFT_SERVICE_PORT);
        String cid = "Cjinshan";
        String iid = "b1a4ee97f6b51c4f702948a9a1a303bd";
        ReqType req_type = ReqType.ITEMBASE;
        JSONObject obj_request = new JSONObject();
        obj_request.put("cid", cid);
        obj_request.put("iid", iid);
        obj_request.put("req_type", req_type);
        String request = obj_request.toJSONString();
		try {
			Result ret = demo.get_info_data(cid, iid, req_type);
			ItemBase ib = ItemBase.parseFrom(ByteString.copyFrom(ret.base));
			log.info(ib.getCid());
			ret = demo.get_data(request);
			ib = ItemBase.parseFrom(ByteString.copyFrom(ret.base));
			log.info(ib.getCid());
			
			} catch (InvalidProtocolBufferException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        demo.close();
    }

}
