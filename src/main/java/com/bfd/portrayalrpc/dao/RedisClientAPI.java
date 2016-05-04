package com.bfd.portrayalrpc.dao;

import com.bfd.portrayalrpc.Exception.RpcException;

/**
 * Created by Zheng.Liu (Eric) on 2015/6/30.
 */
public interface RedisClientAPI {

	void initial() throws Exception;

	boolean set(final String key, String value) throws RpcException;

	String get(final String key) throws RpcException;

	boolean setBytes(byte[] key, byte[] value) throws RpcException;

	byte[] getBytes(byte[] key) throws RpcException;

	boolean lpush(final String key, String... values) throws RpcException;

	boolean rpush(final String key, String... values)throws RpcException;

	String lpop(final String key) throws RpcException;

	String rpop(final String key) throws RpcException;

	boolean del(String key) throws RpcException;
}

