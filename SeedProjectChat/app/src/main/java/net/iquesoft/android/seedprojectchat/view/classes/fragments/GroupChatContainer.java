package net.iquesoft.android.seedprojectchat.view.classes.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;

import com.arellomobile.mvp.presenter.InjectPresenter;

import net.iquesoft.android.seedprojectchat.adapters.GroupChatContainerAdapter;
import net.iquesoft.android.seedprojectchat.common.BaseFragment;
import net.iquesoft.android.seedprojectchat.di.components.IMainActivityComponent;
import net.iquesoft.android.seedprojectchat.model.GroupChat;
import net.iquesoft.android.seedprojectchat.presenter.classes.fragments.GroupChatContainerPresenter;
import net.iquesoft.android.seedprojectchat.util.RecyclerItemClickListener;
import net.iquesoft.android.seedprojectchat.view.classes.activity.MainActivity;
import net.iquesoft.android.seedprojectchat.view.interfaces.fragments.IGroupChatContainer;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.ScaleInAnimator;
import rx.Observable;

public class GroupChatContainer extends BaseFragment implements IGroupChatContainer {

    @InjectPresenter
    GroupChatContainerPresenter presenter;

    @Inject
    GroupChatFragment groupChatFragment;

    @BindView(net.iquesoft.android.seedprojectchat.R.id.recycler_group_chat)
    RecyclerView recyclerGroupChat;

    private MainActivity mainActivity;
    private GroupChatContainerAdapter groupChatContainerAdapter;
    private View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(net.iquesoft.android.seedprojectchat.R.layout.fragment_group_chat_container, container, false);
            ButterKnife.bind(this, rootView);
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.getComponent(IMainActivityComponent.class).inject(this);
        mainActivity = (MainActivity) getActivity();
    }

    @OnClick(net.iquesoft.android.seedprojectchat.R.id.create_new_group_chat)
    public void setFabCreateGroupChat(){
        LayoutInflater inflater = getActivity().getLayoutInflater();
        presenter.createDialogGroupChat(getActivity(), inflater);
    }

    public void addNewChatToRecycler(GroupChat groupChat){
        groupChatContainerAdapter.insert(groupChat, 0);
        recyclerGroupChat.smoothScrollToPosition(0);
    }


    public void setGroupChatContainerAdapter(List<GroupChat> groupChatList) {
        groupChatContainerAdapter = new GroupChatContainerAdapter(groupChatList, getActivity(), getActivity().getSupportFragmentManager());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerGroupChat.setLayoutManager(linearLayoutManager);
        recyclerGroupChat.setItemAnimator(new ScaleInAnimator(new OvershootInterpolator(1f)));
        ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(groupChatContainerAdapter);
        scaleInAnimationAdapter.setFirstOnly(true);
        scaleInAnimationAdapter.setDuration(500);
        recyclerGroupChat.setAdapter(scaleInAnimationAdapter);
        recyclerGroupChat.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), (view, position) ->{
            if (groupChatList.size() != 0){
                Observable.just(groupChatList.get(position)).subscribe(response -> groupChatFragment.setCurentGroupChat(response));
                mainActivity.replaceFragment(groupChatFragment);
            }
        }
        ));
    }

}
