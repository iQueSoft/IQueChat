package net.iquesoft.android.seedprojectchat.view.classes.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.iquesoft.android.seedprojectchat.common.BaseFragment;
import net.iquesoft.android.seedprojectchat.di.components.IMainActivityComponent;
import net.iquesoft.android.seedprojectchat.view.interfaces.fragments.IContainerFriendFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContainerFriendFragment extends BaseFragment implements IContainerFriendFragment{

    @Inject
    InviteToFriendFragment inviteToFriendFragment;

    @Inject
    FriendsFragment friendsFragment;

    @Inject
    MyInvateFragment myInvateFragment;

    @BindView(net.iquesoft.android.seedprojectchat.R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(net.iquesoft.android.seedprojectchat.R.id.pager)
    ViewPager pager;

    private View rootView;
    private SectionsPagerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(net.iquesoft.android.seedprojectchat.R.layout.fragment_container_friend, container, false);
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
        if (adapter == null){
            setViewPager();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void setViewPager() {
        if (tabLayout.getTabCount() == 0){
            tabLayout.addTab(tabLayout.newTab().setText("My Friends"));
            tabLayout.addTab(tabLayout.newTab().setText("Invite to Friends"));
            tabLayout.addTab(tabLayout.newTab().setText("My Invites"));
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        }
        adapter = new SectionsPagerAdapter(getActivity().getSupportFragmentManager());
        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    return friendsFragment;
                case 1:
                    return inviteToFriendFragment;
                case 2:
                    return myInvateFragment;
                default:
                    return friendsFragment;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }
}
