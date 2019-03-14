// IClientInterface.aidl
package com.zero.aidldemoservice;

import com.zero.aidldemoservice.IRemoteCallback;
import com.zero.aidldemoservice.Person;


// Declare any non-default types here with import statements

interface IClientInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);

    void registerCallback(IRemoteCallback cb);
    void unregisterCallback(IRemoteCallback cb);

    boolean client2Service(Person person);
}
