package com.bfd.portrayalrpc.dao.impl;

import java.io.IOException;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.apache.commons.lang.StringUtils;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.hadoop.hbase.thrift2.generated.TColumnValue;
import org.apache.hadoop.hbase.thrift2.generated.TGet;
import org.apache.hadoop.hbase.thrift2.generated.THBaseService.Client;
import org.apache.hadoop.hbase.thrift2.generated.TIOError;
import org.apache.hadoop.hbase.thrift2.generated.TPut;
import org.apache.hadoop.hbase.thrift2.generated.TResult;
import org.apache.thrift.TException;
import org.apache.thrift.TServiceClient;
import org.apache.thrift.TServiceClientFactory;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bfd.portrayalrpc.Exception.RpcException;
import com.bfd.portrayalrpc.dao.HBaseClientAPI;
import com.bfd.portrayalrpc.util.ECode;
import com.bfd.portrayalrpc.util.SysConfig;


public class HBaseThriftPoolClient implements HBaseClientAPI {

	private static final Logger log = LoggerFactory.getLogger(HBaseThriftPoolClient.class);
	private String host = "localhost";
	private int port = 9090;
	private boolean secure = false;
	
	private GenericObjectPool<TServiceClient> pool; 
	
	 // 下面的配置项是连接池的基本配置
    /** 超时时间，单位为ms，默认为3s */
    private int timeout = 3000;

    /** 最大活跃连接数 */
    private int maxActive = 1024;

    /** 链接池中最大空闲的连接数,默认为100 */
    private int maxIdle = 100;

    /** 连接池中最少空闲的连接数,默认为0 */
    private int minIdle = 0;

    /** 当连接池资源耗尽时，调用者最大阻塞的时间 */
    private int maxWait = 2000;

    /** 空闲链接”检测线程，检测的周期，毫秒数，默认位3min，-1表示关闭空闲检测 */
    private int timeBetweenEvictionRunsMillis = 180000;

    /** 空闲时是否进行连接有效性验证，如果验证失败则移除，默认为false */
    private boolean testWhileIdle = false;
	
	public void init() throws RpcException {
		log.info("Initial HBase HBaseThriftClient",
				"HBase HBaseThriftClient load start ...");
		try {
			host = SysConfig.HBASE_THRIFT_HOST;
			if (StringUtils.isEmpty(host)) {
				throw new Exception(
						"hbase.thrift.host configuration error, NULL...");
			}
			port = SysConfig.HBASE_THRIFT_PORT;
			secure = SysConfig.HBASE_THRIFT_ISSECURE;
			timeout = SysConfig.HBASE_THRIFT_TIMEOUT;
			pool = bulidClientPool();
			
		} catch (Exception e) {
			log.error("HBaseThriftClient: Failed to build hbase thrift connection",
							"Failed to build hbase "
									+ "thrift connection, host : " + host
									+ ", port = " + port, e);
			throw new RpcException(
					"HBaseThriftClient configuration load error!!!", e);
		}

		log.info("Finish initial HBase HBaseThriftClient",
				"HBase HBaseThriftClient load finish ...");
	}

	
    protected GenericObjectPool<TServiceClient> bulidClientPool() {
        // 设置poolConfig
    	GenericObjectPool.Config poolConfig = new GenericObjectPool.Config();
        poolConfig.maxActive = maxActive;
        poolConfig.maxIdle = maxIdle;
        poolConfig.minIdle = minIdle;
        poolConfig.maxWait = maxWait;
        poolConfig.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
        poolConfig.testWhileIdle = testWhileIdle;
        TServiceClientFactory<? extends TServiceClient> clientFactory = new Client.Factory();
        
        HBaseThriftPoolFactory clientPool = new HBaseThriftPoolFactory(clientFactory, host, port, timeout);
        return new GenericObjectPool<TServiceClient>(clientPool, poolConfig);
    }


