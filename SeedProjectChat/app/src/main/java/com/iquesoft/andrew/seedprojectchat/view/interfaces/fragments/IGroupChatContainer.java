package com.iquesoft.andrew.seedprojectchat.view.interfaces.fragments;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.iquesoft.andrew.seedprojectchat.model.GroupChat;

import java.util.List;

/**
 * Created by andru on 9/17/2016.
 */

public interface IGroupChatContainer extends MvpView {

    @StateStrategyType(SkipStrategy.class)
    void setGroupChatContainerAdapter(List<GroupChat> groupChatList);

    @StateStrategyType(SkipStrategy.class)
    void addNewChatToRecycler(GroupChat groupChat);

}
