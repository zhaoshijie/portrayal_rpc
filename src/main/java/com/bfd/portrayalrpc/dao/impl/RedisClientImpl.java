package com.bfd.portrayalrpc.dao.impl;

import com.bfd.portrayalrpc.Exception.RpcException;
import com.bfd.portrayalrpc.dao.RedisClientAPI;
import com.bfd.portrayalrpc.util.ECode;
import com.bfd.portrayalrpc.util.SysConfig;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

/**
 * Created by ronghua on 2015/7/14.
 */
public class RedisClientImpl implements RedisClientAPI {
    private static final Logger log = LoggerFactory
            .getLogger(RedisClientImpl.class);
    
	private static ShardedJedisPool shardedJedisPool;
    //private Jedis shardedJedis;
    //private static JedisPool shardedJedisPool;

    public void initial() throws Exception {
        log.info("RedisClientImpl init");
        try {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(SysConfig.REDIS_MAX_ACTIVE);
            config.setMaxIdle(SysConfig.REDIS_MAX_IDLE);
            config.setMaxWaitMillis(SysConfig.REDIS_MAX_WAIT);
            config.setTestOnBorrow(SysConfig.REDIS_TEST_ON_BORROW);
            List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
            shards.add(new JedisShardInfo(SysConfig.REDIS_HOST, SysConfig.REDIS_PORT));
            shardedJedisPool = new ShardedJedisPool(config, shards);
            //shardedJedisPool = new JedisPool(config, SysConfig.REDIS_HOST, SysConfig.REDIS_PORT);
        } catch (Exception e) {
            log.error("RedisClientImpl Init Error!", e);
            throw new RpcException(ECode.REDIS_CONNECT_ERROR.getDesc(), ECode.REDIS_CONNECT_ERROR.getErrorCode());
        }
    }

	public void returnResource(ShardedJedis jedis) {
        if (jedis != null && shardedJedisPool != null) {
            try {
            	shardedJedisPool.returnResourceObject(jedis);
            } catch (Exception e) {
                log.error("redis returnResource error!", e);
            }
        }
    }
	//Interface	
	public boolean set(final String key, String value) throws RpcException {
		ShardedJedis shardedJedis = shardedJedisPool.getResource();
		try {
			shardedJedis.set(key, value);
			return true;
		} catch (Exception e) {
			shardedJedis = null;
			throw new RpcException(ECode.REDIS_OPRR_ERROR.getDesc(), ECode.REDIS_OPRR_ERROR.getErrorCode());
		}
		finally {
			returnResource(shardedJedis);
		}
	}

	public String get(final String key) throws RpcException {
		ShardedJedis shardedJedis = shardedJedisPool.getResource();
		try {
			return shardedJedis.get(key);
		} catch (Exception e) {
			shardedJedis = null;
			throw new RpcException(ECode.REDIS_OPRR_ERROR.getDesc(), ECode.REDIS_OPRR_ERROR.getErrorCode());
		}
		finally {
			returnResource(shardedJedis);
		}
	}

	public boolean lpush(String key, String... values) throws RpcException {
		return lpushJedis(key, values) > 0;
	}

	public boolean rpush(String key, String... values) throws RpcException {
		return rpushJedis(key, values) > 0;
	}

	public String lpop(final String key) throws RpcException{
		ShardedJedis shardedJedis = shardedJedisPool.getResource();	
		try {
			return shardedJedis.lpop(key);
		} catch (Exception e) {
			shardedJedis = null;
			throw new RpcException(ECode.REDIS_OPRR_ERROR.getDesc(), ECode.REDIS_OPRR_ERROR.getErrorCode());
		}
		finally {
			returnResource(shardedJedis);
		}
	}

	public String rpop(final String key) throws RpcException{
		ShardedJedis shardedJedis = shardedJedisPool.getResource();
		try {
			return shardedJedis.rpop(key);
		} catch (Exception e) {
			shardedJedis = null;
			throw new RpcException(ECode.REDIS_OPRR_ERROR.getDesc(), ECode.REDIS_OPRR_ERROR.getErrorCode());
		}
		finally {
			returnResource(shardedJedis);
		}
	}

	//Internal
	public Long rpushJedis(final String key, String... values) throws RpcException{
		ShardedJedis shardedJedis = shardedJedisPool.getResource();
		try {
			return shardedJedis.rpush(key, values);
		} catch (Exception e) {
			shardedJedis = null;
			throw new RpcException(ECode.REDIS_OPRR_ERROR.getDesc(), ECode.REDIS_OPRR_ERROR.getErrorCode());
		}
		finally {
			returnResource(shardedJedis);
		}
	}

	public Long lpushJedis(final String key, String... values) throws RpcException{
		ShardedJedis shardedJedis = shardedJedisPool.getResource();
	
		try {
			return shardedJedis.lpush(key, values);
		} catch (Exception e) {
			shardedJedis = null;
			throw new RpcException(ECode.REDIS_OPRR_ERROR.getDesc(), ECode.REDIS_OPRR_ERROR.getErrorCode());
		}
		finally {
			returnResource(shardedJedis);
		}
	}

	public boolean del(String key) throws RpcException{
		ShardedJedis shardedJedis = shardedJedisPool.getResource();
		try {
			return shardedJedis.del(key) > 0;
		} catch (Exception e) {
			shardedJedis = null;
			throw new RpcException(ECode.REDIS_OPRR_ERROR.getDesc(), ECode.REDIS_OPRR_ERROR.getErrorCode());
		}
		finally {
			returnResource(shardedJedis);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bfd.bip.db.redis.RedisClientAPI#setBytes(byte[], byte[])
	 */
	public boolean setBytes(byte[] key, byte[] value) throws RpcException{
		// TODO Auto-generated method stub
		ShardedJedis shardedJedis = shardedJedisPool.getResource();
		try {
			shardedJedis.set(key, value);
			return true;
		} catch (Exception e) {
			shardedJedis = null;
			throw new RpcException(ECode.REDIS_OPRR_ERROR.getDesc(), ECode.REDIS_OPRR_ERROR.getErrorCode());
		}
		finally {
			returnResource(shardedJedis);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bfd.bip.db.redis.RedisClientAPI#getBytes(byte[])
	 */
	public byte[] getBytes(byte[] key) throws RpcException{
		// TODO Auto-generated method stub
		ShardedJedis shardedJedis = shardedJedisPool.getResource();
		try {
			return shardedJedis.get(key);
		} catch (Exception e) {
			shardedJedis = null;
			throw new RpcException(ECode.REDIS_OPRR_ERROR.getDesc(), ECode.REDIS_OPRR_ERROR.getErrorCode());
		}
		finally {
			returnResource(shardedJedis);
		}
	}

    public static void main(String[] args) {
        try {
            RedisClientAPI client = new RedisClientImpl();
            client.initial();
            String r = client.get("Cyouban>31017>NewsBase");
            log.info("result:" + r);
        } catch (Exception e) {
            log.error("Error: ", e);
        }
    }

}

