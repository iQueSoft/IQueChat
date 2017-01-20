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

import net.iquesoft.android.seedprojectchat.adapters.InvateToFriendFragmentAdapter;
import net.iquesoft.android.seedprojectchat.common.BaseFragment;
import net.iquesoft.android.seedprojectchat.di.components.IMainActivityComponent;
import net.iquesoft.android.seedprojectchat.model.Friends;
import net.iquesoft.android.seedprojectchat.presenter.classes.fragments.InviteToFriendFragmentPresenter;
import net.iquesoft.android.seedprojectchat.view.interfaces.fragments.IInviteToFriendFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.ScaleInAnimator;

public class InviteToFriendFragment extends BaseFragment implements IInviteToFriendFragment {

    @InjectPresenter
    InviteToFriendFragmentPresenter presenter;

    @BindView(net.iquesoft.android.seedprojectchat.R.id.recycler_invite)
    RecyclerView recyclerInvite;

    private View rootView;
    private InvateToFriendFragmentAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(net.iquesoft.android.seedprojectchat.R.layout.fragment_invite_to_friends_fragment, container, false);
            ButterKnife.bind(this, rootView);
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.getComponent(IMainActivityComponent.class).inject(this);
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    public void setUserAdapter(List<Friends> users) {
        adapter = new InvateToFriendFragmentAdapter(users, getActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerInvite.setLayoutManager(linearLayoutManager);
        recyclerInvite.setItemAnimator(new ScaleInAnimator(new OvershootInterpolator(1f)));
        ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(adapter);
        scaleInAnimationAdapter.setFirstOnly(true);
        scaleInAnimationAdapter.setDuration(500);
        recyclerInvite.setAdapter(scaleInAnimationAdapter);
    }
}
