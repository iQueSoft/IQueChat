package net.iquesoft.android.seedprojectchat.presenter.classes.fragments;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.persistence.BackendlessDataQuery;

import net.iquesoft.android.seedprojectchat.common.DefaultBackendlessCallback;
import net.iquesoft.android.seedprojectchat.model.Friends;
import net.iquesoft.android.seedprojectchat.presenter.interfaces.fragments.IFriendsFragmentPresenter;
import net.iquesoft.android.seedprojectchat.view.interfaces.fragments.IFriendsFragment;

import java.util.LinkedList;
import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

@InjectViewState
public class FriendsFragmentPresenter extends MvpPresenter<IFriendsFragment> implements IFriendsFragmentPresenter {

    private Subscription subscription;

    private List<Friends> friendsList = new LinkedList<>();
    private PublishSubject<List<Friends>> friendsObservable = PublishSubject.create();
    private String whereClause = "(user_one.objectId='" + Backendless.UserService.CurrentUser().getProperty("objectId") + "'" +
            " or " +
            "user_two.objectId='" + Backendless.UserService.CurrentUser().getProperty("objectId") + "')" +
            " and " +
            "status = '2'";

    public List<Friends> getFriendsList() {
        return friendsList;
    }

    public void setFriendsList(List<Friends> friendsList) {
        this.friendsList.clear();
        this.friendsList = friendsList;
    }

    @Override
    public void attachView(IFriendsFragment view) {
        subscription = getCurentFriendList().subscribe(response -> getViewState().setUserAdapter(response));
        super.attachView(view);
    }

    @Override
    public void detachView(IFriendsFragment view) {
        subscription.unsubscribe();
        super.detachView(view);
    }

    @Override
    public boolean isInRestoreState(IFriendsFragment view) {
        return super.isInRestoreState(view);
    }

    public PublishSubject<List<Friends>> getCurentFriendList(){
        friendsObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        getViewState().setProgressBarVisible();
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

    public void updateCurentFriendList(){
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
