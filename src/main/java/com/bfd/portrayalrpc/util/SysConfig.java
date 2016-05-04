package com.bfd.portrayalrpc.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by ronghua on 2015/7/14.
 */
public class SysConfig {
    private static final Logger log = LoggerFactory.getLogger(System.class);

    public static Integer THRIFT_SERVICE_PORT = 10090;
    
    public static String HBASE_CONNECTOR_TYPE = "THRIFT";
    public static String REDIS_CONNECTOR_TYPE = "DIRECT";
    
    public static String HBASE_ROOT_DIR = "hdfs://172.18.1.22:8020/hbase";
    public static String HBASE_CLUSTER_DISTRIBUTED = "true";
    public static String HBASE_ZOOKEEPER_CLIENTPORT = "2181";
    public static String HBASE_ZOOKEEPER_QUORUM = "172.18.1.22:2181,172.18.1.23:2181,172.18.1.24:2181";
    
    public static String HBASE_ITEM_TABLENAME = "Item";
    public static String HBASE_ITEM_COLFAMILY = "i";
    public static String HBASE_ITEM_BASECOL = "itemBaseSer";
    public static String HBASE_ITEM_PROFILECOL = "itemProfileSer";
    
    public static String HBASE_NEWS_TABLENAME = "Item";
    public static String HBASE_NEWS_COLFAMILY = "i";
    public static String HBASE_NEWS_BASECOL = "newsBaseSer";
    public static String HBASE_NEWS_PROFILECOL = "newsProfileSer";
    
    public static String HBASE_THRIFT_HOST = "localhost";
    public static Integer HBASE_THRIFT_PORT = 9090;
    public static Boolean HBASE_THRIFT_ISSECURE = false;
    public static Integer HBASE_THRIFT_TIMEOUT = 10000;

    public static String REDIS_HOST = "localhost";
    public static Integer REDIS_PORT = 6379;
    public static Integer REDIS_TIMEOUT = 1000;
    public static Integer REDIS_MAX_ACTIVE = 20;
    public static Integer REDIS_MAX_IDLE = 5;
    public static Integer REDIS_MAX_WAIT = 10000;
    public static Boolean REDIS_TEST_ON_BORROW = true;

    public static String REDIS_SENTINEL_ITEM_ADDR = "192.168.40.37:26379,192.168.40.38:26379,192.168.40.39:26379,192.168.40.40:26379,192.168.40.41:26379,192.168.40.42:26379";
    public static String REDIS_SENTINEL_ITEM_BUSINESS_ID = "item";
    
    public static String REDIS_SENTINEL_NEWS_ADDR = "192.168.40.37:26379,192.168.40.38:26379,192.168.40.39:26379,192.168.40.40:26379,192.168.40.41:26379,192.168.40.42:26379";
    public static String REDIS_SENTINEL_NEWS_BUSINESS_ID = "WpWcate";

    static Properties pp = null;

    static {
        load();
    }

