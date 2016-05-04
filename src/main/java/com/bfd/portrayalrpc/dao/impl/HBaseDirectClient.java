package com.bfd.portrayalrpc.dao.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bfd.portrayalrpc.Exception.RpcException;
import com.bfd.portrayalrpc.dao.HBaseClientAPI;
import com.bfd.portrayalrpc.util.ECode;
import com.bfd.portrayalrpc.util.SysConfig;

/**
 * Created by Zheng.Liu (Eric) on 2015/6/12.
 * 
 * <pre>
 * Tool function to operate Hbase DB directly
 * </pre>
 */
@SuppressWarnings("deprecation")
public class HBaseDirectClient implements HBaseClientAPI {

	private static final Logger log = LoggerFactory.getLogger(HBaseDirectClient.class);

	public static Configuration configuration;
	public static HTablePool pool = null;

	/**
	 * Initialize HBase Connection info
	 * 
	 * @throws HBaseInitialException
	 */
	@SuppressWarnings("deprecation")
	public void init() throws RpcException {
		try {
			log.info("Initial HBase configuration HBase Configuration load start ..." + 
					"\nhbase.rootdir = " + SysConfig.HBASE_ROOT_DIR +
					"\nhbase.zookeeper.property.clientPort = " + SysConfig.HBASE_ZOOKEEPER_CLIENTPORT +
					"\nhbase.cluster.distributed = " + SysConfig.HBASE_CLUSTER_DISTRIBUTED +
					"\nhbase.zookeeper.quorum = " + SysConfig.HBASE_ZOOKEEPER_QUORUM);
			configuration = HBaseConfiguration.create();
			configuration.set("hbase.rootdir", SysConfig.HBASE_ROOT_DIR);
			configuration.set("hbase.zookeeper.property.clientPort", SysConfig.HBASE_ZOOKEEPER_CLIENTPORT);
			configuration.set("hbase.cluster.distributed", SysConfig.HBASE_CLUSTER_DISTRIBUTED);
			configuration.set("hbase.zookeeper.quorum", SysConfig.HBASE_ZOOKEEPER_QUORUM);
			
			pool = new HTablePool(configuration, 100);
			log.info("Initial HBase configuration HBase Configuration load finish ...");
		} catch (Exception e) {
			throw new RpcException(
					"HBase configuration load error!!!", e);
		}
	}

	/**
	 * Create HBase table
	 *
	 * @param tableName
	 * @param colFamilies
	 * @throws HBaseInitialException
	 * @throws HBaseTableException
	 */
	public void createTable(String tableName, String[] colFamilies)
			throws RpcException {
		log.info("HBase Table Creation",
				"HBase Table Creation started [" + tableName + "]......");
		if (configuration == null) {
			log.error("HBase Table Creation Fail",
					"HBase configuration not loaded...");
			throw new RpcException("HBase coniguration not loaded...");
		}
		try {
			HBaseAdmin hBaseAdmin = new HBaseAdmin(configuration);
			if (hBaseAdmin.tableExists(tableName)) {
				//hBaseAdmin.disableTable(tableName);
				//hBaseAdmin.deleteTable(tableName);
				log.info("HBase Table Creation", "Table "
						+ tableName + " exist, delete..");
			}
			HTableDescriptor tableDescriptor = new HTableDescriptor(TableName.valueOf(tableName));
			for (String cf : colFamilies) {
				tableDescriptor.addFamily(new HColumnDescriptor(cf));
			}
			hBaseAdmin.createTable(tableDescriptor);
			hBaseAdmin.close();
		} catch (MasterNotRunningException e) {
			throw new RpcException(
					"HBase Table creation fail, MasterNotRunningException...",
					e);
		} catch (ZooKeeperConnectionException e) {
			throw new RpcException(
					"HBase Table creation fail, ZooKeeperConnectionException...",
					e);
		} catch (IOException e) {
			throw new RpcException(
					"HBase Table creation fail, IOException...", e);
		}
		log.info("HBase Table Creation Complete", "Table "
				+ tableName + " created...");
	}

	/**
	 * Insert data to HBase
	 * 
	 * @param tableName
	 * @param rowKey
	 * @param colFamily
	 * @param qualifier
	 * @param value
	 * @throws HBaseDataException
	 * @throws IOException
	 */
	public void insertData(String tableName, String rowKey, String colFamily,
			String qualifier, String value) throws RpcException,
			IOException {
		HTableInterface hTable = pool.getTable(tableName);
		Put put = new Put(rowKey.getBytes());
		put.add(colFamily.getBytes(), qualifier.getBytes(), value.getBytes());
		try {
			hTable.put(put);
		} catch (Exception e) {
			throw new RpcException("Data insert error...", e);
		}
	}

