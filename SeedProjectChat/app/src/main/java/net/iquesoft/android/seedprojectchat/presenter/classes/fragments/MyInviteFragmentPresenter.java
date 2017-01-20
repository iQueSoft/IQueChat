package net.iquesoft.android.seedprojectchat.presenter.classes.fragments;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.BackendlessCallback;
import com.backendless.persistence.BackendlessDataQuery;
import net.iquesoft.android.seedprojectchat.model.Friends;
import net.iquesoft.android.seedprojectchat.presenter.interfaces.fragments.IMyInviteFragmentPresenter;
import net.iquesoft.android.seedprojectchat.view.interfaces.fragments.IMyInvateFragment;

import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

@InjectViewState
public class MyInviteFragmentPresenter extends MvpPresenter<IMyInvateFragment> implements IMyInviteFragmentPresenter {

    private Subscription subscription;

    @Override
    public void attachView(IMyInvateFragment view) {
        subscription = getMyInviteFriendsList().subscribe(response -> getViewState().setUserAdapter(response));
        super.attachView(view);
    }

    @Override
    public void detachView(IMyInvateFragment view) {
        subscription.unsubscribe();
        super.detachView(view);
    }

    public BehaviorSubject<List<Friends>> getMyInviteFriendsList(){
        BehaviorSubject<List<Friends>> myInviteFriendsBS = BehaviorSubject.create();
        myInviteFriendsBS.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        String whereClause = "user_one.objectId='" + Backendless.UserService.CurrentUser().getProperty("objectId") + "'" +
                " and " +
                "status = '1'";
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setWhereClause(whereClause);
            Friends.findAsync(dataQuery, new BackendlessCallback<BackendlessCollection<Friends>>(){
                @Override
                public void handleResponse(BackendlessCollection<Friends> response) {
                    Log.i("friend", response.getData().toString());
                    myInviteFriendsBS.onNext(response.getData());
                }
            });
        return myInviteFriendsBS;
    }

}
