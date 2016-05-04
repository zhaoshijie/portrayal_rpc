package com.bfd.portrayalrpc.client;

import com.alibaba.fastjson.JSONObject;
import com.bfd.portrayalrpc.Exception.RpcException;
import com.bfd.portrayalrpc.service.PortrayalServiceImpl;
import com.bfd.portrayalrpc.thrift.ReqType;

public class PortrayalServiceImplTest {
	public static void main(String[] args) {
		try {
			PortrayalServiceImpl impl = new PortrayalServiceImpl();
	        String cid = "Cjinshan";
	        String iid = "b1a4ee97f6b51c4f702948a9a1a303bd";
	        ReqType req_type = ReqType.ITEMBASE;
	        JSONObject obj_request = new JSONObject();
	        obj_request.put("cid", cid);
	        obj_request.put("iid", iid);
	        obj_request.put("req_type", req_type);

	        String request = obj_request.toJSONString();
			//Result ret = impl.get_info_data(cid, iid, req_type);
			//System.out.println(ret.status);
			//System.out.println(ret);
			
			String ret_str = impl.get_json_data(request);
			JSONObject ret = JSONObject.parseObject(ret_str);
			System.out.println(ret.getJSONObject("base"));
		} catch (RpcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
