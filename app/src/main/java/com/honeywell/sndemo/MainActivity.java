package com.honeywell.sndemo;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.honeywell.sndemo.refection.SystemProperties;
import com.honeywell.sndemo.widget.WaterMarkText;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "LYT-PDB";

    Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        ((Button)findViewById(R.id.button)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d(TAG, "onClick");
//                setMdmBootloaderEnabledLocked();
//            }
//        });
    }

    private static final boolean mShowWaterMark = true;
    private boolean mWaterMarkAdded = false;

    @Override
    protected void onRestart() {
        super.onRestart();
        if (mShowWaterMark && !mWaterMarkAdded) {
            WaterMarkText.getInstance().setText("Testing").show(this);
            mWaterMarkAdded = true;
        }

    }
    public static final String PERSISTENT_DATA_BLOCK_SERVICE = "persistent_data_block";

    @SuppressLint("WrongConstant")
    public int setMdmBootloaderEnabledLocked() {
        Log.d(TAG, "setMdmBootloaderEnabledLocked");
        int mode = 0;
        try{
            Class <?> ServiceManager = Class.forName("android.os.ServiceManager");
            Method getService = ServiceManager.getMethod("getService", java.lang.String.class);
            Object oRemoteService = getService.invoke(null, "persistent_data_block");
            if (oRemoteService == null){
                Log.d(TAG, "oRemoteService == null ");
                return mode;
            }

            Class <?> cStub = Class.forName("android.service.persistentdata.IPersistentDataBlockService$Stub");
            Method asInterface = cStub.getMethod("asInterface", android.os.IBinder.class);
            Object mPersistentDataBlockService = asInterface.invoke(null, oRemoteService);

            if (mPersistentDataBlockService == null){
                Log.d(TAG, "mPersistentDataBlockService == null ");
            } else {
                Method setMdmBootloaderEnabled = mPersistentDataBlockService.getClass().getMethod("setMdmBootloaderEnabled",int.class);
                setMdmBootloaderEnabled.invoke(mPersistentDataBlockService,2);
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
//            Log.d(TAG, "getMdmBootloaderEnabled: mode = " + mode);
            return mode;
        }
    };

    public String getDeviceSN() {
        String serialNumber = SystemProperties.get("ro.vendor.hon.extserial.num");
        Log.d("Demo", "serialNumber =  " + serialNumber);
        return serialNumber;
    }
}