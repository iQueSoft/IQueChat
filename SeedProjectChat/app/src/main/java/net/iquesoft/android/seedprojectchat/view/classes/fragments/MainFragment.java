package net.iquesoft.android.seedprojectchat.view.classes.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.presenter.InjectPresenter;

import net.iquesoft.android.seedprojectchat.adapters.MainFragmentAdapter;
import net.iquesoft.android.seedprojectchat.common.BaseFragment;
import net.iquesoft.android.seedprojectchat.model.BaseChatModel;
import net.iquesoft.android.seedprojectchat.presenter.classes.fragments.MainFragmentPresenter;
import net.iquesoft.android.seedprojectchat.view.classes.activity.MainActivity;
import net.iquesoft.android.seedprojectchat.view.interfaces.fragments.IMainFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainFragment extends BaseFragment implements IMainFragment {

    @InjectPresenter
    MainFragmentPresenter presenter;

    @BindView(net.iquesoft.android.seedprojectchat.R.id.main_screen_recycler_view)
    RecyclerView recyclerMain;

    private View rootView;
    private MainFragmentAdapter adapter;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    public void setRecyclerMain(List<BaseChatModel> baseChatModelList){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerMain.setLayoutManager(layoutManager);
        adapter = new MainFragmentAdapter(baseChatModelList, (MainActivity) getActivity());
        recyclerMain.setAdapter(adapter);
    }
}
