package com.linda.aidl_example;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by linda on 2017/7/3.
 */

public class Subject implements Parcelable {

    private int subjectId;
    private String subjectName;

    public Subject(int subjectId, String subjectName) {
        this.subjectId = subjectId;
        this.subjectName = subjectName;
    }

    public Subject(Parcel parcel) {
        subjectId = parcel.readInt();
        subjectName = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(subjectId);
        parcel.writeString(subjectName);
    }

    public static final Parcelable.Creator<Subject> CREATOR = new Parcelable.Creator<Subject>() {
        @Override
        public Subject createFromParcel(Parcel parcel) {
            return new Subject(parcel);
        }

        @Override
        public Subject[] newArray(int i) {
            return new Subject[i];
        }
    };


    @Override
    public String toString() {
        return "Subject{" +
                "subjectId=" + subjectId +
                ", subjectName='" + subjectName + '\'' +
                '}';
    }
}
