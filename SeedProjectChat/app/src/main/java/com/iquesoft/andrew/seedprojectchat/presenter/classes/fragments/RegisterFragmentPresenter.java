package com.iquesoft.andrew.seedprojectchat.presenter.classes.fragments;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.files.BackendlessFile;
import com.iquesoft.andrew.seedprojectchat.R;
import com.iquesoft.andrew.seedprojectchat.common.DefaultBackendlessCallback;
import com.iquesoft.andrew.seedprojectchat.model.ChatUser;
import com.iquesoft.andrew.seedprojectchat.presenter.interfaces.fragments.IRegisterFragmentPresenter;
import com.iquesoft.andrew.seedprojectchat.view.interfaces.fragments.IRegisterFragment;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.io.File;

import javax.inject.Inject;

import id.zelory.compressor.Compressor;

/**
 * Created by Andrew on 17.08.2016.
 */

public class RegisterFragmentPresenter implements IRegisterFragmentPresenter {

    private IRegisterFragment view;
    private ChatUser chatUser;
    private String uriPhoto;


    @Inject
    public RegisterFragmentPresenter(){

    }

    @Override
    public void init(IRegisterFragment view) {
        this.view = view;
    }

    @Override
    public void uploadUserPhoto(File file, CircularImageView circleImageView, String userEMail) {
        Thread uploadPhotoThread = new Thread(() -> {
            if (userEMail != null){
                Bitmap compressedImageBitmap = Compressor.getDefault(view.getActivityContext()).compressToBitmap(file);
                Backendless.Files.Android.upload(compressedImageBitmap, Bitmap.CompressFormat.PNG, 80, userEMail + "-MainPhoto.png" ,"userPhoto", new AsyncCallback<BackendlessFile>() {
                    @Override
                    public void handleResponse(final BackendlessFile backendlessFile) {
                        uriPhoto = backendlessFile.getFileURL();
                        Log.i("response", uriPhoto);
                        Uri uri = Uri.parse(uriPhoto);
                        Picasso.with(view.getActivityContext()).load(uri).placeholder(R.drawable.seed_logo).into(circleImageView);
                    }

                    @Override
                    public void handleFault(BackendlessFault backendlessFault)
                    {
                        Toast.makeText(view.getActivityContext(), backendlessFault.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(view.getActivityContext(), "Insert you eMail", Toast.LENGTH_LONG).show();
            }
        });
        uploadPhotoThread.start();
    }

    public void onRegisterButtonClicked(TextView emailText, TextView nameText, TextView passwordText)
    {
        String email = null, name = null, password = null;

        if( !emailText.getText().toString().isEmpty() & view.getValidateUtil().isEmailValid(emailText.getText().toString()) )
        {
            email = emailText.getText().toString();
        } else {
            emailText.setError("Field 'email' cannot be empty or invalid email format");
        }

        if( !nameText.getText().toString().isEmpty())
        {
            name = nameText.getText().toString();
        } else {
            nameText.setError("Field 'Username' cannot be empty");
        }

        if( !passwordText.getText().toString().isEmpty() & view.getValidateUtil().isPasswordValid(passwordText.getText().toString()))
        {
            password = passwordText.getText().toString();
        } else {
            passwordText.setError("Field 'password' cannot be empty or contain less 4 characters");
        }

        if( email != null & name != null & password != null)
        {
            chatUser = new ChatUser();
            chatUser.setEmail( email );
            chatUser.setName( name );
            chatUser.setPassword( password );
            chatUser.setPhoto(uriPhoto);
            Thread registerThread = new Thread(() -> {
                Backendless.UserService.register( chatUser, new DefaultBackendlessCallback<BackendlessUser>(view.getActivityContext())
                {
                    @Override
                    public void handleResponse( BackendlessUser response )
                    {
                        super.handleResponse( response );
                        Log.i("response", response.toString());
                        showToast("You sucsesfull registred");
                        view.getLoginActivity().setLoginFragment();
                    }
                });
            });
            registerThread.start();
        }

    }

    private void showToast( String msg )
    {
        Toast.makeText( view.getActivityContext(), msg, Toast.LENGTH_LONG ).show();
    }
}
