package net.iquesoft.android.seedprojectchat.presenter.classes.fragments;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.persistence.BackendlessDataQuery;

import net.iquesoft.android.seedprojectchat.common.DefaultBackendlessCallback;
import net.iquesoft.android.seedprojectchat.model.Friends;
import net.iquesoft.android.seedprojectchat.presenter.interfaces.fragments.IInviteToFriendFragmentPresenter;
import net.iquesoft.android.seedprojectchat.view.interfaces.fragments.IInviteToFriendFragment;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

@InjectViewState
public class InviteToFriendFragmentPresenter extends MvpPresenter<IInviteToFriendFragment> implements IInviteToFriendFragmentPresenter {

    private PublishSubject<List<Friends>> friendsObservable = PublishSubject.create();
    private String whereClause = "user_two.objectId='" + Backendless.UserService.CurrentUser().getProperty("objectId") + "'" +
            " and " +
            "status = '1'";

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        getViewState().setProgressBarVisible();
        getCurentFriendList().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(response -> getViewState().setUserAdapter(response));
    }

    private PublishSubject<List<Friends>> getCurentFriendList(){
        friendsObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setWhereClause(whereClause);
            Friends.findAsync(dataQuery, new DefaultBackendlessCallback<BackendlessCollection<Friends>>(){
                @Override
                public void handleResponse(BackendlessCollection<Friends> response) {
                    friendsObservable.onNext(response.getData());
                }
            });
        return friendsObservable;
    }

    public void updateInvites(){
        getViewState().setProgressBarVisible();
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setWhereClause(whereClause);
        Friends.findAsync(dataQuery, new DefaultBackendlessCallback<BackendlessCollection<Friends>>(){
            @Override
            public void handleResponse(BackendlessCollection<Friends> response) {
                friendsObservable.onNext(response.getData());
            }
        });
    }
}
