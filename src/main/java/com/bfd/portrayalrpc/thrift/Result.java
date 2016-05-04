/**
 * Autogenerated by Thrift Compiler (0.9.2)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.bfd.portrayalrpc.thrift;

import org.apache.thrift.scheme.IScheme;
import org.apache.thrift.scheme.SchemeFactory;
import org.apache.thrift.scheme.StandardScheme;

import org.apache.thrift.scheme.TupleScheme;
import org.apache.thrift.protocol.TTupleProtocol;
import org.apache.thrift.protocol.TProtocolException;
import org.apache.thrift.EncodingUtils;
import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.server.AbstractNonblockingServer.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.Set;
import java.util.HashSet;
import java.util.EnumSet;
import java.util.Collections;
import java.util.BitSet;
import java.nio.ByteBuffer;
import java.util.Arrays;
import javax.annotation.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked"})
@Generated(value = "Autogenerated by Thrift Compiler (0.9.2)", date = "2015-8-11")
public class Result implements org.apache.thrift.TBase<Result, Result._Fields>, java.io.Serializable, Cloneable, Comparable<Result> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("Result");

  private static final org.apache.thrift.protocol.TField STATUS_FIELD_DESC = new org.apache.thrift.protocol.TField("status", org.apache.thrift.protocol.TType.I32, (short)1);
  private static final org.apache.thrift.protocol.TField BASE_FIELD_DESC = new org.apache.thrift.protocol.TField("base", org.apache.thrift.protocol.TType.STRING, (short)2);
  private static final org.apache.thrift.protocol.TField PROFILE_FIELD_DESC = new org.apache.thrift.protocol.TField("profile", org.apache.thrift.protocol.TType.STRING, (short)3);
  private static final org.apache.thrift.protocol.TField MSG_FIELD_DESC = new org.apache.thrift.protocol.TField("msg", org.apache.thrift.protocol.TType.STRUCT, (short)4);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new ResultStandardSchemeFactory());
    schemes.put(TupleScheme.class, new ResultTupleSchemeFactory());
  }

  public int status; // required
  public ByteBuffer base; // required
  public ByteBuffer profile; // required
  public ErrMsg msg; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    STATUS((short)1, "status"),
    BASE((short)2, "base"),
    PROFILE((short)3, "profile"),
    MSG((short)4, "msg");

    private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

    static {
      for (_Fields field : EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // STATUS
          return STATUS;
        case 2: // BASE
          return BASE;
        case 3: // PROFILE
          return PROFILE;
        case 4: // MSG
          return MSG;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final String _fieldName;

    _Fields(short thriftId, String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  private static final int __STATUS_ISSET_ID = 0;
  private byte __isset_bitfield = 0;
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.STATUS, new org.apache.thrift.meta_data.FieldMetaData("status", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    tmpMap.put(_Fields.BASE, new org.apache.thrift.meta_data.FieldMetaData("base", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING        , true)));
    tmpMap.put(_Fields.PROFILE, new org.apache.thrift.meta_data.FieldMetaData("profile", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING        , true)));
    tmpMap.put(_Fields.MSG, new org.apache.thrift.meta_data.FieldMetaData("msg", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, ErrMsg.class)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(Result.class, metaDataMap);
  }

  public Result() {
  }

  public Result(
    int status,
    ByteBuffer base,
    ByteBuffer profile,
    ErrMsg msg)
  {
    this();
    this.status = status;
    setStatusIsSet(true);
    this.base = org.apache.thrift.TBaseHelper.copyBinary(base);
    this.profile = org.apache.thrift.TBaseHelper.copyBinary(profile);
    this.msg = msg;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public Result(Result other) {
    __isset_bitfield = other.__isset_bitfield;
    this.status = other.status;
    if (other.isSetBase()) {
      this.base = org.apache.thrift.TBaseHelper.copyBinary(other.base);
    }
    if (other.isSetProfile()) {
      this.profile = org.apache.thrift.TBaseHelper.copyBinary(other.profile);
    }
    if (other.isSetMsg()) {
      this.msg = new ErrMsg(other.msg);
    }
  }

  public Result deepCopy() {
    return new Result(this);
  }

  @Override
  public void clear() {
    setStatusIsSet(false);
    this.status = 0;
    this.base = null;
    this.profile = null;
    this.msg = null;
  }

  public int getStatus() {
    return this.status;
  }

  public Result setStatus(int status) {
    this.status = status;
    setStatusIsSet(true);
    return this;
  }

  public void unsetStatus() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __STATUS_ISSET_ID);
  }

  /** Returns true if field status is set (has been assigned a value) and false otherwise */
  public boolean isSetStatus() {
    return EncodingUtils.testBit(__isset_bitfield, __STATUS_ISSET_ID);
  }

  public void setStatusIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __STATUS_ISSET_ID, value);
  }

  public byte[] getBase() {
    setBase(org.apache.thrift.TBaseHelper.rightSize(base));
    return base == null ? null : base.array();
  }

  public ByteBuffer bufferForBase() {
    return org.apache.thrift.TBaseHelper.copyBinary(base);
  }

  public Result setBase(byte[] base) {
    this.base = base == null ? (ByteBuffer)null : ByteBuffer.wrap(Arrays.copyOf(base, base.length));
    return this;
  }

  public Result setBase(ByteBuffer base) {
    this.base = org.apache.thrift.TBaseHelper.copyBinary(base);
    return this;
  }

  public void unsetBase() {
    this.base = null;
  }

  /** Returns true if field base is set (has been assigned a value) and false otherwise */
  public boolean isSetBase() {
    return this.base != null;
  }

  public void setBaseIsSet(boolean value) {
    if (!value) {
      this.base = null;
    }
  }

  public byte[] getProfile() {
    setProfile(org.apache.thrift.TBaseHelper.rightSize(profile));
    return profile == null ? null : profile.array();
  }

  public ByteBuffer bufferForProfile() {
    return org.apache.thrift.TBaseHelper.copyBinary(profile);
  }

  public Result setProfile(byte[] profile) {
    this.profile = profile == null ? (ByteBuffer)null : ByteBuffer.wrap(Arrays.copyOf(profile, profile.length));
    return this;
  }

  public Result setProfile(ByteBuffer profile) {
    this.profile = org.apache.thrift.TBaseHelper.copyBinary(profile);
    return this;
  }

  public void unsetProfile() {
    this.profile = null;
  }

  /** Returns true if field profile is set (has been assigned a value) and false otherwise */
  public boolean isSetProfile() {
    return this.profile != null;
  }

  public void setProfileIsSet(boolean value) {
    if (!value) {
      this.profile = null;
    }
  }

  public ErrMsg getMsg() {
    return this.msg;
  }

  public Result setMsg(ErrMsg msg) {
    this.msg = msg;
    return this;
  }

  public void unsetMsg() {
    this.msg = null;
  }

  /** Returns true if field msg is set (has been assigned a value) and false otherwise */
  public boolean isSetMsg() {
    return this.msg != null;
  }

  public void setMsgIsSet(boolean value) {
    if (!value) {
      this.msg = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case STATUS:
      if (value == null) {
        unsetStatus();
      } else {
        setStatus((Integer)value);
      }
      break;

    case BASE:
      if (value == null) {
        unsetBase();
      } else {
        setBase((ByteBuffer)value);
      }
      break;

    case PROFILE:
      if (value == null) {
        unsetProfile();
      } else {
        setProfile((ByteBuffer)value);
      }
      break;

    case MSG:
      if (value == null) {
        unsetMsg();
      } else {
        setMsg((ErrMsg)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case STATUS:
      return Integer.valueOf(getStatus());

    case BASE:
      return getBase();

    case PROFILE:
      return getProfile();

    case MSG:
      return getMsg();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case STATUS:
      return isSetStatus();
    case BASE:
      return isSetBase();
    case PROFILE:
      return isSetProfile();
    case MSG:
      return isSetMsg();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof Result)
      return this.equals((Result)that);
    return false;
  }

  public boolean equals(Result that) {
    if (that == null)
      return false;

    boolean this_present_status = true;
    boolean that_present_status = true;
    if (this_present_status || that_present_status) {
      if (!(this_present_status && that_present_status))
        return false;
      if (this.status != that.status)
        return false;
    }

    boolean this_present_base = true && this.isSetBase();
    boolean that_present_base = true && that.isSetBase();
    if (this_present_base || that_present_base) {
      if (!(this_present_base && that_present_base))
        return false;
      if (!this.base.equals(that.base))
        return false;
    }

    boolean this_present_profile = true && this.isSetProfile();
    boolean that_present_profile = true && that.isSetProfile();
    if (this_present_profile || that_present_profile) {
      if (!(this_present_profile && that_present_profile))
        return false;
      if (!this.profile.equals(that.profile))
        return false;
    }

    boolean this_present_msg = true && this.isSetMsg();
    boolean that_present_msg = true && that.isSetMsg();
    if (this_present_msg || that_present_msg) {
      if (!(this_present_msg && that_present_msg))
        return false;
      if (!this.msg.equals(that.msg))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    List<Object> list = new ArrayList<Object>();

    boolean present_status = true;
    list.add(present_status);
    if (present_status)
      list.add(status);

    boolean present_base = true && (isSetBase());
    list.add(present_base);
    if (present_base)
      list.add(base);

    boolean present_profile = true && (isSetProfile());
    list.add(present_profile);
    if (present_profile)
      list.add(profile);

    boolean present_msg = true && (isSetMsg());
    list.add(present_msg);
    if (present_msg)
      list.add(msg);

    return list.hashCode();
  }

  @Override
  public int compareTo(Result other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetStatus()).compareTo(other.isSetStatus());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetStatus()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.status, other.status);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetBase()).compareTo(other.isSetBase());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetBase()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.base, other.base);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetProfile()).compareTo(other.isSetProfile());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetProfile()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.profile, other.profile);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetMsg()).compareTo(other.isSetMsg());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetMsg()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.msg, other.msg);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("Result(");
    boolean first = true;

    sb.append("status:");
    sb.append(this.status);
    first = false;
    if (!first) sb.append(", ");
    sb.append("base:");
    if (this.base == null) {
      sb.append("null");
    } else {
      org.apache.thrift.TBaseHelper.toString(this.base, sb);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("profile:");
    if (this.profile == null) {
      sb.append("null");
    } else {
      org.apache.thrift.TBaseHelper.toString(this.profile, sb);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("msg:");
    if (this.msg == null) {
      sb.append("null");
    } else {
      sb.append(this.msg);
    }
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    // check for sub-struct validity
    if (msg != null) {
      msg.validate();
    }
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
    try {
      // it doesn't seem like you should have to do this, but java serialization is wacky, and doesn't call the default constructor.
      __isset_bitfield = 0;
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class ResultStandardSchemeFactory implements SchemeFactory {
    public ResultStandardScheme getScheme() {
      return new ResultStandardScheme();
    }
  }

  private static class ResultStandardScheme extends StandardScheme<Result> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, Result struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // STATUS
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.status = iprot.readI32();
              struct.setStatusIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // BASE
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.base = iprot.readBinary();
              struct.setBaseIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // PROFILE
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.profile = iprot.readBinary();
              struct.setProfileIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 4: // MSG
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.msg = new ErrMsg();
              struct.msg.read(iprot);
              struct.setMsgIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();

      // check for required fields of primitive type, which can't be checked in the validate method
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, Result struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      oprot.writeFieldBegin(STATUS_FIELD_DESC);
      oprot.writeI32(struct.status);
      oprot.writeFieldEnd();
      if (struct.base != null) {
        oprot.writeFieldBegin(BASE_FIELD_DESC);
        oprot.writeBinary(struct.base);
        oprot.writeFieldEnd();
      }
      if (struct.profile != null) {
        oprot.writeFieldBegin(PROFILE_FIELD_DESC);
        oprot.writeBinary(struct.profile);
        oprot.writeFieldEnd();
      }
      if (struct.msg != null) {
        oprot.writeFieldBegin(MSG_FIELD_DESC);
        struct.msg.write(oprot);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class ResultTupleSchemeFactory implements SchemeFactory {
    public ResultTupleScheme getScheme() {
      return new ResultTupleScheme();
    }
  }

  private static class ResultTupleScheme extends TupleScheme<Result> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, Result struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetStatus()) {
        optionals.set(0);
      }
      if (struct.isSetBase()) {
        optionals.set(1);
      }
      if (struct.isSetProfile()) {
        optionals.set(2);
      }
      if (struct.isSetMsg()) {
        optionals.set(3);
      }
      oprot.writeBitSet(optionals, 4);
      if (struct.isSetStatus()) {
        oprot.writeI32(struct.status);
      }
      if (struct.isSetBase()) {
        oprot.writeBinary(struct.base);
      }
      if (struct.isSetProfile()) {
        oprot.writeBinary(struct.profile);
      }
      if (struct.isSetMsg()) {
        struct.msg.write(oprot);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, Result struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(4);
      if (incoming.get(0)) {
        struct.status = iprot.readI32();
        struct.setStatusIsSet(true);
      }
      if (incoming.get(1)) {
        struct.base = iprot.readBinary();
        struct.setBaseIsSet(true);
      }
      if (incoming.get(2)) {
        struct.profile = iprot.readBinary();
        struct.setProfileIsSet(true);
      }
      if (incoming.get(3)) {
        struct.msg = new ErrMsg();
        struct.msg.read(iprot);
        struct.setMsgIsSet(true);
      }
    }
  }

}
