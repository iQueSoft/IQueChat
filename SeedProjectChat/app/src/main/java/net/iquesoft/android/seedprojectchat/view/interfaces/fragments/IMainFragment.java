package net.iquesoft.android.seedprojectchat.view.interfaces.fragments;

import com.arellomobile.mvp.MvpView;
import net.iquesoft.android.seedprojectchat.model.BaseChatModel;

import java.util.List;

public interface IMainFragment extends MvpView {
    void setRecyclerMain(List<BaseChatModel> baseChatModelList);
    void setProgressBarVisible();
    void setProgressBarGone();
}
