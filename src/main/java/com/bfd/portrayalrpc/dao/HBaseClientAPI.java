package com.bfd.portrayalrpc.dao;

import java.io.IOException;
import java.util.Map;

import com.bfd.portrayalrpc.Exception.RpcException;

/**
 * Created by Zheng.Liu (Eric) on 2015/6/29. HBase DB Util
 */
public interface HBaseClientAPI {
	void init() throws RpcException;

	void writeItemBytes(String tableName, String rowKey, String colFamily,
			String qualifier, byte[] byteArray) throws RpcException, Exception;

	byte[] queryByRowKey(String tableName, String rowKey, String colFamily,
			String qualifier) throws RpcException, Exception;
	
	Map<String, byte[]> queryByRowKey(String tableName, String rowKey, String colFamily,
			String [] qulifiers) throws RpcException, Exception;

	void closeConnection();
}