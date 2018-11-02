package com.enjoy02.enjoyfragmentdemo02.fragment;

import android.support.v4.app.Fragment;
import android.util.Log;

public class FragmentUtils {

    public static void getFragmentInfo(Fragment fragment) {
        if (fragment == null) {
            Log.i("Zero", "fragment is null");
            return;
        }
        Log.i("Zero", "getId: " + fragment.getId()
                + " isAdded: " + fragment.isAdded()
                + " isDetached: " + fragment.isDetached()
                + " isHidden: " + fragment.isHidden()
                + " isInLayout: " + fragment.isInLayout()
                + " isRemoving: " + fragment.isRemoving()
                + " isResumed: " + fragment.isResumed()
                + " isVisible: " + fragment.isVisible()
                + " getTag: " + fragment.getTag()
        );
    }
}
