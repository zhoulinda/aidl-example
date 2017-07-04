package com.linda.aidl_example;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int MESSAGE_NEW_SUBJECT_ADDED = 1;
    private ISubjectManager mRemoteSubjectManger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, SubjectService.class);
        bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_NEW_SUBJECT_ADDED:
                    Log.e(TAG, "added a new subject" + msg.obj);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            ISubjectManager mSubjectManger = ISubjectManager.Stub.asInterface(iBinder);
            try {
                mRemoteSubjectManger = mSubjectManger;

                List<Subject> list = mSubjectManger.getSubjectList();
                Log.e(TAG, "list:  " + list.toString());

                Subject subject = new Subject(3, "java");
                mSubjectManger.addSubject(subject);

                List<Subject> newList = mSubjectManger.getSubjectList();
                Log.e(TAG, "newList:  " + newList.toString());

                mSubjectManger.registerListener(listener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mRemoteSubjectManger = null;
        }
    };

    private OnNewSubjectAddedListener listener = new OnNewSubjectAddedListener.Stub() {
        @Override
        public void onNewSubjectAdded(Subject newSubject) throws RemoteException {
            mHandler.obtainMessage(MESSAGE_NEW_SUBJECT_ADDED, newSubject).sendToTarget();
        }
    };


    @Override
    protected void onDestroy() {
        if (mRemoteSubjectManger != null && mRemoteSubjectManger.asBinder().isBinderAlive()){
            try {
                mRemoteSubjectManger.unRegisterListener(listener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        unbindService(conn);
        super.onDestroy();
    }
}
