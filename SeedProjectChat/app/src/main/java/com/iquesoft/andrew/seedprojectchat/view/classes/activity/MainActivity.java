package com.iquesoft.andrew.seedprojectchat.view.classes.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.iquesoft.andrew.seedprojectchat.R;
import com.iquesoft.andrew.seedprojectchat.common.BaseActivity;
import com.iquesoft.andrew.seedprojectchat.di.IHasComponent;
import com.iquesoft.andrew.seedprojectchat.di.components.DaggerIMainActivityComponent;
import com.iquesoft.andrew.seedprojectchat.di.components.IMainActivityComponent;
import com.iquesoft.andrew.seedprojectchat.di.components.ISeedProjectChatComponent;
import com.iquesoft.andrew.seedprojectchat.di.modules.MainActivityModule;
import com.iquesoft.andrew.seedprojectchat.model.ChatUser;
import com.iquesoft.andrew.seedprojectchat.presenter.classes.activity.MainActivityPresenter;
import com.iquesoft.andrew.seedprojectchat.util.UpdateCurentUser;
import com.iquesoft.andrew.seedprojectchat.view.classes.fragments.ContainerFriendFragment;
import com.iquesoft.andrew.seedprojectchat.view.classes.fragments.FindFriendFragment;
import com.iquesoft.andrew.seedprojectchat.view.interfaces.activity.IMainActivity;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, IMainActivity, IHasComponent<IMainActivityComponent> {

    private IMainActivityComponent mainActivityComponent;

    @Inject
    MainActivityPresenter presenter;

    @Inject
    FindFriendFragment findFriendFragment;

    @Inject
    ContainerFriendFragment containerFriendFragment;

    @Inject
    UpdateCurentUser updateCurentUser;

    //@BindView(R.id.header_image_view)
    CircularImageView headerImage;
    //@BindView(R.id.tv_user_name)
    TextView userName;
    //@BindView(R.id.tv_user_email)
    TextView userEMail;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        fragmentManager = getSupportFragmentManager();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        headerImage = (CircularImageView) headerView.findViewById(R.id.header_image_view);
        userName = (TextView) headerView.findViewById(R.id.tv_user_name);
        userEMail = (TextView) headerView.findViewById(R.id.tv_user_email);

        Uri uri = Uri.parse(Backendless.UserService.CurrentUser().getProperty("photo").toString());
        Picasso.with(getBaseContext()).load(uri).placeholder(R.drawable.seed_logo).into(headerImage);
        userName.setText(Backendless.UserService.CurrentUser().getProperty("name").toString());
        userEMail.setText(Backendless.UserService.CurrentUser().getProperty("email").toString());

    }

    @Override
    protected void onStop() {
        BackendlessUser backendlessUser = Backendless.UserService.CurrentUser();
        backendlessUser.setProperty(ChatUser.ONLINE, false);
        updateCurentUser.update(backendlessUser, this);
        super.onStop();
    }

    @Override
    protected void onResume() {
        BackendlessUser backendlessUser = Backendless.UserService.CurrentUser();
        backendlessUser.setProperty(ChatUser.ONLINE, true);
        updateCurentUser.update(backendlessUser, this);
        super.onResume();
    }

    public void setFindFriendFragment() {
        replaceFragment(findFriendFragment, "findFriendFragment");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            Backendless.UserService.logout(new AsyncCallback<Void>() {
                public void handleResponse(Void response) {
                    // user has been logged out.
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }

                public void handleFault(BackendlessFault fault) {
                    // something went wrong and logout failed, to get the error code call fault.getCode()
                }
            });
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_friends) {
            replaceFragment(containerFriendFragment, "containerFriendFragment");
        } else if (id == R.id.nav_gallery) {
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void replaceFragment(Fragment fragment, String TAG) {
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment, TAG);
        fragmentTransaction.addToBackStack("backpressed stack");
        fragmentTransaction.commit();
    }


    @Override
    protected void setupComponent(ISeedProjectChatComponent appComponent) {
        mainActivityComponent = DaggerIMainActivityComponent.builder()
                .iSeedProjectChatComponent(appComponent)
                .mainActivityModule(new MainActivityModule(this))
                .build();
        mainActivityComponent.inject(this);
    }

    @Override
    public IMainActivityComponent getComponent() {
        return mainActivityComponent;
    }
}
