package com.bfd.portrayalrpc.dao.impl;

import com.bfd.portrayalrpc.Exception.RpcException;
import com.bfd.portrayalrpc.dao.RedisClientAPI;
import com.bfd.portrayalrpc.util.ECode;
import com.bfd.portrayalrpc.util.SysConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.redis.sentinel.client.RedisClient;


public class RedisSentinelClientImpl implements RedisClientAPI {
    private static final Logger log = LoggerFactory
            .getLogger(RedisSentinelClientImpl.class);

    private RedisClient redisClient = null;
	String sentinelAddr = "192.168.40.37:26379,192.168.40.38:26379,192.168.40.39:26379,192.168.40.40:26379,192.168.40.41:26379,192.168.40.42:26379";//Default
	String businessId = "Item";//Default
	public RedisSentinelClientImpl(String sentinel_addr, String business_id){
		sentinelAddr = sentinel_addr;
		businessId = business_id;
	}
    public void initial() throws Exception {
        log.info("RedisSentinelClientImpl init start");
        try {
            redisClient = new RedisClient(sentinelAddr, businessId);
        } catch (Exception e) {
            log.error("RedisSentinelClientImpl init Error!", e);
            throw new RpcException(ECode.REDIS_CONNECT_ERROR.getDesc(), ECode.REDIS_CONNECT_ERROR.getErrorCode());
        }
        log.info("RedisSentinelClientImpl init finish");
    }


	public boolean set(String key, String value) {
		return redisClient.set(key, value);
	}

	public String get(String key) {
		return redisClient.get(key);
	}

	public boolean lpush(String key, String... values) {
		return redisClient.lpush(key, values);
	}

	public boolean rpush(String key, String... values) {
		return redisClient.rpush(key, values);
	}

	public String lpop(String key) {
		return redisClient.lpop(key);
	}

	public String rpop(String key) {
		return redisClient.rpop(key);
	}

	public boolean del(String key) {
		return redisClient.del(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bfd.bip.db.redis.RedisClientAPI#setBytes(byte[], byte[])
	 */
	public boolean setBytes(byte[] key, byte[] value) {
		// TODO Auto-generated method stub
		return redisClient.setBytes(key, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bfd.bip.db.redis.RedisClientAPI#getBytes(byte[])
	 */
	public byte[] getBytes(byte[] key) {
		// TODO Auto-generated method stub
		return redisClient.getBytes(key);
	}
	
    public static void main(String[] args) {
        try {
        	String sentinelAddr = "192.168.40.37:26379,192.168.40.38:26379,192.168.40.39:26379,192.168.40.40:26379,192.168.40.41:26379,192.168.40.42:26379";//Default
        	String businessId = "Item";//Default
            RedisClientAPI client = new RedisSentinelClientImpl(sentinelAddr, businessId);
            client.initial();
            String r = client.get("Cyouban>31017>NewsBase");
            log.info("result:" + r);
        } catch (Exception e) {
            log.error("Error: ", e);
        }
    }
}
