package com.enjoy02.enjoyfragmentdemo02.arch;

import android.content.Context;

/**
 * fragment观察者
 */
public abstract class FragmentObserver implements Observer {
    @Override
    public void update(Observable observable) {
        FragmentObservable fragmentObservable = (FragmentObservable) observable;
        if (fragmentObservable.fragmentLifeState == FragmentObservable.FragmentLifeState.ATTACH) {
            this.onAttach(fragmentObservable.context);
        } else if (fragmentObservable.fragmentLifeState == FragmentObservable.FragmentLifeState.DETACH) {
            this.onDetach();
        }
    }

    public abstract void onAttach(Context context);

    public abstract void onDetach();
}
