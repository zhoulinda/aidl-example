// ISubjectManager.aidl
package com.linda.aidl_example;

import com.linda.aidl_example.Subject;
// Declare any non-default types here with import statements

interface ISubjectManager {
   List<Subject> getSubjectList();
   void addSubject(in Subject subject);
}
