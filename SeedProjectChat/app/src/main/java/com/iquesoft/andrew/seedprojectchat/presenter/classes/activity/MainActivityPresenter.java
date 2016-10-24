package com.iquesoft.andrew.seedprojectchat.presenter.classes.activity;

import com.iquesoft.andrew.seedprojectchat.presenter.interfaces.activity.IMainActivityPresenter;
import com.iquesoft.andrew.seedprojectchat.view.interfaces.activity.IMainActivity;

import javax.inject.Inject;

/**
 * Created by Andrew on 18.08.2016.
 */

public class MainActivityPresenter implements IMainActivityPresenter {

    private IMainActivity view;

    @Inject
    public MainActivityPresenter(IMainActivity view){
        this.view = view;
    }


}
