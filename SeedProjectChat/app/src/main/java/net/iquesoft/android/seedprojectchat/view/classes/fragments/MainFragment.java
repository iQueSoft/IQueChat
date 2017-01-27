package net.iquesoft.android.seedprojectchat.view.classes.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ProgressBar;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;

import net.iquesoft.android.seedprojectchat.R;
import net.iquesoft.android.seedprojectchat.adapters.MainFragmentAdapter;
import net.iquesoft.android.seedprojectchat.common.BaseFragment;
import net.iquesoft.android.seedprojectchat.model.BaseChatModel;
import net.iquesoft.android.seedprojectchat.presenter.classes.fragments.MainFragmentPresenter;
import net.iquesoft.android.seedprojectchat.view.classes.activity.MainActivity;
import net.iquesoft.android.seedprojectchat.view.interfaces.fragments.IMainFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.ScaleInAnimator;

public class MainFragment extends BaseFragment implements IMainFragment {

    @InjectPresenter
    MainFragmentPresenter presenter;

    @BindView(net.iquesoft.android.seedprojectchat.R.id.main_screen_recycler_view)
    RecyclerView recyclerMain;

    @BindView(R.id.swipe_refresh)
    SwipyRefreshLayout swipyRefreshLayout;

    @BindView(R.id.progress_main)
    ProgressBar progressBar;

    private View rootView;
    private MainFragmentAdapter adapter;

    public void setProgressBarVisible(){
        progressBar.setVisibility(View.VISIBLE);
    }

    public void setProgressBarGone(){
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(net.iquesoft.android.seedprojectchat.R.layout.fragment_main, container, false);
            ButterKnife.bind(this, rootView);
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        swipyRefreshLayout.setOnRefreshListener(direction -> {
            presenter.getFriendListAndGroupChatList();
            swipyRefreshLayout.setRefreshing(false);
        });
        super.onActivityCreated(savedInstanceState);
    }

    public void setRecyclerMain(List<BaseChatModel> baseChatModelList){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerMain.setLayoutManager(layoutManager);
        adapter = new MainFragmentAdapter(baseChatModelList, (MainActivity) getActivity());
        recyclerMain.setItemAnimator(new ScaleInAnimator(new OvershootInterpolator(1f)));
        ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(adapter);
        scaleInAnimationAdapter.setFirstOnly(false);
        scaleInAnimationAdapter.setDuration(500);
        setProgressBarGone();
        recyclerMain.setAdapter(scaleInAnimationAdapter);
    }
}
