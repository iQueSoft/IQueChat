package com.iquesoft.andrew.seedprojectchat.view.interfaces.fragments;

import com.arellomobile.mvp.MvpView;
import com.iquesoft.andrew.seedprojectchat.model.Friends;

import java.util.List;

/**
 * Created by andru on 9/5/2016.
 */

public interface IMyInvateFragment extends MvpView {
    void setUserAdapter(List<Friends> users);
}
