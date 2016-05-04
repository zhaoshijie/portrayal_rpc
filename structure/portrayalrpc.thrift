namespace java com.bfd.portrayalrpc.thrift
namespace py bfd.interface
 
struct ErrMsg {
	1: i32 code,
	2: string descirbe
}
 
struct Result {
	1: i32 status,
	2: binary base,
        3: binary profile,
	4: ErrMsg msg
}

enum ReqType {
	ITEM=0,
        NEWS=1,
        ITEMBASE=2,
        ITEMPROFILE=3,
        NEWSBASE=4,
        NEWSPROFILE=5
}

service PortrayalService{
        Result get_data(1:string request),
        Result get_info_data(1:string cid, 2:string iid, 3:ReqType req_type),
	string get_json_data(1:string request)
}
