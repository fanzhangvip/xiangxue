package com.enjoy02.enjoyfragmentdemo02;

import android.support.v4.app.FragmentManager;

import java.util.Collections;

public class FragmentTransactionBugFixHack {
    public static void reorderIndices(FragmentManager fragmentManager) {
//        if (!(fragmentManager instanceof FragmentManagerImpl))
//            return;
//        FragmentManagerImpl fragmentManagerImpl = (FragmentManagerImpl) fragmentManager;
//        if (fragmentManagerImpl.mAvailIndices != null && fragmentManagerImpl.mAvailIndices.size() > 1) {
//            Collections.sort(fragmentManagerImpl.mAvailIndices, Collections.reverseOrder());
//        }
    }
}
