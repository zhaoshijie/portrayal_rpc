package com.bfd.portrayalrpc.Exception;

/**
 * Created by ronghua on 2015/7/16.
 */
@SuppressWarnings("serial")
public class RpcException extends Exception {
    //exception code
    private int exceptionCode = 40001;
    
    public RpcException() {
    	super("Rpc Error");
    	exceptionCode = 40001;
    }

    public RpcException(String msg) {
        super(msg);
        exceptionCode = 40001;
    }
    
    public RpcException(String msg, int except_code){
    	super(msg);
    	exceptionCode = except_code;
    }

    public RpcException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public RpcException(Throwable cause) {
        super(cause);
    }
    
    public int getExceptionCode() {
        return exceptionCode;
    }
}
