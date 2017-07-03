package com.linda.aidl_example;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, SubjectService.class);
        bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            ISubjectManager mSubjectManger = ISubjectManager.Stub.asInterface(iBinder);
            try {
                List<Subject> list = mSubjectManger.getSubjectList();
                Log.e(TAG, "list:  " + list.toString());

                Subject subject = new Subject(3, "java");
                mSubjectManger.addSubject(subject);

                List<Subject> newList = mSubjectManger.getSubjectList();
                Log.e(TAG, "newList:  " + newList.toString());

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
    }
}
