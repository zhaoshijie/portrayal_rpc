package com.bfd.portrayalrpc.service;

import com.bfd.portrayalrpc.Exception.RpcException;
import com.bfd.portrayalrpc.dao.HBaseClientAPI;
import com.bfd.portrayalrpc.dao.RedisClientAPI;
import com.bfd.portrayalrpc.dao.impl.HBaseDirectClient;
import com.bfd.portrayalrpc.dao.impl.HBaseThriftPoolClient;
import com.bfd.portrayalrpc.dao.impl.RedisClientImpl;
import com.bfd.portrayalrpc.dao.impl.RedisSentinelClientImpl;
import com.bfd.portrayalrpc.thrift.ErrMsg;
import com.bfd.portrayalrpc.thrift.PortrayalService;
import com.bfd.portrayalrpc.thrift.ReqType;
import com.bfd.portrayalrpc.thrift.Result;
import com.bfd.portrayalrpc.util.ECode;
import com.bfd.portrayalrpc.util.FormatKey;
import com.bfd.portrayalrpc.util.ProtoBufUtil;
import com.bfd.portrayalrpc.util.SysConfig;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PortrayalServiceImpl implements PortrayalService.Iface {
    private static final Logger log = LoggerFactory.getLogger(PortrayalServiceImpl.class);
    private final RedisClientAPI redis_item_client;
    private final RedisClientAPI redis_news_client;
    private final HBaseClientAPI hbase_client;
    

    public PortrayalServiceImpl() throws RpcException,Exception {
    	if(SysConfig.REDIS_CONNECTOR_TYPE.equals("DIRECT")){
    		log.info("REDIS_CONNECTOR_TYPE DIRECT");
    		redis_item_client = new RedisClientImpl();
    		redis_news_client = new RedisClientImpl();
    	}else if (SysConfig.REDIS_CONNECTOR_TYPE.equals("SENTINEL")){
    		log.info("REDIS_CONNECTOR_TYPE SENTINEL");
    		redis_item_client = new RedisSentinelClientImpl(SysConfig.REDIS_SENTINEL_ITEM_ADDR,
    				SysConfig.REDIS_SENTINEL_ITEM_BUSINESS_ID);
    		redis_news_client = new RedisSentinelClientImpl(SysConfig.REDIS_SENTINEL_NEWS_ADDR,
    				SysConfig.REDIS_SENTINEL_ITEM_BUSINESS_ID);
    	}else {
    		throw new Exception("Redis connector type error not DIRECT or SENTINEL");
    	}
    	redis_item_client.initial();
    	redis_news_client.initial();
    	
    	if(SysConfig.HBASE_CONNECTOR_TYPE.equals("DIRECT")){
    		log.info("HBASE_CONNECTOR_TYPE DIRECT");
    		hbase_client = new HBaseDirectClient();
    	}else if (SysConfig.HBASE_CONNECTOR_TYPE.equals("THRIFT")){
    		log.info("HBASE_CONNECTOR_TYPE THRIFT");
    		hbase_client = new HBaseThriftPoolClient();
    	}else {
    		throw new Exception("Hbase connector type error not DIRECT or THRIFT");
    	}
    	hbase_client.init();
    	
    }
    
    /**
     * @param request 
     * @throws RpcException
     */
    public JSONObject parse_request(String request) throws RpcException{
    	JSONObject json_request = null;
    	try{
    		json_request = JSON.parseObject(request);
    		if(!json_request.containsKey("cid") || !json_request.containsKey("iid")
    			|| !json_request.containsKey("req_type")){
    			log.error("Parmeter Error, cid or iid or req_type is none");
    			throw new RpcException(ECode.PARAM_ERROR.getDesc(), ECode.PARAM_ERROR.getErrorCode());
    			}
    	}
    	catch(Exception e){
    		log.error("prarse_request error:", e);
			throw new RpcException(ECode.PARAM_PARSE_ERROR.getDesc(), ECode.PARAM_PARSE_ERROR.getErrorCode());
			}
    	return json_request;
    }
    
    public ReqType get_req_type(String request) throws RpcException{
    	JSONObject json_request = parse_request(request);
    	return ReqType.findByValue(json_request.getIntValue("req_type"));
    }
    
    public byte[] get_value(String cid, String iid, ReqType req_type) throws RpcException, Exception {
    	//log.debug("start get value");
    	byte[] redis_key = null;
    	String hbase_table = null;
    	String hbase_colfamily = null;
    	String hbase_col = null;
    	RedisClientAPI redis_client = null;
    	String hbase_key = FormatKey.hbase_format(cid, iid);
    	switch(req_type){
    	case ITEMBASE:
    		redis_key = FormatKey.redis_item_base(cid, iid).getBytes();
    		redis_client = redis_item_client;
    		hbase_table = SysConfig.HBASE_ITEM_TABLENAME;
    		hbase_colfamily = SysConfig.HBASE_ITEM_COLFAMILY;
    		hbase_col = SysConfig.HBASE_ITEM_BASECOL;
    		break;
    		
    	case ITEMPROFILE:
    		redis_key = FormatKey.redis_item_profile(cid, iid).getBytes();
    		redis_client = redis_item_client;
    		hbase_table = SysConfig.HBASE_ITEM_TABLENAME;
    		hbase_colfamily = SysConfig.HBASE_ITEM_COLFAMILY;
    		hbase_col = SysConfig.HBASE_ITEM_PROFILECOL;
    		break;
    		
    	case NEWSBASE:
    		redis_key = FormatKey.redis_news_base(cid, iid).getBytes();
    		redis_client = redis_news_client;
    		hbase_table = SysConfig.HBASE_NEWS_TABLENAME;
    		hbase_colfamily = SysConfig.HBASE_NEWS_COLFAMILY;
    		hbase_col = SysConfig.HBASE_NEWS_BASECOL;
    		break;
    		
    	case NEWSPROFILE:
    		redis_key = FormatKey.redis_news_profile(cid, iid).getBytes();
    		redis_client = redis_news_client;
    		hbase_table = SysConfig.HBASE_NEWS_TABLENAME;
    		hbase_colfamily = SysConfig.HBASE_NEWS_COLFAMILY;
    		hbase_col = SysConfig.HBASE_NEWS_PROFILECOL;
    		break;
		default:
			throw new RpcException(ECode.REQ_TYPE_ERROR.getDesc(), ECode.REQ_TYPE_ERROR.getErrorCode());
    	
    	}
    	log.debug("start get redis data key is:" + new String(redis_key));
    	byte[] ret = redis_client.getBytes(redis_key);
    	if(is_null_or_empty(ret)) {
    		log.debug("redis data is null, get hbase data key is:" + hbase_key);
    		ret = hbase_client.queryByRowKey(hbase_table, hbase_key,
    				hbase_colfamily, hbase_col);
    		if(!is_null_or_empty(ret)){
    			log.debug("update redis data key is:" + new String(redis_key));
    			redis_client.setBytes(redis_key, ret);
    		}
    	}
    	return ret;
    }
    boolean is_null_or_empty(byte[] data) {
    	return (data == null|| data.length == 0);
    }
    
    public Map<String, byte[]> get_item_value(String cid, String iid) throws RpcException, Exception{
    	Map<String, byte[]> value = new HashMap<String, byte[]>();
    	byte[] redis_base_key = FormatKey.redis_item_base(cid, iid).getBytes();
    	byte[] redis_profile_key = FormatKey.redis_item_profile(cid, iid).getBytes();
    	String hbase_key = FormatKey.hbase_format(cid, iid);
    	
    	byte [] ret_base = redis_item_client.getBytes(redis_base_key);
    	byte [] ret_profile = redis_item_client.getBytes(redis_profile_key);
    	
    	if(is_null_or_empty(ret_base) && is_null_or_empty(ret_profile)) {
    		log.debug("cid, iid get base and profile data from hbase");
    		String [] qulifiers = {SysConfig.HBASE_ITEM_BASECOL, SysConfig.HBASE_ITEM_PROFILECOL};
    		value = hbase_client.queryByRowKey(SysConfig.HBASE_ITEM_TABLENAME,
    				hbase_key, SysConfig.HBASE_ITEM_COLFAMILY, qulifiers);
    		if (value.get(SysConfig.HBASE_ITEM_BASECOL) != null){
    			log.debug("update base redis data key is:" + "cid" + ">iid");
    			redis_item_client.setBytes(redis_base_key, value.get(SysConfig.HBASE_ITEM_BASECOL));
    		}
    		if (value.get(SysConfig.HBASE_ITEM_PROFILECOL) != null){
    			log.debug("update profile redis data key is:" + "cid" + ">iid");
    			redis_item_client.setBytes(redis_profile_key, value.get(SysConfig.HBASE_ITEM_PROFILECOL));
    		}
    		return value;
    	}else if (is_null_or_empty(ret_base)) {
    		log.debug("cid, iid get base data from hbase");
    		ret_base = hbase_client.queryByRowKey(SysConfig.HBASE_ITEM_TABLENAME,
    				hbase_key, SysConfig.HBASE_ITEM_COLFAMILY,
    				SysConfig.HBASE_ITEM_BASECOL);
    		if (!is_null_or_empty(ret_base)) {
    			log.debug("update base redis data key is:" + "cid" + ">iid");
    			redis_item_client.setBytes(redis_base_key, ret_base);
    		}
    	}else if (is_null_or_empty(ret_profile)) {
    		log.debug("cid, iid get profile data from hbase");
    		ret_profile = hbase_client.queryByRowKey(SysConfig.HBASE_ITEM_TABLENAME,
    				hbase_key, SysConfig.HBASE_ITEM_COLFAMILY,
    				SysConfig.HBASE_ITEM_PROFILECOL);
    		if (!is_null_or_empty(ret_profile)) {
    			log.debug("update profile redis data key is:" + "cid" + ">iid");
    			redis_item_client.setBytes(redis_profile_key, ret_profile);
    		}
    	}
    	value.put(SysConfig.HBASE_ITEM_BASECOL, ret_base);
    	value.put(SysConfig.HBASE_ITEM_PROFILECOL, ret_profile);
    	return value;
    }
    
    public Map<String, byte[]> get_news_value(String cid, String iid) throws RpcException, Exception{
    	Map<String, byte[]> value = new HashMap<String, byte[]>();
    	byte[] redis_base_key = FormatKey.redis_news_base(cid, iid).getBytes();
    	byte[] redis_profile_key = FormatKey.redis_news_profile(cid, iid).getBytes();
    	String hbase_key = FormatKey.hbase_format(cid, iid);
    	
    	byte [] ret_base = redis_news_client.getBytes(redis_base_key);
    	byte [] ret_profile = redis_news_client.getBytes(redis_profile_key);
    	
    	if(is_null_or_empty(ret_base) && is_null_or_empty(ret_profile)) {
    		log.debug("cid, iid get base and profile data from hbase");
    		String [] qulifiers = {SysConfig.HBASE_NEWS_BASECOL, SysConfig.HBASE_NEWS_PROFILECOL};
    		value = hbase_client.queryByRowKey(SysConfig.HBASE_NEWS_TABLENAME,
    				hbase_key, SysConfig.HBASE_NEWS_COLFAMILY, qulifiers);
    		if (value.get(SysConfig.HBASE_NEWS_BASECOL) != null) {
    			log.debug("update base redis data key is:" + "cid" + ">iid");
    			redis_news_client.setBytes(redis_base_key, value.get(SysConfig.HBASE_NEWS_BASECOL));
    		}
    		if (value.get(SysConfig.HBASE_NEWS_PROFILECOL) != null){
    			log.debug("update profile redis data key is:" + "cid" + ">iid");
    			redis_news_client.setBytes(redis_profile_key, ret_profile);
    		}
    		return value;
    	}else if (is_null_or_empty(ret_base)) {
    		log.debug("cid, iid get base data from hbase");
    		ret_base = hbase_client.queryByRowKey(SysConfig.HBASE_NEWS_TABLENAME,
    				hbase_key, SysConfig.HBASE_NEWS_COLFAMILY,
    				SysConfig.HBASE_NEWS_BASECOL);
    		if (!is_null_or_empty(ret_base)) {
    			log.debug("update base redis data key is:" + "cid" + ">iid");
    			redis_news_client.setBytes(redis_base_key, ret_base);
    			}
    	}else if (is_null_or_empty(ret_profile)) {
    		log.debug("cid, iid get profile and profile data from hbase");
    		ret_profile = hbase_client.queryByRowKey(SysConfig.HBASE_NEWS_TABLENAME,
    				hbase_key, SysConfig.HBASE_NEWS_COLFAMILY,
    				SysConfig.HBASE_NEWS_PROFILECOL);
    		if (!is_null_or_empty(ret_profile)){
    			log.debug("update profile redis data key is:" + "cid" + ">iid");
    			redis_news_client.setBytes(redis_profile_key, ret_profile);}
    	}
    	value.put(SysConfig.HBASE_NEWS_BASECOL, ret_base);
    	value.put(SysConfig.HBASE_NEWS_PROFILECOL, ret_profile);
    	return value;
    }

    public void iner_get(String cid, String iid, ReqType req_type, Result result) throws RpcException, Exception {
    	log.debug("Start iner get" );
    	if (req_type == null) {
    		log.error("req_type is null cid:" + cid + " iid:" + iid);
    		throw new RpcException(ECode.REQ_TYPE_ERROR.getDesc(), ECode.REQ_TYPE_ERROR.getErrorCode());
    	}
    	switch(req_type) {
		case ITEMBASE:
		case NEWSBASE:
			byte[] data = get_value(cid, iid, req_type);
			if (data != null) {
				result.base = ByteBuffer.wrap(data);
			}
			break;
		case ITEMPROFILE:
		case NEWSPROFILE:
			byte[] data_profile = get_value(cid, iid, req_type);
			if (data_profile != null) {
				result.profile = ByteBuffer.wrap(data_profile);
			}
			break;
		case ITEM:
			Map<String, byte[]> value = get_item_value(cid, iid);
			byte[] base = value.get(SysConfig.HBASE_ITEM_BASECOL);
			
			if (!is_null_or_empty(base))
				result.base =ByteBuffer.wrap(base);
			byte[] profile = value.get(SysConfig.HBASE_ITEM_PROFILECOL);
			if (!is_null_or_empty(profile))
				result.profile = ByteBuffer.wrap(profile);
			break;
		case NEWS:
			Map<String, byte[]> news_value = get_news_value(cid, iid);
			byte[] news_base = news_value.get(SysConfig.HBASE_NEWS_BASECOL);
			if (!is_null_or_empty(news_base))
				result.base =ByteBuffer.wrap(news_base);
			byte[] news_profile = news_value.get(SysConfig.HBASE_NEWS_PROFILECOL);
			if (!is_null_or_empty(news_profile))
				result.profile = ByteBuffer.wrap(news_profile);
			break;
		default:
			throw new RpcException(ECode.REQ_TYPE_ERROR.getDesc(), ECode.REQ_TYPE_ERROR.getErrorCode());
		}
    }
    
    public Result get_data(String request) {
    	log.debug("start get_data");
    	Result result = new Result();
    	ErrMsg msg = new ErrMsg();
    	try{
    		JSONObject json_request = parse_request(request);
    		String cid = json_request.getString("cid");
    		String iid = json_request.getString("iid");
    		ReqType req_type = ReqType.findByValue(json_request.getIntValue("req_type"));
    		iner_get(cid, iid, req_type, result);
        	result.status = 0;
        	return result;
    	}
    	catch(RpcException e){
    		result.status = -1;
    		log.error("RpcException error:", e);
    		msg.setCode(e.getExceptionCode());
    		msg.setDescirbe(e.getMessage());
    	}catch(Exception e){
    		result.status = -1;
    		log.error("get data errer:", e);
            msg.setCode(ECode.INTERNAL_ERROR.getErrorCode());
            msg.setDescirbe(ECode.INTERNAL_ERROR.getDesc());
    	}
    	result.msg = msg;
    	return result;
    }
    
    public Result get_info_data(String cid, String iid, ReqType req_type) {
       	log.debug("start get_info_data");
    	Result result = new Result();
    	ErrMsg msg = new ErrMsg();
    	try{
    		iner_get(cid, iid, req_type, result);
        	result.status = 0;
        	return result;
    	}
    	catch(RpcException e){
    		result.status = -1;
    		msg.setCode(e.getExceptionCode());
    		msg.setDescirbe(e.getMessage());
    	}catch(Exception e){
    		result.status = -1;
    		log.error("get data errer:", e);
            msg.setCode(ECode.INTERNAL_ERROR.getErrorCode());
            msg.setDescirbe(ECode.INTERNAL_ERROR.getDesc());
    	}
    	result.msg = msg;
    	return result;
    }
    
    public String get_json_data(String request){
    	Result result = get_data(request);
    	JSONObject ret = new JSONObject();
    	ret.put("status", result.status);
    	if (result.status == 0){
    		try {
				ReqType req_type = get_req_type(request);
				switch(req_type) {
				case ITEMBASE:
				case ITEMPROFILE:
				case ITEM:
					if (result.base != null) {
						String message_base = ProtoBufUtil.itemBaseByteToJSONString(result.base.array());
						JSONObject it_base = JSONObject.parseObject(message_base);
						ret.put("base", it_base);
					}
					if (result.profile != null) {
						String message_profile = ProtoBufUtil.itemProfileByteToJSONString(result.profile.array());
						JSONObject it_profile = JSONObject.parseObject(message_profile);
						ret.put("profile", it_profile);
					}
					break;
				case NEWSBASE:
				case NEWSPROFILE:
				case NEWS:
					if (result.base != null) {
						String message_base = ProtoBufUtil.newsBaseByteToJSONString(result.base.array());
						JSONObject ne_base = JSONObject.parseObject(message_base);
						ret.put("base", ne_base);
					}
					if (result.profile != null) {
						String message_profile = ProtoBufUtil.newsProfileByteToJSONString(result.profile.array());
						JSONObject ne_profile = JSONObject.parseObject(message_profile);
						ret.put("profile", ne_profile);
					}
					break;
				}
			} catch (RpcException e) {
				ret.put("status", result.status);
				JSONObject msg = new JSONObject();
	    		msg.put("code", e.getExceptionCode());
	    		msg.put("describe", e.getMessage());
				e.printStackTrace();
				ret.put("msg", msg);
				log.error("request json data ", e);
			} catch (Exception e) {
				ret.put("status", result.status);
				JSONObject msg = new JSONObject();
	    		msg.put("code", ECode.INTERNAL_ERROR.getErrorCode());
	    		msg.put("describe", ECode.INTERNAL_ERROR.getDesc());
				e.printStackTrace();
				ret.put("msg", msg);
				log.error("request json data ", e);
			}
    	} else {
    		JSONObject msg = new JSONObject();
    		msg.put("code", result.msg.getCode());
    		msg.put("describe", result.msg.getDescirbe());
    		ret.put("msg", msg);
    	}
    	return ret.toJSONString();
    }
}
