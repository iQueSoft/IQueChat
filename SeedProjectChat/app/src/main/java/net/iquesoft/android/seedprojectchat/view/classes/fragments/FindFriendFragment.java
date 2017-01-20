package net.iquesoft.android.seedprojectchat.view.classes.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.backendless.BackendlessUser;

import net.iquesoft.android.seedprojectchat.adapters.FindFriendAdapter;
import net.iquesoft.android.seedprojectchat.common.BaseFragment;
import net.iquesoft.android.seedprojectchat.di.components.IMainActivityComponent;
import net.iquesoft.android.seedprojectchat.presenter.classes.fragments.FindFriendFragmentPresenter;
import net.iquesoft.android.seedprojectchat.view.interfaces.fragments.IFindFriendFragment;

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

public class FindFriendFragment extends BaseFragment implements IFindFriendFragment {

    @InjectPresenter
    FindFriendFragmentPresenter presenter;

    @Inject
    FriendsFragment friendsFragment;

    private View rootView;

    @BindView(net.iquesoft.android.seedprojectchat.R.id.edit_text_username)
    EditText username;
    @BindView(net.iquesoft.android.seedprojectchat.R.id.recycler_view_find_user)
    RecyclerView recyclerViewFindUsers;

    private Subscription getFriendsSubscription;

    @OnClick(net.iquesoft.android.seedprojectchat.R.id.btn_find_users)
    public void findClick() {
        getFriendsSubscription = friendsFragment.getCurentFriendList().subscribe(response ->{
            presenter.getBackendlessUsers(username.getText().toString(), response).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(this::setUserAdapter);
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(net.iquesoft.android.seedprojectchat.R.layout.fragment_find_friend, container, false);
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
    public void onResume() {
        super.onResume();
    }

    private void setUserAdapter(List<BackendlessUser> users) {
        FindFriendAdapter adapter = new FindFriendAdapter(users, getActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewFindUsers.setLayoutManager(linearLayoutManager);
        recyclerViewFindUsers.setItemAnimator(new ScaleInAnimator(new OvershootInterpolator(1f)));
        ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(adapter);
        scaleInAnimationAdapter.setFirstOnly(true);
        scaleInAnimationAdapter.setDuration(500);
        recyclerViewFindUsers.setAdapter(scaleInAnimationAdapter);
    }
}
