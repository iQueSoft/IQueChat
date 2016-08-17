package com.iquesoft.andrew.seedprojectchat.common;


import android.support.v4.app.Fragment;

import com.iquesoft.andrew.seedprojectchat.di.IHasComponent;

/**
 * Created by Andrew on 16.08.2016.
 */

public abstract class BaseFragment extends Fragment {
    @SuppressWarnings("unchecked")
    protected <T> T getComponent(Class<T> componentType) {
        return componentType.cast(((IHasComponent<T>)getActivity()).getComponent());
    }
}
