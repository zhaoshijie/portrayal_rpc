package com.bfd.portrayalrpc.util;

/**
 * Created by ronghua on 2015/7/15.
 */
public enum ECode {
    PARAM_ERROR(40001, "Parmeter Error, cid or iid or req_type is none"),
    PARAM_PARSE_ERROR(40002, "Parmeter Parse Error, String to JsonObject Exception"),
    REDIS_CONNECT_ERROR(40002, "Redis Connect Failed"),
    HBASE_CONNECT_ERROR(40003, "Hbase Connect Failed"),
    REDIS_OPRR_ERROR(40004, "Redis Oper Failed"),
    HBASE_OPER_ERROR(40005, "Hbase Oper Failed"),
    INTERNAL_ERROR(40006, "Internal Error"),
	REQ_TYPE_ERROR(40007, "request type is error"),
	PROTOBUF_PARSE_ERROR(40008, "byte to protobuf error"),
	PROTOBUF_TO_JSON_ERROR(40009, "protobuf  to json String error");
	

    private ECode(Integer errorCode, String desc) {
        this.errorCode = errorCode;
        this.desc = desc;
    }

    public static String getDescByErrorCode(Integer errorCode) {
        for (ECode eCode : ECode.values()) {
            if (eCode.getErrorCode() == errorCode) {
                return eCode.getDesc();
            }
        }
        return "";
    }

    private Integer errorCode;
    private String desc;

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
