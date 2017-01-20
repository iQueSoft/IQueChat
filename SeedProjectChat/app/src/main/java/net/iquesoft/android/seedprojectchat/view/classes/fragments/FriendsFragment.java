package net.iquesoft.android.seedprojectchat.view.classes.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;

import com.arellomobile.mvp.presenter.InjectPresenter;

import net.iquesoft.android.seedprojectchat.adapters.UserListAdapter;
import net.iquesoft.android.seedprojectchat.common.BaseFragment;
import net.iquesoft.android.seedprojectchat.common.DefaultBackendlessCallback;
import net.iquesoft.android.seedprojectchat.di.components.IMainActivityComponent;
import net.iquesoft.android.seedprojectchat.model.Friends;
import net.iquesoft.android.seedprojectchat.presenter.classes.fragments.FriendsFragmentPresenter;
import net.iquesoft.android.seedprojectchat.util.RecyclerItemClickListener;
import net.iquesoft.android.seedprojectchat.view.classes.activity.MainActivity;
import net.iquesoft.android.seedprojectchat.view.interfaces.fragments.IFriendsFragment;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.huannguyen.swipetodeleterv.ItemRemovalListener;
import io.huannguyen.swipetodeleterv.STDRecyclerView;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.ScaleInAnimator;
import rx.subjects.BehaviorSubject;

public class FriendsFragment extends BaseFragment implements IFriendsFragment {

    @InjectPresenter
    FriendsFragmentPresenter presenter;

    @Inject
    ChatWithFriendFragment chatWithFriendFragment;

    @BindView(net.iquesoft.android.seedprojectchat.R.id.recycler_view)
    STDRecyclerView stdRecyclerView;

    @BindView(net.iquesoft.android.seedprojectchat.R.id.fragment_friends_container)
    FrameLayout frameLayout;

    private MainActivity mainActivity;

    private View rootView;

    @OnClick(net.iquesoft.android.seedprojectchat.R.id.fab_add_friend)
    public void addFriend() {
        mainActivity.setFindFriendFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(net.iquesoft.android.seedprojectchat.R.layout.fragment_friends, container, false);
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


    public BehaviorSubject<List<Friends>> getCurentFriendList(){
        return presenter.getCurentFriendList();
    }

    public void setUserAdapter(List<Friends> users) {
        UserListAdapter adapter = new UserListAdapter(users, getActivity());
        adapter.setItemRemovalListener(new ItemRemovalListener() {
            @Override
            public void onItemTemporarilyRemoved(int position) {

            }

            @Override
            public void onItemPermanentlyRemoved(Object item) {
                Friends friends = (Friends) item;
                friends.removeAsync(new DefaultBackendlessCallback<>());
            }

            @Override
            public void onItemAddedBack(int position) {

            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        stdRecyclerView.setLayoutManager(linearLayoutManager);
        stdRecyclerView.setItemAnimator(new ScaleInAnimator(new OvershootInterpolator(1f)));
        ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(adapter);
        scaleInAnimationAdapter.setFirstOnly(true);
        scaleInAnimationAdapter.setDuration(500);
        stdRecyclerView.setupSwipeToDelete(adapter, ItemTouchHelper.RIGHT);
        stdRecyclerView.setAdapter(scaleInAnimationAdapter);
        stdRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), (view, position) ->{
            chatWithFriendFragment.setFriend(users.get(position));
            mainActivity.replaceFragment(chatWithFriendFragment);
        }));

    }
}
