package net.iquesoft.android.seedprojectchat.view.interfaces.fragments;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.backendless.BackendlessUser;

public interface ILoginFragment extends MvpView {
    @StateStrategyType(SkipStrategy.class)
    void showProgress(final boolean show);
    @StateStrategyType(SkipStrategy.class)
    void setCurentUser(BackendlessUser loggedInUser);
}
