/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /Users/yusuke/AndroidStudioProjects/android_sample/Chapter04/app/src/main/aidl/jp/co/se/android/recipe/chapter04/ICh0410Service.aidl
 */
package jp.co.se.android.recipe.chapter04;
public interface ICh0410Service extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements jp.co.se.android.recipe.chapter04.ICh0410Service
{
private static final java.lang.String DESCRIPTOR = "jp.co.se.android.recipe.chapter04.ICh0410Service";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an jp.co.se.android.recipe.chapter04.ICh0410Service interface,
 * generating a proxy if needed.
 */
public static jp.co.se.android.recipe.chapter04.ICh0410Service asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof jp.co.se.android.recipe.chapter04.ICh0410Service))) {
return ((jp.co.se.android.recipe.chapter04.ICh0410Service)iin);
}
return new jp.co.se.android.recipe.chapter04.ICh0410Service.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_addString:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
int _result = this.addString(_arg0);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_getString:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String[] _result = this.getString();
reply.writeNoException();
reply.writeStringArray(_result);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements jp.co.se.android.recipe.chapter04.ICh0410Service
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public int addString(java.lang.String value) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(value);
mRemote.transact(Stub.TRANSACTION_addString, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public java.lang.String[] getString() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String[] _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getString, _data, _reply, 0);
_reply.readException();
_result = _reply.createStringArray();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_addString = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_getString = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
}
public int addString(java.lang.String value) throws android.os.RemoteException;
public java.lang.String[] getString() throws android.os.RemoteException;
}
