package net.iquesoft.android.seedprojectchat.presenter.classes.fragments;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import net.iquesoft.android.seedprojectchat.Network.ApiCall;
import net.iquesoft.android.seedprojectchat.model.BaseChatModel;
import net.iquesoft.android.seedprojectchat.view.interfaces.fragments.IMainFragment;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@InjectViewState
public class MainFragmentPresenter extends MvpPresenter<IMainFragment> {

    private ArrayList<BaseChatModel> objectArrayList = new ArrayList<>();
    private Subscription subscription;

    @Override
    protected void onFirstViewAttach() {
        getFriendListAndGroupChatList();
        super.onFirstViewAttach();
    }
//
//    @Override
//    public void detachView(IMainFragment view) {
//        subscription.unsubscribe();
//        super.detachView(view);
//    }

    public void getFriendListAndGroupChatList(){
        getViewState().setProgressBarVisible();
        objectArrayList.clear();
        subscription = ApiCall.getCurentFriendList().subscribe(response -> {
            Observable.from(response).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(friends -> {
                objectArrayList.add(friends);
            });
            getGroupChatListAndAddAdapter();
        });
    }

    private void getGroupChatListAndAddAdapter() {
        ApiCall.getGroupChatList()
                .subscribe(response -> {
                    Observable.from(response).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(friends -> objectArrayList.add(friends));
                    setRecyclerAdapter(objectArrayList);
                });
    }

    private void setRecyclerAdapter(List<BaseChatModel> objectArrayList) {
        Observable.just(objectArrayList).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .flatMap(response -> Observable.from(response)
                        .filter(resp -> resp.getMessages().size() != 0).filter(resp -> resp.getUpdated() != null)
                        .toSortedList((messages, messages2) -> messages2.getUpdated()
                                .compareTo(messages.getUpdated())))
                .subscribe(response -> {
                    getViewState().setRecyclerMain(response);
                });
    }
}
