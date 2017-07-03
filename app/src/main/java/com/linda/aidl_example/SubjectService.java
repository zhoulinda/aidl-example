package com.linda.aidl_example;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by linda on 2017/7/3.
 */

public class SubjectService extends Service {

    private CopyOnWriteArrayList<Subject> mSubjects = new CopyOnWriteArrayList<>();


    @Override
    public void onCreate() {
        super.onCreate();
        Subject subject1 = new Subject(1, "android");
        Subject subject2 = new Subject(2, "ios");
        mSubjects.add(subject1);
        mSubjects.add(subject2);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private ISubjectManager.Stub mBinder = new ISubjectManager.Stub() {
        @Override
        public List<Subject> getSubjectList() throws RemoteException {
            return mSubjects;
        }

        @Override
        public void addSubject(Subject subject) throws RemoteException {
            mSubjects.add(subject);
        }
    };


}
