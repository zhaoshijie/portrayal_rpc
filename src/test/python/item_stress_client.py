import random
import os
import traceback
import logging
import json
import multiprocessing
import redis
import time
from  cost_tracker import TimeTracker
from thrift.transport import TSocket, TTransport
from thrift.protocol import TBinaryProtocol
from thrift.transport.TTransport import TBufferedTransport

from bfd.interface import PortrayalService
from bfd.interface.ttypes import *
from set_logger import set_logger
from NewsProfile_pb2 import NewsBase, NewsProfile
from ItemProfile_pb2 import ItemBase, ItemProfile
from pbjson import pb2dict, pb2json



cur_dir=os.path.dirname(os.path.abspath(__file__)) or os.getcwd()
set_logger(cur_dir + "/logs/stress_test.log")


class Connection(object):
    def __init__(self, ip, port, name):
        self.ip = ip
        self.port = port
        self.name = name
        self.transport = TSocket.TSocket(ip, port)
        self.transport.setTimeout(2000)
        self.transport = TTransport.TBufferedTransport(self.transport)
        self.protocol = TBinaryProtocol.TBinaryProtocol(self.transport)

    def connect(self):
        msg = "Connect must implement connect"
        raise  Exception(msg)

    def close(self):
        if self.transport:
            self.transport.close()

"""new server client connection class"""
class PortrayalConnection(Connection):

    def __init__(self, ip, port, name):
        super(PortrayalConnection, self).__init__(ip, port, name)

    def connect(self):
        self.__service = PortrayalService.Client(self.protocol)
        self.transport.open()
        if not self.__service:
            raise RuntimeError("Invalid Portrayal Thrift init params")
    def get_info_data(self, cid, iid, req_type):
        return self.__service.get_info_data(cid, iid, req_type)



pool = redis.ConnectionPool(host='172.18.1.101', port=6379, db=0)
client = None

def init():
    global client
    client = redis.Redis(connection_pool=pool)
    print("init succeed")

def process(num,l, client, process_id, datas):
    count = 0
    err_count = 0
    time_tracker = TimeTracker()
    rpc_con = PortrayalConnection("localhost", 19090, "Portayal")
    rpc_con.connect()
    pid = os.getpid()
    set_logger("%s/logs/stress_test_%s.log" %(cur_dir, pid))
    data_len = len(datas) - 1

    while True:
        index = random.randint(0, data_len)
        key_base = datas[index]
        try:
            ib_res = client.get(key_base)
            ib = ItemBase()
            ib.ParseFromString(ib_res)
            if not ib.cid:
                continue
        except Exception as e:
            print("get redis data key:%s  error:%s" % (key_base, e))
            continue
        l.acquire()
        if num.value >= 50000:
            l.release()
            break
        num.value = num.value + 1
        l.release()
        count = count + 1
        time_tracker.start_tracker()
        try:
            result = rpc_con.get_info_data(ib.cid, ib.iid, ReqType.ITEMBASE)
            print(ib.cid, ib.iid)
            #print("process_id:%s, count:%s" %(process_id, count))
        except Exception as e:
            err_count = err_count + 1
            rpc_con.close()
            rpc_con = PortrayalConnection("localhost", 19090, "Portayal")
            rpc_con.connect()
            print("get rpc server data key:%s error:%s" %(key_base, e))
        time_tracker.end_tracker()
        time_tracker.time_elapsed()
        time_tracker.inc_job_counter(1)
        job_count = time_tracker.get_job_count()
    avg_time = time_tracker.average_time_cost()
    request_num = time_tracker.get_job_count()
    all_time = time_tracker.get_time_elapsed()
    print("avg_time:%s, request_num:%s, all_time:%s, err_count:%s" % (avg_time, request_num, all_time, err_count))

def read_data(file):
    fd = open(file)
    datas = []
    for value in fd.readlines():
        value_ = value.split('\t')
        data="%s>%s>ItemBase" % (value_[0].replace("\n",""), value_[1].replace("\n",""))
        datas.append(data)
    return datas

def main(thread_counts):
    global client
    num = multiprocessing.Value('d', 0.0)
    lock = multiprocessing.Lock()
    init()
    jobs = []
    datas = read_data("stress_item.txt")
    all_time_start = time.time()
    for i in range(thread_counts):
        p = multiprocessing.Process(target=process, args=(num, lock, client, i, datas))
        jobs.append(p)
        p.start()
    for job in jobs:
        job.join()
    all_time_end = time.time()
    print("num-value:%s all-time:%s" % (num.value, all_time_end-all_time_start))
main(50)
