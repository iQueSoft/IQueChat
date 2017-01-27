package net.iquesoft.android.seedprojectchat.presenter.classes.fragments;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.persistence.BackendlessDataQuery;

import net.iquesoft.android.seedprojectchat.common.DefaultBackendlessCallback;
import net.iquesoft.android.seedprojectchat.model.Friends;
import net.iquesoft.android.seedprojectchat.presenter.interfaces.fragments.IMyInviteFragmentPresenter;
import net.iquesoft.android.seedprojectchat.view.interfaces.fragments.IMyInvateFragment;

import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

@InjectViewState
public class MyInviteFragmentPresenter extends MvpPresenter<IMyInvateFragment> implements IMyInviteFragmentPresenter {

    private Subscription subscription;
    private PublishSubject<List<Friends>> myInviteFriendsBS = PublishSubject.create();
    private String whereClause = "user_one.objectId='" + Backendless.UserService.CurrentUser().getProperty("objectId") + "'" +
            " and " +
            "status = '1'";

    @Override
    public void attachView(IMyInvateFragment view) {
        getViewState().setProgressBarVisible();
        subscription = getMyInviteFriendsList().subscribe(response -> getViewState().setUserAdapter(response));
        super.attachView(view);
    }

    @Override
    public void detachView(IMyInvateFragment view) {
        subscription.unsubscribe();
        super.detachView(view);
    }

    public PublishSubject<List<Friends>> getMyInviteFriendsList(){
        myInviteFriendsBS.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setWhereClause(whereClause);
            Friends.findAsync(dataQuery, new DefaultBackendlessCallback<BackendlessCollection<Friends>>(){
                @Override
                public void handleResponse(BackendlessCollection<Friends> response) {
                    myInviteFriendsBS.onNext(response.getData());
                }
            });
        return myInviteFriendsBS;
    }

    public void updateMyInviteList(){
        getViewState().setProgressBarVisible();
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setWhereClause(whereClause);
        Friends.findAsync(dataQuery, new DefaultBackendlessCallback<BackendlessCollection<Friends>>(){
            @Override
            public void handleResponse(BackendlessCollection<Friends> response) {
                myInviteFriendsBS.onNext(response.getData());
            }
        });
    }

}
