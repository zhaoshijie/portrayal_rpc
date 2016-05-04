#coding=utf-8
import os
import traceback
import logging
import json
import sys

from NewsProfile_pb2 import NewsBase, NewsProfile
from ItemProfile_pb2 import ItemBase, ItemProfile
from pbjson import pb2dict, pb2json
from thrift.transport import TSocket, TTransport
from thrift.protocol import TBinaryProtocol
from thrift.transport.TTransport import TBufferedTransport

from bfd.interface import PortrayalService
from bfd.interface.ttypes import *

from set_logger import set_logger

cur_dir=os.path.dirname(os.path.abspath(__file__)) or os.getcwd()
set_logger(cur_dir + "/logs/fun_test.log")


class Connection(object):
    def __init__(self, ip, port, name):
        self.ip = ip
        self.port = port
        self.name = name
        self.transport = TSocket.TSocket(ip, port)
        self.transport = TTransport.TBufferedTransport(self.transport)
        self.protocol = TBinaryProtocol.TBinaryProtocol(self.transport)

    def connect(self):
        msg = "Connect must implement connect"
        raise  Exception(msg)

    def close(self):
        if self.transport:
            self.transport.close()

"""new server client connection class"""
class PortrayalRpcConnection(Connection):

    def __init__(self, ip, port, name):
        super(PortrayalRpcConnection, self).__init__(ip, port, name)

    def connect(self):
        self.__service = PortrayalService.Client(self.protocol)
        self.transport.open()
        if not self.__service:
            raise RuntimeError("Invalid Portrayal Thrift init params")

    def get_info_data(self, cid, iid, req_type):
        return self.__service.get_info_data(cid, iid, req_type)

    def get_data(self, request):
        return self.__service.get_data(request)

    def get_json_data(self, request):
        return self.__service.get_json_data(request)

def item_print(ret):
    if ret and ret.status == 0:
        if ret.base:
            ib = ItemBase()
            ib.ParseFromString(ret.base)
            print ib.cid
        if ret.profile:
            ip = ItemProfile()
            ip.ParseFromString(ret.profile)
            print ip.cid
        if not ret.base and not ret.profile:
            print("No data")
    elif ret and ret.status != 0:
        print(ret.msg)

def news_print(ret):
    if ret and ret.status == 0:
        if ret.base:
            ib = NewsBase()
            ib.ParseFromString(ret.base)
            print ib.cid
        if ret.profile:
            ip = NewsProfile()
            ip.ParseFromString(ret.profile)
            print ip.cid
        if not ret.base and not ret.profile:
            print("No data")
         
    elif ret and ret.status != 0:
        print(ret.msg)

def test_item(client, req_type):
    # test item
    cid = "Cjinshan"
    iid = "b1a4ee97f6b51c4f702948a9a1a303bd1"
    ret = client.get_info_data(cid, iid, req_type)
    item_print(ret)
    request = {"cid": cid, "iid": iid, "req_type": req_type}
    ret = client.get_data(json.dumps(request))
    item_print(ret)
    ret = client.get_json_data(json.dumps(request))
    print ret


def test_news(client, req_type):
    # test news
    cid = "Cyouban"
    iid = "31017"
    cid = "Cqichedp"
    iid = "1000352"
    cid = "C3dm"
    iid = "00c19f80d4ee27d09333320ab889ef59"
    ret = client.get_info_data(cid, iid, req_type)
    news_print(ret)

    request = {"cid": cid, "iid": iid, "req_type": req_type}
    ret = client.get_data(json.dumps(request))
    news_print(ret)

    ret = client.get_json_data(json.dumps(request))
    print(ret)

def main():
    client = PortrayalRpcConnection("localhost", 19090, "portrayalrpc")
    client.connect()
    # test item
    test_item(client, ReqType.ITEM)
    print "-----------------------------------------------------------\n"
    test_item(client, ReqType.ITEMBASE)
    print "-----------------------------------------------------------\n"
    test_item(client, ReqType.ITEMPROFILE)
    print "-----------------------------------------------------------\n"
    # test news
    #test_news(client, ReqType.NEWS)
    print "-----------------------------------------------------------\n"
    test_news(client, ReqType.NEWSBASE)
    print "-----------------------------------------------------------\n"
    test_news(client, ReqType.NEWSPROFILE)

if __name__ == "__main__":
    for i in range(0,1):
        main()
