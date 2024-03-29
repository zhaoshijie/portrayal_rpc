#
# Autogenerated by Thrift Compiler (0.9.2)
#
# DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
#
#  options string: py
#

from thrift.Thrift import TType, TMessageType, TException, TApplicationException

from thrift.transport import TTransport
from thrift.protocol import TBinaryProtocol, TProtocol
try:
  from thrift.protocol import fastbinary
except:
  fastbinary = None


class ReqType:
  ITEM = 0
  NEWS = 1
  ITEMBASE = 2
  ITEMPROFILE = 3
  NEWSBASE = 4
  NEWSPROFILE = 5

  _VALUES_TO_NAMES = {
    0: "ITEM",
    1: "NEWS",
    2: "ITEMBASE",
    3: "ITEMPROFILE",
    4: "NEWSBASE",
    5: "NEWSPROFILE",
  }

  _NAMES_TO_VALUES = {
    "ITEM": 0,
    "NEWS": 1,
    "ITEMBASE": 2,
    "ITEMPROFILE": 3,
    "NEWSBASE": 4,
    "NEWSPROFILE": 5,
  }


class ErrMsg:
  """
  Attributes:
   - code
   - descirbe
  """

  thrift_spec = (
    None, # 0
    (1, TType.I32, 'code', None, None, ), # 1
    (2, TType.STRING, 'descirbe', None, None, ), # 2
  )

  def __init__(self, code=None, descirbe=None,):
    self.code = code
    self.descirbe = descirbe

  def read(self, iprot):
    if iprot.__class__ == TBinaryProtocol.TBinaryProtocolAccelerated and isinstance(iprot.trans, TTransport.CReadableTransport) and self.thrift_spec is not None and fastbinary is not None:
      fastbinary.decode_binary(self, iprot.trans, (self.__class__, self.thrift_spec))
      return
    iprot.readStructBegin()
    while True:
      (fname, ftype, fid) = iprot.readFieldBegin()
      if ftype == TType.STOP:
        break
      if fid == 1:
        if ftype == TType.I32:
          self.code = iprot.readI32();
        else:
          iprot.skip(ftype)
      elif fid == 2:
        if ftype == TType.STRING:
          self.descirbe = iprot.readString();
        else:
          iprot.skip(ftype)
      else:
        iprot.skip(ftype)
      iprot.readFieldEnd()
    iprot.readStructEnd()

  def write(self, oprot):
    if oprot.__class__ == TBinaryProtocol.TBinaryProtocolAccelerated and self.thrift_spec is not None and fastbinary is not None:
      oprot.trans.write(fastbinary.encode_binary(self, (self.__class__, self.thrift_spec)))
      return
    oprot.writeStructBegin('ErrMsg')
    if self.code is not None:
      oprot.writeFieldBegin('code', TType.I32, 1)
      oprot.writeI32(self.code)
      oprot.writeFieldEnd()
    if self.descirbe is not None:
      oprot.writeFieldBegin('descirbe', TType.STRING, 2)
      oprot.writeString(self.descirbe)
      oprot.writeFieldEnd()
    oprot.writeFieldStop()
    oprot.writeStructEnd()

  def validate(self):
    return


  def __hash__(self):
    value = 17
    value = (value * 31) ^ hash(self.code)
    value = (value * 31) ^ hash(self.descirbe)
    return value

  def __repr__(self):
    L = ['%s=%r' % (key, value)
      for key, value in self.__dict__.iteritems()]
    return '%s(%s)' % (self.__class__.__name__, ', '.join(L))

  def __eq__(self, other):
    return isinstance(other, self.__class__) and self.__dict__ == other.__dict__

  def __ne__(self, other):
    return not (self == other)

class Result:
  """
  Attributes:
   - status
   - base
   - profile
   - msg
  """

  thrift_spec = (
    None, # 0
    (1, TType.I32, 'status', None, None, ), # 1
    (2, TType.STRING, 'base', None, None, ), # 2
    (3, TType.STRING, 'profile', None, None, ), # 3
    (4, TType.STRUCT, 'msg', (ErrMsg, ErrMsg.thrift_spec), None, ), # 4
  )

  def __init__(self, status=None, base=None, profile=None, msg=None,):
    self.status = status
    self.base = base
    self.profile = profile
    self.msg = msg

  def read(self, iprot):
    if iprot.__class__ == TBinaryProtocol.TBinaryProtocolAccelerated and isinstance(iprot.trans, TTransport.CReadableTransport) and self.thrift_spec is not None and fastbinary is not None:
      fastbinary.decode_binary(self, iprot.trans, (self.__class__, self.thrift_spec))
      return
    iprot.readStructBegin()
    while True:
      (fname, ftype, fid) = iprot.readFieldBegin()
      if ftype == TType.STOP:
        break
      if fid == 1:
        if ftype == TType.I32:
          self.status = iprot.readI32();
        else:
          iprot.skip(ftype)
      elif fid == 2:
        if ftype == TType.STRING:
          self.base = iprot.readString();
        else:
          iprot.skip(ftype)
      elif fid == 3:
        if ftype == TType.STRING:
          self.profile = iprot.readString();
        else:
          iprot.skip(ftype)
      elif fid == 4:
        if ftype == TType.STRUCT:
          self.msg = ErrMsg()
          self.msg.read(iprot)
        else:
          iprot.skip(ftype)
      else:
        iprot.skip(ftype)
      iprot.readFieldEnd()
    iprot.readStructEnd()

  def write(self, oprot):
    if oprot.__class__ == TBinaryProtocol.TBinaryProtocolAccelerated and self.thrift_spec is not None and fastbinary is not None:
      oprot.trans.write(fastbinary.encode_binary(self, (self.__class__, self.thrift_spec)))
      return
    oprot.writeStructBegin('Result')
    if self.status is not None:
      oprot.writeFieldBegin('status', TType.I32, 1)
      oprot.writeI32(self.status)
      oprot.writeFieldEnd()
    if self.base is not None:
      oprot.writeFieldBegin('base', TType.STRING, 2)
      oprot.writeString(self.base)
      oprot.writeFieldEnd()
    if self.profile is not None:
      oprot.writeFieldBegin('profile', TType.STRING, 3)
      oprot.writeString(self.profile)
      oprot.writeFieldEnd()
    if self.msg is not None:
      oprot.writeFieldBegin('msg', TType.STRUCT, 4)
      self.msg.write(oprot)
      oprot.writeFieldEnd()
    oprot.writeFieldStop()
    oprot.writeStructEnd()

  def validate(self):
    return


  def __hash__(self):
    value = 17
    value = (value * 31) ^ hash(self.status)
    value = (value * 31) ^ hash(self.base)
    value = (value * 31) ^ hash(self.profile)
    value = (value * 31) ^ hash(self.msg)
    return value

  def __repr__(self):
    L = ['%s=%r' % (key, value)
      for key, value in self.__dict__.iteritems()]
    return '%s(%s)' % (self.__class__.__name__, ', '.join(L))

  def __eq__(self, other):
    return isinstance(other, self.__class__) and self.__dict__ == other.__dict__

  def __ne__(self, other):
    return not (self == other)
