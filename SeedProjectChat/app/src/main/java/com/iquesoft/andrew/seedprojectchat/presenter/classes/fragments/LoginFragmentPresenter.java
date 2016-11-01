package com.iquesoft.andrew.seedprojectchat.presenter.classes.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.Scopes;
import com.iquesoft.andrew.seedprojectchat.common.DefaultBackendlessCallback;
import com.iquesoft.andrew.seedprojectchat.model.ChatUser;
import com.iquesoft.andrew.seedprojectchat.presenter.interfaces.fragments.ILoginFragmentPresenter;
import com.iquesoft.andrew.seedprojectchat.util.UpdateCurentUser;
import com.iquesoft.andrew.seedprojectchat.view.classes.activity.LoginActivity;
import com.iquesoft.andrew.seedprojectchat.view.classes.activity.MainActivity;
import com.iquesoft.andrew.seedprojectchat.view.interfaces.fragments.ILoginFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Andrew on 17.08.2016.
 */

@InjectViewState
public class LoginFragmentPresenter extends MvpPresenter<ILoginFragment> implements ILoginFragmentPresenter {

    private String TAG = "google+";

    public void onLoginButtonClicked(String userEMail, String password, boolean rememberLogin, LoginActivity loginActivity, UpdateCurentUser updateCurentUser)
    {
        Thread loginThread = new Thread(() -> {
            Backendless.UserService.login( userEMail, password, new DefaultBackendlessCallback<BackendlessUser>(loginActivity.getContext())
            {
                public void handleResponse( BackendlessUser backendlessUser )
                {
                    super.handleResponse( backendlessUser );
                    Thread thread = new Thread(()->{
                        String deviceId = Build.SERIAL;
                        if( deviceId.isEmpty() )
                        {
                            Toast.makeText(loginActivity.getContext(), "Could not retrieve DEVICE ID", Toast.LENGTH_SHORT ).show();
                            return;
                        } else {
                            backendlessUser.setProperty(ChatUser.DEVICEID, deviceId);
                            backendlessUser.setProperty(ChatUser.ONLINE, true);
                            updateCurentUser.update(backendlessUser, loginActivity.getContext());
                        }
                    });
                    thread.start();
                    Log.i("login", backendlessUser.toString());
                    loginActivity.getContext().startActivity(new Intent(loginActivity.getContext(), MainActivity.class));
                    getViewState().showProgress(false);
                    loginActivity.finish();
                }

                @Override
                public void handleFault(BackendlessFault fault) {
                    super.handleFault(fault);
                    getViewState().showProgress(false);
                }
            }, rememberLogin );
        });
        loginThread.start();
    }

    public void onRestorePasswordButtonClicked(String eMail, Context context)
    {
        Thread restorePasswordThread = new Thread(() -> {
            Backendless.UserService.restorePassword( eMail, new DefaultBackendlessCallback<Void>(context)
            {
                @Override
                public void handleResponse( Void response )
                {
                    super.handleResponse( response );
                    Toast.makeText(context, "Please check you eMail", Toast.LENGTH_LONG).show();
                }
            } );
        });
        restorePasswordThread.start();
    }

    public void loginInBackendless(final GoogleSignInAccount acct, LoginActivity loginActivity)
    {
        Log.d( TAG, "handleSignInResult: try login to backendless" );
        final String accountName = acct.getEmail();

        final String scopes = "oauth2:" + Scopes.PLUS_LOGIN + " " +
                Scopes.PLUS_ME + " " + Scopes.PROFILE + " " +
                Scopes.EMAIL;

        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>(){
            @Override
            protected String doInBackground( Void... params )
            {
                String token = null;
                try
                {
                    token = GoogleAuthUtil.getToken( loginActivity.getContext(), accountName, scopes );
                    GoogleAuthUtil.invalidateToken( loginActivity.getContext(), token );
                    handleAccessTokenInBackendless( acct.getIdToken(), token, acct.getPhotoUrl().toString());
                }
                catch( UserRecoverableAuthException e )
                {
                    loginActivity.startActivityForResult( e.getIntent(), 2 );
                }
                catch( Exception e )
                {
                    Log.e( TAG, e.toString() );
                }
                return token;
            }
        };

        task.execute( );
    }



    private void handleAccessTokenInBackendless( String idToken, String accessToken, String photoURI )
    {
        Log.d( TAG, "idToken: "+idToken+ ", accessToken: "+accessToken );

        Map<String, String> googlePlusFieldsMapping = new HashMap<String, String>();
        googlePlusFieldsMapping.put( "given_name", "gp_given_name" );
        googlePlusFieldsMapping.put( "family_name", "gp_family_name" );
        googlePlusFieldsMapping.put( "gender", "gender" );
        googlePlusFieldsMapping.put("email", "email");
        List<String> permissions = new ArrayList<String>();

        if (idToken != null && accessToken != null)
            Backendless.UserService.loginWithGooglePlusSdk( idToken,
                    accessToken,
                    googlePlusFieldsMapping,
                    permissions,
                    new AsyncCallback<BackendlessUser>()
                    {
                        @Override
                        public void handleResponse( BackendlessUser backendlessUser )
                        {
                            Log.i( TAG, "Logged in to backendless, user id is: " + backendlessUser.getObjectId() );
                            backendlessUser.setProperty(ChatUser.PHOTO, photoURI);
                            getViewState().setCurentUser(backendlessUser);
                        }

                        @Override
                        public void handleFault( BackendlessFault backendlessFault )
                        {
                            Log.e( TAG, "Could not login to backendless: " +
                                    backendlessFault.getMessage() +
                                    " code: " + backendlessFault.getCode() );
                        }
                    });
    }

}
