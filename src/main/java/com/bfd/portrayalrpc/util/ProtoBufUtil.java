package com.bfd.portrayalrpc.util;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.protobuf.InvalidProtocolBufferException;
import com.googlecode.protobuf.format.JsonFormat;
import com.bfd.portrayalrpc.Exception.RpcException;
import com.bfd.portrayalrpc.protobuf.ItemInfo.ItemBase;
import com.bfd.portrayalrpc.protobuf.ItemInfo.ItemProfile;
import com.bfd.portrayalrpc.protobuf.NewsInfo.NewsBase;
import com.bfd.portrayalrpc.protobuf.NewsInfo.NewsProfile;
import com.bfd.portrayalrpc.service.PortrayalServiceImpl;

/**
 * Created by Zheng.Liu (Eric) on 2015/07/10.
 * 
 * ProtoBuf \ JSON converter util
 */
public class ProtoBufUtil {

	private static final Logger log = LoggerFactory.getLogger(ProtoBufUtil.class);
	/**
	 * Convert a JSON string to ItemBase ProtoBuf Object
	 * 
	 * @param jsonStr
	 * @return
	 * @throws JsonFormat.ParseException
	 */
	public static ItemBase parseItemBaseFromJSONStr(
			String jsonStr) throws JsonFormat.ParseException {
		ItemBase.Builder itemBaseBuilder = ItemBase
				.newBuilder();
		JsonFormat.merge(jsonStr, itemBaseBuilder);
		return itemBaseBuilder.build();
	}

	/**
	 * Convert a JSON string to ItemProfile ProtoBuf Object
	 * 
	 * @param jsonStr
	 * @return
	 * @throws JsonFormat.ParseException
	 */
	public static ItemProfile parseItemProfileFromJSONStr(
			String jsonStr) throws JsonFormat.ParseException {
		ItemProfile.Builder itemProfileBuilder = ItemProfile
				.newBuilder();
		JsonFormat.merge(jsonStr, itemProfileBuilder);
		return itemProfileBuilder.build();
	}

	/**
	 * Convert Item Base ProtoBuf Bytes to JSON String
	 * 
	 * @param protoBufArray
	 * @return
	 * @throws JsonFormat.ParseException
	 * @throws InvalidProtocolBufferException
	 */
	public static String itemBaseByteToJSONString(byte[] protoBufArray)
			throws RpcException {
		ItemBase ib = null;
		try {
			ib = ItemBase.parseFrom(protoBufArray);
		} catch (Exception e) {
			log.error("byte to base error:", e);
			throw new RpcException(ECode.PROTOBUF_PARSE_ERROR.getDesc(), ECode.PROTOBUF_PARSE_ERROR.getErrorCode());
		}
		try {
			return JsonFormat.printToString(ib);
		} catch (Exception e) {
			log.error("base  to json error:", e);
			throw new RpcException(ECode.PROTOBUF_TO_JSON_ERROR.getDesc(), ECode.PROTOBUF_TO_JSON_ERROR.getErrorCode());
		}
	}

	public static String newsBaseByteToJSONString(byte[] protoBufArray)
			throws RpcException {
		NewsBase ib = null;
		try {
			ib = NewsBase.parseFrom(protoBufArray);
		} catch (Exception e) {
			log.error("byte to base error:", e);
			throw new RpcException(ECode.PROTOBUF_PARSE_ERROR.getDesc(), ECode.PROTOBUF_PARSE_ERROR.getErrorCode());}
		try {
			return JsonFormat.printToString(ib);
		} catch (Exception e) {
			log.error("base  to json error:", e);
			throw new RpcException(ECode.PROTOBUF_TO_JSON_ERROR.getDesc(), ECode.PROTOBUF_TO_JSON_ERROR.getErrorCode());
		}
	}
	/**
	 * Convert Item Profile ProtoBuf Bytes to JSON String
	 * 
	 * @param protoBufArray
	 * @return
	 * @throws JsonFormat.ParseException
	 * @throws InvalidProtocolBufferException
	 */
	public static String itemProfileByteToJSONString(byte[] protoBufArray)
			throws Exception, JsonFormat.ParseException,
			InvalidProtocolBufferException {
		ItemProfile ip = null;
		try {
			ip = ItemProfile.parseFrom(protoBufArray);
		} catch (Exception e) {
			log.error("byte to profile error:", e);
			throw new RpcException(ECode.PROTOBUF_PARSE_ERROR.getDesc(), ECode.PROTOBUF_PARSE_ERROR.getErrorCode());
		}
		try {
			return JsonFormat.printToString(ip);
		} catch (Exception e) {
			log.error("profile  to json error:", e);
			throw new RpcException(ECode.PROTOBUF_TO_JSON_ERROR.getDesc(), ECode.PROTOBUF_TO_JSON_ERROR.getErrorCode());
		}

	}
	
	public static String newsProfileByteToJSONString(byte[] protoBufArray)
			throws Exception, JsonFormat.ParseException,
			InvalidProtocolBufferException {
		NewsProfile ip = null;
		try {
			ip = NewsProfile.parseFrom(protoBufArray);
		} catch (Exception e) {
			log.error("byte to base error:", e);
			throw new RpcException(ECode.PROTOBUF_PARSE_ERROR.getDesc(), ECode.PROTOBUF_PARSE_ERROR.getErrorCode());
		}
		try {
			return JsonFormat.printToString(ip);
		} catch (Exception e) {
			log.error("profile  to json error:", e);
			throw new RpcException(ECode.PROTOBUF_TO_JSON_ERROR.getDesc(), ECode.PROTOBUF_TO_JSON_ERROR.getErrorCode());
		}

	}

	public static void main(String[] args)
			throws InvalidProtocolBufferException, JsonFormat.ParseException,
			UnsupportedEncodingException {

		ItemProfile.Builder tpb = ItemProfile
				.newBuilder();
		tpb.setCid("test_cid");
		tpb.setIid("test_iid");
		tpb.setCmpCpu("中文");
		ItemProfile itemProfile = tpb.build();

		try {
			String s = itemProfileByteToJSONString(itemProfile
					.toByteArray());
			System.out.println(JSON.toJSONString("中文",
					SerializerFeature.BrowserCompatible));
			System.out.println(s);
			
			ItemProfile profile = parseItemProfileFromJSONStr(s);
			System.out.println(profile.getCid());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