    public static void load() {
        log.info("Start Loading System Config File.");

        pp = new Properties();
        try {
            InputStream is = SysConfig.class.getClassLoader()
                    .getResourceAsStream("portrayal_rpc.properties");
            pp.load(is);
            is.close();
        } catch (IOException e) {
            log.error(e.getMessage());
        } catch (Exception e) {
            log.error("Load SysConfig Error: ", e);
        }

        THRIFT_SERVICE_PORT = getIntegerProperty("thrift.service.port", THRIFT_SERVICE_PORT);
        
        HBASE_CONNECTOR_TYPE = getStringProperty("hbase.connector.type", HBASE_CONNECTOR_TYPE);
        REDIS_CONNECTOR_TYPE = getStringProperty("redis.connector.type", REDIS_CONNECTOR_TYPE);
        
        HBASE_ROOT_DIR = getStringProperty("hbase.rootdir", HBASE_ROOT_DIR);
        HBASE_CLUSTER_DISTRIBUTED = getStringProperty("hbase.cluster.distributed", HBASE_CLUSTER_DISTRIBUTED);
        HBASE_ZOOKEEPER_CLIENTPORT = getStringProperty("hbase.zookeeper.property.clientPort", HBASE_ZOOKEEPER_CLIENTPORT);
        HBASE_ZOOKEEPER_QUORUM = getStringProperty("hbase.zookeeper.quorum", HBASE_ZOOKEEPER_QUORUM);
     
        HBASE_THRIFT_HOST = getStringProperty("hbase.thrift.host", HBASE_THRIFT_HOST);
        HBASE_THRIFT_PORT = getIntegerProperty("hbase.thrift.port", HBASE_THRIFT_PORT);
        HBASE_THRIFT_ISSECURE = getBooleanProperty("hbase.thrift.isSecure", HBASE_THRIFT_ISSECURE);
        HBASE_THRIFT_TIMEOUT = getIntegerProperty("hbase.thrift.timeout", HBASE_THRIFT_TIMEOUT);
        
        HBASE_ITEM_TABLENAME = getStringProperty("hbase.item.tableName", HBASE_ITEM_TABLENAME);
        HBASE_ITEM_COLFAMILY = getStringProperty("hbase.item.colFamily", HBASE_ITEM_COLFAMILY);
        HBASE_ITEM_BASECOL = getStringProperty("hbase.item.baseCol", HBASE_ITEM_BASECOL);
        HBASE_ITEM_PROFILECOL = getStringProperty("hbase.item.profileCol", HBASE_ITEM_PROFILECOL);
        
        HBASE_NEWS_TABLENAME = getStringProperty("hbase.news.tableName", HBASE_NEWS_TABLENAME);
        HBASE_NEWS_COLFAMILY = getStringProperty("hbase.news.colFamily", HBASE_NEWS_COLFAMILY);
        HBASE_NEWS_BASECOL = getStringProperty("hbase.news.baseCol", HBASE_NEWS_BASECOL);
        HBASE_NEWS_PROFILECOL = getStringProperty("hbase.news.profileCol", HBASE_NEWS_PROFILECOL);
        
        REDIS_HOST = getStringProperty("redis.host", REDIS_HOST);
        REDIS_PORT = getIntegerProperty("redis.port", REDIS_PORT);
        REDIS_TIMEOUT = getIntegerProperty("redis.timeout", REDIS_TIMEOUT);
        REDIS_MAX_ACTIVE = getIntegerProperty("redis.max.active", REDIS_MAX_ACTIVE);
        REDIS_MAX_IDLE = getIntegerProperty("redis.max.idle", REDIS_MAX_IDLE);
        REDIS_MAX_WAIT = getIntegerProperty("redis.max.wait", REDIS_MAX_WAIT);
        REDIS_TEST_ON_BORROW = getBooleanProperty("redis.test.on.borrow", REDIS_TEST_ON_BORROW);

        REDIS_SENTINEL_ITEM_ADDR = getStringProperty("redis.sentinel.item.addr", REDIS_SENTINEL_ITEM_ADDR);
        REDIS_SENTINEL_ITEM_BUSINESS_ID = getStringProperty("redis.sentinel.item.businessId", REDIS_SENTINEL_ITEM_BUSINESS_ID);

        REDIS_SENTINEL_NEWS_ADDR = getStringProperty("redis.sentinel.news.addr", REDIS_SENTINEL_NEWS_ADDR);
        REDIS_SENTINEL_NEWS_BUSINESS_ID = getStringProperty("redis.sentinel.news.businessId", REDIS_SENTINEL_NEWS_BUSINESS_ID);

        log.info("System Config File Loaded.");
    }

    private static boolean getBooleanProperty(String key,
                                              boolean defaultValue) {
        if (pp == null || pp.getProperty(key) == null) {
            return defaultValue;
        }
        try {
            return "true".equals(pp.getProperty(key));
        } catch (Exception e) {
            log.error(e.toString(), e);
        }

        return defaultValue;
    }

    private static int getIntegerProperty(String key, int defaultValue) {
        if (pp == null || pp.getProperty(key) == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(pp.getProperty(key));
        } catch (Exception e) {
            log.error(e.toString(), e);
        }
        return defaultValue;
    }

    @SuppressWarnings("unused")
	private static long getLongProperty(String key, long defaultValue) {
        if (pp == null || pp.getProperty(key) == null) {
            return defaultValue;
        }
        try {
            return Long.parseLong(pp.getProperty(key));
        } catch (Exception e) {
            log.error(e.toString(), e);
        }
        return defaultValue;
    }

    private static String getStringProperty(String key, String defaultValue) {
        if (pp == null || pp.getProperty(key) == null) {
            return defaultValue;
        }
        return pp.getProperty(key).trim();
    }

    public static void main(String args[]) {
        load();
    }
}
