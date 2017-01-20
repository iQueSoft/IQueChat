package net.iquesoft.android.seedprojectchat.common;


import com.arellomobile.mvp.MvpAppCompatFragment;
import net.iquesoft.android.seedprojectchat.di.IHasComponent;

public abstract class BaseFragment extends MvpAppCompatFragment {
    @SuppressWarnings("unchecked")
    protected <T> T getComponent(Class<T> componentType) {
        return componentType.cast(((IHasComponent<T>)getActivity()).getComponent());
    }
}
