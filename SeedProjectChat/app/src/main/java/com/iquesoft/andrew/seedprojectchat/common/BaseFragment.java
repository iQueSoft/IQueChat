package com.iquesoft.andrew.seedprojectchat.common;


import com.arellomobile.mvp.MvpAppCompatFragment;
import com.iquesoft.andrew.seedprojectchat.di.IHasComponent;

/**
 * Created by Andrew on 16.08.2016.
 */

public abstract class BaseFragment extends MvpAppCompatFragment {
    @SuppressWarnings("unchecked")
    protected <T> T getComponent(Class<T> componentType) {
        return componentType.cast(((IHasComponent<T>)getActivity()).getComponent());
    }
}
