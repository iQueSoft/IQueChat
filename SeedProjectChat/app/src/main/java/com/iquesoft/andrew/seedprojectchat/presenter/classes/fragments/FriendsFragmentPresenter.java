package com.iquesoft.andrew.seedprojectchat.presenter.classes.fragments;

import com.iquesoft.andrew.seedprojectchat.presenter.interfaces.fragments.IFriendsFragmentPresenter;
import com.iquesoft.andrew.seedprojectchat.view.interfaces.fragments.IFriendsFragment;

import javax.inject.Inject;

/**
 * Created by Andrew on 8/23/2016.
 */

public class FriendsFragmentPresenter implements IFriendsFragmentPresenter {

    private IFriendsFragment view;

    @Inject
    public FriendsFragmentPresenter(){}

    @Override
    public void init(IFriendsFragment view) {
        this.view = view;
    }

}