	/**
	 * Write Item Profile or Item Base bytes data to Hbase
	 * 
	 * @param tableName
	 * @param rowKey
	 * @param colFamily
	 * @param qualifier
	 * @param byteArray
	 * @throws HBaseDataException
	 * @throws IOException
	 */
	@SuppressWarnings("deprecation")
	public void writeItemBytes(String tableName, String rowKey,
			String colFamily, String qualifier, byte[] byteArray)
			throws RpcException, IOException {
		@SuppressWarnings("deprecation")
		HTableInterface hTable = pool.getTable(tableName);
		Put put = new Put(rowKey.getBytes());
		put.add(colFamily.getBytes(), qualifier.getBytes(), byteArray);
		try {
			hTable.put(put);
		} catch (Exception e) {
			throw new RpcException("Data insert error...", e);
		} finally {
			hTable.close();
		}
	}

	/**
	 * Insert data in Batch
	 *
	 * @param tableName
	 * @param rowKey
	 * @param dataSet
	 *            [{family,qualifier,data},{}]
	 */
	public void insertData(String tableName, String rowKey, String[][] dataSet) {
		HTableInterface hTable = pool.getTable(tableName);
		Put put = new Put(rowKey.getBytes());
		for (String[] data : dataSet) {
			put.add(data[0].getBytes(), data[1].getBytes(), data[2].getBytes());//
		}
		try {
			hTable.put(put);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Drop a Table from HBase
	 *
	 * @param tableName
	 */
	public void dropTable(String tableName) {
		try {
			HBaseAdmin admin = new HBaseAdmin(configuration);
			admin.disableTable(tableName);
			admin.deleteTable(tableName);
		} catch (MasterNotRunningException e) {
			e.printStackTrace();
		} catch (ZooKeeperConnectionException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Delete by row key
	 *
	 * @param tablename
	 * @param rowkey
	 */
	public void deleteByRowKey(String tablename, String rowkey) {
		try {
			HTableInterface hTable = pool.getTable(tablename);
			List list = new ArrayList();
			Delete d1 = new Delete(rowkey.getBytes());
			list.add(d1);
			hTable.delete(list);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Query all rows TODO to be used later
	 * 
	 * @param tableName
	 */
	public void QueryAll(String tableName) {
		HTableInterface hTable = pool.getTable(tableName);
		try {
			ResultScanner rs = pool.getTable(tableName).getScanner(new Scan());
			for (Result r : rs) {
				for (KeyValue keyValue : r.raw()) {
					// logger.debug("Row Family:"
					// + new String(keyValue.getFamily())
					// + "Row Qualifier:"
					// + new String(keyValue.getQualifier())
					// + "Row Value:" + new String(keyValue.getValue()));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Query HBase data via rowKey and qualifier
	 * 
	 * @param tableName
	 * @param rowKey
	 * @param qualifier
	 * @throws RpcException 
	 * @throws IOException
	 */
	public byte[] queryByRowKey(String tableName, String rowKey,
			String colFamily, String qualifier) throws RpcException {
		try {
			HTableInterface htable = pool.getTable(tableName);
			Get get = new Get(rowKey.getBytes());
			get.addColumn(colFamily.getBytes(), qualifier.getBytes());
			Result r = htable.get(get);
			for (KeyValue keyValue : r.raw()) {
				return keyValue.getValue();
			}
		} catch (Exception e) {
			log.error("HBase Data Query Fail",
					"HBase Query Error: rowKey = {" + rowKey + "}, erorr = {"
							+ e.getMessage() + "}...", e);
			throw new RpcException(ECode.HBASE_OPER_ERROR.getDesc(), ECode.HBASE_OPER_ERROR.getErrorCode());
		}
		return null;
	}

	public void closeConnection() {
		try {
			pool.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error("HBase HConnection Close Exception",
					"Close HConnection failed...", e);
		}
	}

	public Map<String, byte[]> queryByRowKey(String tableName, String rowKey, String colFamily, String[] qulifiers)
			throws RpcException {
		// TODO Auto-generated method stub
		@SuppressWarnings("deprecation")
		Map<String, byte[]> map_value = new HashMap<String, byte[]>();
		try {

			HTableInterface htable = pool.getTable(tableName);
			Get scan = new Get(rowKey.getBytes());
			for (String qulifier : qulifiers)
				scan.addColumn(colFamily.getBytes(), qulifier.getBytes());
			Result r = htable.get(scan);
			for (KeyValue keyValue : r.raw()) {
				map_value.put(new String(keyValue.getQualifier()), keyValue.getValue());
			}
			return map_value;
		} catch (IOException e) {
			log.error("HBase Data Query Fail",
					"HBase Query Error: rowKey = {" + rowKey + "}, erorr = {"
							+ e.getMessage() + "}...", e);
			throw new RpcException(ECode.HBASE_OPER_ERROR.getDesc(), ECode.HBASE_OPER_ERROR.getErrorCode());
		}
	}    
	public static void main(String[] args) {
        try {
        	HBaseDirectClient client = new HBaseDirectClient();
            client.init();
            String [] colfamily = {"i"};
            client.createTable("TestTest", colfamily);
        } catch (Exception e) {
            log.error("Error: ", e);
        }
    }
}
