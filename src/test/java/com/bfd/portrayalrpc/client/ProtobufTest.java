package com.bfd.portrayalrpc.client;
import com.bfd.portrayalrpc.protobuf.ItemInfo.ItemBase;
import com.bfd.portrayalrpc.protobuf.ItemInfo.ItemProfile;
import com.bfd.portrayalrpc.util.ProtoBufUtil;
import com.bfd.portrayalrpc.util.SysConfig;
import com.google.protobuf.Message;
import com.googlecode.protobuf.format.JsonFormat;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bfd.portrayalrpc.dao.HBaseClientAPI;
import com.bfd.portrayalrpc.dao.RedisClientAPI;
import com.bfd.portrayalrpc.dao.impl.HBaseDirectClient;
import com.bfd.portrayalrpc.dao.impl.HBaseThriftPoolClient;
import com.bfd.portrayalrpc.dao.impl.RedisClientImpl;

public class ProtobufTest {
    private static final Logger log = LoggerFactory
            .getLogger(RedisClientImpl.class);

    public static void main(String[] args) {
        try {
        	// redis
            RedisClientAPI client = new RedisClientImpl();
            client.initial();
            byte[] base = client.getBytes("Czuanshixiaoniao>1719785>ItemBase".getBytes());
            byte[] profile = client.getBytes("Cjinshan>b1a4ee97f6b51c4f702948a9a1a303bd>ItemProfile".getBytes());
            ItemBase ib = ItemBase.parseFrom(base);
            ItemProfile ip = ItemProfile.parseFrom(profile);
            log.info(ib.getCid());
            log.info(ip.getCid());
            
            String jsonFormat = JsonFormat.printToString(ib);
            JSONObject ib_base = JSONObject.parseObject(jsonFormat);
            System.out.println(ib_base.get("cid"));
            System.out.println("11111111");
            // hbase
            HBaseClientAPI hbase_client = new HBaseThriftPoolClient();
            String rowKey = "Cjinshan>b1a4ee97f6b51c4f702948a9a1a303bd";
            hbase_client.init();
            //hbase_client.writeItemBytes(SysConfig.HBASE_ITEM_TABLENAME, 
            //		rowKey, SysConfig.HBASE_ITEM_COLFAMILY, SysConfig.HBASE_ITEM_BASECOL, base);

            //hbase_client.writeItemBytes(SysConfig.HBASE_ITEM_TABLENAME, 
            //		rowKey, SysConfig.HBASE_ITEM_COLFAMILY, SysConfig.HBASE_ITEM_PROFILECOL, profile);
            
            rowKey = "Cjinshan>b1a4ee97f6b51c4f702948a9a1a303bd";
            byte[] h_base = hbase_client.queryByRowKey(SysConfig.HBASE_ITEM_TABLENAME, rowKey,
            		SysConfig.HBASE_ITEM_COLFAMILY, SysConfig.HBASE_ITEM_BASECOL);
            if(h_base != null) {
                ItemBase h_ib = ItemBase.parseFrom(h_base);
                log.info(h_ib.getCid());
            }
            byte[] h_profile = hbase_client.queryByRowKey(SysConfig.HBASE_ITEM_TABLENAME, rowKey,
            		SysConfig.HBASE_ITEM_COLFAMILY, SysConfig.HBASE_ITEM_PROFILECOL);
            if (h_profile != null) {
            	ItemProfile h_ip = ItemProfile.parseFrom(h_profile);
            	log.info(h_ip.getCid());
            }
            
            String[] colums = {SysConfig.HBASE_ITEM_PROFILECOL, SysConfig.HBASE_ITEM_BASECOL};
            Map<String, byte[]> map_value = hbase_client.queryByRowKey(SysConfig.HBASE_ITEM_TABLENAME, rowKey,
            		SysConfig.HBASE_ITEM_COLFAMILY, colums);
            if (map_value != null){
            	System.out.println("map_value");
            	ItemBase hh_ib = ItemBase.parseFrom(map_value.get(SysConfig.HBASE_ITEM_BASECOL));
            	log.info(hh_ib.getCid());
            }
            //log.info("result:" + r);
        } catch (Exception e) {
            log.error("Error: ", e);
        }
    }


}
