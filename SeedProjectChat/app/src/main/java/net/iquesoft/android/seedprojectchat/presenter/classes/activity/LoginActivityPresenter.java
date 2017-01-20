package net.iquesoft.android.seedprojectchat.presenter.classes.activity;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import net.iquesoft.android.seedprojectchat.presenter.interfaces.activity.ILoginActivityPresenter;
import net.iquesoft.android.seedprojectchat.view.interfaces.activity.ILoginActivity;

@InjectViewState
public class LoginActivityPresenter extends MvpPresenter<ILoginActivity> implements ILoginActivityPresenter {

    @Override
    public void onBackPressed() {
        getViewState().popFragmentFromStack();
    }
}
