package com.honeywell.sndemo.refection;

import android.annotation.SuppressLint;
import android.os.RemoteException;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RefectionUtils {

    private static final String TAG = RefectionUtils.class.getSimpleName();

    @SuppressLint("WrongConstant")
    public void setMdmBootloaderEnabledLocked() {
        Log.d(TAG, "setMdmBootloaderEnabledLocked");
        int mode = 0;
        try{
            Class <?> ServiceManager = Class.forName("android.os.ServiceManager");
            Method getService = ServiceManager.getMethod("getService", java.lang.String.class);
            Object oRemoteService = getService.invoke(null, "persistent_data_block");
            if (oRemoteService == null){
                Log.d(TAG, "oRemoteService == null ");
                return ;
            }

            Class <?> cStub = Class.forName("android.service.persistentdata.IPersistentDataBlockService$Stub");
            Method asInterface = cStub.getMethod("asInterface", android.os.IBinder.class);
            Object mPersistentDataBlockService = asInterface.invoke(null, oRemoteService);

            if (mPersistentDataBlockService == null){
                Log.d(TAG, "mPersistentDataBlockService == null ");
            } else {
                Method setMdmBootloaderEnabled = mPersistentDataBlockService.getClass().getMethod("setMdmBootloaderEnabled",Boolean.class);
                setMdmBootloaderEnabled.invoke(mPersistentDataBlockService,true);
            }

        } catch (ClassNotFoundException e) {
            Log.e(TAG, "getMdmBootloaderEnabledLocked - ClassNotFoundException!");
        } catch (InvocationTargetException e) {
            Log.e(TAG, "getMdmBootloaderEnabledLocked - InvocationTargetException!");
        } catch (NoSuchMethodException e) {
            Log.e(TAG, "getMdmBootloaderEnabledLocked - NoSuchMethodException!");
            throw new RemoteException();
        } catch (IllegalAccessException e) {
            Log.e(TAG, "getMdmBootloaderEnabledLocked - IllegalAccessException!");
        }  finally {
            return;
        }
    };

    public int getMdmBootloaderEnabledLocked() {
        Log.d(TAG, "getMdmBootloaderEnabledLocked");
        int mode = 0;
        try{
            Class <?> ServiceManager = Class.forName("android.os.ServiceManager");
            Method getService = ServiceManager.getMethod("getService", java.lang.String.class);
            Object oRemoteService = getService.invoke(null, "persistent_data_block");

            Class <?> cStub = Class.forName("android.service.persistentdata.IPersistentDataBlockService$Stub");
            Method asInterface = cStub.getMethod("asInterface", android.os.IBinder.class);
            Object mPersistentDataBlockService = asInterface.invoke(null, oRemoteService);
            Method getMdmBootloaderEnabledLocked = mPersistentDataBlockService.getClass().getMethod("getMdmBootloaderEnabledLocked");
            mode = (int) getMdmBootloaderEnabledLocked.invoke(mPersistentDataBlockService);
        } catch (ClassNotFoundException e) {
            Log.e(TAG, "getMdmBootloaderEnabledLocked - ClassNotFoundException!");
        } catch (InvocationTargetException e) {
            Log.e(TAG, "getMdmBootloaderEnabledLocked - InvocationTargetException!");
        } catch (NoSuchMethodException e) {
            Log.e(TAG, "getMdmBootloaderEnabledLocked - NoSuchMethodException!");
            throw new RemoteException();
        } catch (IllegalAccessException e) {
            Log.e(TAG, "getMdmBootloaderEnabledLocked - IllegalAccessException!");
        }  finally {
            Log.d(TAG, "getMdmBootloaderEnabled: mode = " + mode);
            return mode;
        }
    };
}
