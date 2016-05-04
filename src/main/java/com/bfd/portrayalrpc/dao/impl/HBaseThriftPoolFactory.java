package com.bfd.portrayalrpc.dao.impl;

import org.apache.commons.pool.BasePoolableObjectFactory;
import org.apache.thrift.TServiceClient;
import org.apache.thrift.TServiceClientFactory;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HBaseThriftPoolFactory extends BasePoolableObjectFactory<TServiceClient> {

	private static final Logger log = LoggerFactory.getLogger(HBaseThriftPoolFactory.class);
	private final TServiceClientFactory<? extends TServiceClient> clientFactory;
	private final String ip;
	private final int port;
	private final int timeout ;
	
    public HBaseThriftPoolFactory(TServiceClientFactory<? extends TServiceClient> clientFactory,
    		String ip, int port, int timeout) {
        this.clientFactory = clientFactory;
        this.ip = ip;
        this.port = port;
        this.timeout = timeout;
    }

	@Override
	public TServiceClient makeObject() throws Exception {
		log.info("create a connection");
            TSocket tsocket = new TSocket(ip, port, timeout);
            TProtocol protocol = new TBinaryProtocol(tsocket);
            TServiceClient client = clientFactory.getClient(protocol);
            tsocket.open();
            return client;
	}
	
	@Override
	public void destroyObject(TServiceClient client) throws Exception {
            TTransport tp = client.getInputProtocol().getTransport();
            tp.close();
        }

	@Override
	public boolean validateObject(TServiceClient client) {
            TTransport tp = client.getInputProtocol().getTransport();
            return tp.isOpen();
        }

    }