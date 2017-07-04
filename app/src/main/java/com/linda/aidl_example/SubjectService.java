package com.linda.aidl_example;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by linda on 2017/7/3.
 */

public class SubjectService extends Service {

    private static final String TAG = "SubjectService";

    private CopyOnWriteArrayList<Subject> mSubjects = new CopyOnWriteArrayList<>();

    private AtomicBoolean mIsServiceDestoryed = new AtomicBoolean(false);

//    private CopyOnWriteArrayList<OnNewSubjectAddedListener> mListeners = new CopyOnWriteArrayList<>();

    private RemoteCallbackList mListenerList = new RemoteCallbackList();

    @Override
    public void onCreate() {
        super.onCreate();
        Subject subject1 = new Subject(1, "android");
        Subject subject2 = new Subject(2, "ios");
        mSubjects.add(subject1);
        mSubjects.add(subject2);

        new Thread(new ServiceWork()).start();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mIsServiceDestoryed.set(true);
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

        @Override
        public void registerListener(OnNewSubjectAddedListener listener) throws RemoteException {
            mListenerList.register(listener);

            Log.e(TAG, "registerListener:   " + "size :   " + mListenerList.beginBroadcast());
            mListenerList.finishBroadcast();
        }


        @Override
        public void unRegisterListener(OnNewSubjectAddedListener listener) throws RemoteException {
            mListenerList.unregister(listener);
            Log.e(TAG, "unRegisterListener:   " + "size :   " + mListenerList.beginBroadcast());
            mListenerList.finishBroadcast();
        }
    };

    public class ServiceWork implements Runnable {
        @Override
        public void run() {
            while (!mIsServiceDestoryed.get()) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                int subjectId = mSubjects.size() + 1;
                Subject subject = new Subject(mSubjects.size() + 1, "#newBook" + subjectId);
                try {
                    onNewSubjectAdded(subject);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void onNewSubjectAdded(Subject subject) throws RemoteException {
        mSubjects.add(subject);
        int N = mListenerList.beginBroadcast();
        for (int i = 0; i < N; i++) {
            OnNewSubjectAddedListener listener = (OnNewSubjectAddedListener) mListenerList.getBroadcastItem(i);
            if (listener != null) {
                listener.onNewSubjectAdded(subject);
            }
        }
        mListenerList.finishBroadcast();
    }
}
