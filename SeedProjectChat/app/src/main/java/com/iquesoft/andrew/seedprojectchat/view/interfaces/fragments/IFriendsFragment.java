package com.iquesoft.andrew.seedprojectchat.view.interfaces.fragments;

import com.arellomobile.mvp.MvpView;
import com.iquesoft.andrew.seedprojectchat.model.Friends;

import java.util.List;

/**
 * Created by Andrew on 8/23/2016.
 */

public interface IFriendsFragment extends MvpView {
    void setUserAdapter(List<Friends> users);
}