	/**
	 * Write data to hbase
	 *
	 * @param tableName
	 * @param rowKey
	 * @param colFamily
	 * @param colName
	 * @param byteArray
	 * @throws HBaseDataException
	 * @throws IOException
	 * @throws TIOError
	 * @throws TException
	 */
	public void writeItemBytes(final String tableName, final String rowKey,
			final String colFamily, final String colName, final byte[] byteArray)
			throws Exception {
		Client client = null;
		try {
			client = (Client) pool.borrowObject();
			ByteBuffer table = ByteBuffer.wrap(tableName.getBytes());
				TPut put = new TPut();
				put.setRow(rowKey.getBytes());
				TColumnValue columnValue = new TColumnValue();
				columnValue.setFamily(colFamily.getBytes());
				columnValue.setQualifier(colName.getBytes());
				columnValue.setValue(byteArray);
				List<TColumnValue> columnValues = new ArrayList<TColumnValue>();
				columnValues.add(columnValue);
				put.setColumnValues(columnValues);
				client.put(table, put);
		} catch(TTransportException e){
			Throwable cause = e.getCause();
			log.error("Hbase Thrift TTransportException", e);
            if (cause.getCause() != null && cause.getCause() instanceof SocketException) {
                try {
                    pool.clear();
                    log.error("clear all the connection");
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                log.error("Hbase Thrift SocketException clear all the pool connection");
            } else {
            	pool.invalidateObject(client);
            }

            client = null; // 这里是为了防止后续return回pool中
            throw new RpcException(ECode.HBASE_OPER_ERROR.getDesc(), ECode.HBASE_OPER_ERROR.getErrorCode());
		}catch (Exception e) {
			log.error("Hbase Thrift TTransportException" ,e);
			throw new RpcException(ECode.HBASE_OPER_ERROR.getDesc(), ECode.HBASE_OPER_ERROR.getErrorCode());
		}finally{
			if (client != null){
				pool.returnObject(client);
			}
		}
	}

	/**
	 * Query column value via rowKey
	 *
	 * @param tableName
	 * @param rowKey
	 * @param colFamily
	 * @param colName
	 * @return
	 * @throws Exception
	 */
	public byte[] queryByRowKey(final String tableName, final String rowKey,
			final String colFamily, final String colName) throws Exception, RpcException {
		Client client = null;
		try {
			client = (Client) pool.borrowObject();
			ByteBuffer table = ByteBuffer.wrap(tableName.getBytes());
			TGet get = new TGet();
			get.setRow(rowKey.getBytes());
			TResult result = client.get(table, get);
			if (result != null && result.getColumnValues().size() > 0) {
				for (TColumnValue resultColumnValue : result.getColumnValues()) {
						if (colFamily.equals(new String(resultColumnValue.getFamily()))
								&& colName.equals(new String(resultColumnValue.getQualifier()))) {
							return resultColumnValue.getValue();
						}
					}
				} 
			else{
				System.out.println("No data found from hbase for rowkey :" + rowKey);
				}
			} catch(TTransportException e){
				Throwable cause = e.getCause();
				log.error("Hbase Thrift TTransportException", e);
	            if (cause.getCause() != null && cause.getCause() instanceof SocketException) {
	                try {
	                    pool.clear();
	                    log.error("clear all the connection");
	                } catch (Exception e1) {
	                    e1.printStackTrace();
	                }
	                log.error("Hbase Thrift SocketException clear all the pool connection");
	            } else {
	            	pool.invalidateObject(client);
	            }

	            client = null; // 这里是为了防止后续return回pool中
	            throw new RpcException(ECode.HBASE_OPER_ERROR.getDesc(), ECode.HBASE_OPER_ERROR.getErrorCode());
			}catch (Exception e) {
				log.error("Hbase Thrift TTransportException" ,e);
				throw new RpcException(ECode.HBASE_OPER_ERROR.getDesc(), ECode.HBASE_OPER_ERROR.getErrorCode());
			}finally{
				if (client != null){
					pool.returnObject(client);
				}
			}

		return null;
	}

	public void closeConnection() {
		pool.clear();
	}

	public Map<String, byte[]> queryByRowKey(String tableName, String rowKey, String colFamily, String[] qulifiers)
			throws Exception {
		Map<String, byte[]> ret = new HashMap<String, byte[]>();
		Client client = null;
		try {
			client = (Client) pool.borrowObject();
			ByteBuffer table = ByteBuffer.wrap(tableName.getBytes());
			TGet get = new TGet();
			get.setRow(rowKey.getBytes());
			TResult result = client.get(table, get);
			if (result != null && result.getColumnValues().size() > 0) {
				for (TColumnValue resultColumnValue : result.getColumnValues()) {
					for (String qulifier: qulifiers){
						if (colFamily.equals(new String(resultColumnValue.getFamily()))
								&& qulifier.equals(new String(resultColumnValue.getQualifier()))) {
							ret.put(qulifier, resultColumnValue.getValue());
							}
						}
					}
				}else {
					System.out.println("No data found from hbase for rowkey :" + rowKey);
					}
			return ret;
			} catch(TTransportException e){
				Throwable cause = e.getCause();
				log.error("Hbase Thrift TTransportException", e);
	            if (cause.getCause() != null && cause.getCause() instanceof SocketException) {
	                try {
	                    pool.clear();
	                    log.error("clear all the connection");
	                } catch (Exception e1) {
	                    e1.printStackTrace();
	                }
	                log.error("Hbase Thrift SocketException clear all the pool connection");
	            } else {
	            	pool.invalidateObject(client);
	            }

	            client = null; // 这里是为了防止后续return回pool中
	            throw new RpcException(ECode.HBASE_OPER_ERROR.getDesc(), ECode.HBASE_OPER_ERROR.getErrorCode());
			} catch (Exception e) {
				log.error("Hbase Thrift TTransportException" ,e);
				throw new RpcException(ECode.HBASE_OPER_ERROR.getDesc(), ECode.HBASE_OPER_ERROR.getErrorCode());
			} finally {
				if (client != null){
					pool.returnObject(client);
				}
			}
	}
}
