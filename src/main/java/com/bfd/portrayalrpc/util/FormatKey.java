package com.bfd.portrayalrpc.util;

public class FormatKey {
	public static String redis_item_base(String cid, String iid){
		return String.format("%s>%s>%s", cid, iid, "ItemBase");
	}
	public static String redis_item_profile(String cid, String iid){
		return String.format("%s>%s>%s", cid, iid, "ItemProfile");
	}
	public static String redis_news_profile(String cid, String iid){
		return String.format("%s>%s>%s", cid, iid, "NewsProfile");
	}
	public static String redis_news_base(String cid, String iid){
		return String.format("%s>%s>%s", cid, iid, "NewsBase");
	}
	public static String hbase_format(String cid, String iid) {
		return String.format("%s>%s", cid, iid);
	}

}
