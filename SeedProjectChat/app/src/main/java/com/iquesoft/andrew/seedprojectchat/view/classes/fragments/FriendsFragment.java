package com.iquesoft.andrew.seedprojectchat.view.classes.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;

import com.iquesoft.andrew.seedprojectchat.R;
import com.iquesoft.andrew.seedprojectchat.adapters.UserListAdapter;
import com.iquesoft.andrew.seedprojectchat.common.BaseFragment;
import com.iquesoft.andrew.seedprojectchat.di.components.IMainActivityComponent;
import com.iquesoft.andrew.seedprojectchat.model.Friends;
import com.iquesoft.andrew.seedprojectchat.presenter.classes.fragments.FriendsFragmentPresenter;
import com.iquesoft.andrew.seedprojectchat.view.classes.activity.MainActivity;
import com.iquesoft.andrew.seedprojectchat.view.interfaces.fragments.IFriendsFragment;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.ScaleInAnimator;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

/**
 * Created by Andrew on 8/23/2016.
 */

public class FriendsFragment extends BaseFragment implements IFriendsFragment {

    @Inject
    FriendsFragmentPresenter presenter;

    @BindView(R.id.recycler_friends)
    RecyclerView recyclerFriends;

    MainActivity mainActivity;

    Subscription userSubscription;
    UserListAdapter adapter;

    private View rootView;

    @OnClick(R.id.fab_add_friend)
    public void addFriend(View view) {
        mainActivity.setFindFriendFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_friends, container, false);
            ButterKnife.bind(this, rootView);
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        this.getComponent(IMainActivityComponent.class).inject(this);
    }


    @Override
    public void onResume() {
        super.onResume();
        presenter.init(this);
        userSubscription = presenter.getCurentFriendList().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(chatUsers -> {
            Log.i("mySubscript", chatUsers.toString());
            setUserAdapter(chatUsers);
        });
    }

    public BehaviorSubject<List<Friends>> getCurentFriendList(){
        return presenter.getCurentFriendList();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public Context getActivityContext() {
        return getActivity();
    }

    private void setUserAdapter(List<Friends> users) {
        adapter = new UserListAdapter(users, getActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerFriends.setLayoutManager(linearLayoutManager);
        recyclerFriends.setItemAnimator(new ScaleInAnimator(new OvershootInterpolator(1f)));
        ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(adapter);
        scaleInAnimationAdapter.setFirstOnly(true);
        scaleInAnimationAdapter.setDuration(500);
        setSwipeDeleteItemListener(recyclerFriends);
        recyclerFriends.setAdapter(scaleInAnimationAdapter);
    }

    private void setSwipeDeleteItemListener(RecyclerView recyclerView){
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                adapter.remove(viewHolder.getAdapterPosition());
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
}
