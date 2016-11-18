package com.iquesoft.andrew.seedprojectchat.view.interfaces.fragments;

import com.arellomobile.mvp.MvpView;
import com.iquesoft.andrew.seedprojectchat.model.BaseChatModel;

import java.util.List;

/**
 * Created by andru on 08.11.2016.
 */

public interface IMainFragment extends MvpView {
    public void setRecyclerMain(List<BaseChatModel> baseChatModelList);
}
