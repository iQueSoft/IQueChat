package net.iquesoft.android.seedprojectchat.view.interfaces.fragments;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import net.iquesoft.android.seedprojectchat.model.GroupChat;

import java.util.List;

public interface IGroupChatContainer extends MvpView {

    @StateStrategyType(SkipStrategy.class)
    void setGroupChatContainerAdapter(List<GroupChat> groupChatList);

    @StateStrategyType(SkipStrategy.class)
    void addNewChatToRecycler(GroupChat groupChat);

}
