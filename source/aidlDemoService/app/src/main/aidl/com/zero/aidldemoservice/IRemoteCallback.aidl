// IRemoteCallback.aidl
package com.zero.aidldemoservice;
import com.zero.aidldemoservice.Person;
// Declare any non-default types here with import statements

interface IRemoteCallback {
    void actionPerformed(int actionId,Person person);
}
