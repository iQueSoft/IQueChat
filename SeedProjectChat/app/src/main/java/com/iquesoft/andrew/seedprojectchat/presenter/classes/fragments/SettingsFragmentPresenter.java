package com.iquesoft.andrew.seedprojectchat.presenter.classes.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.async.callback.BackendlessCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.files.BackendlessFile;
import com.iquesoft.andrew.seedprojectchat.R;
import com.iquesoft.andrew.seedprojectchat.common.DefaultBackendlessCallback;
import com.iquesoft.andrew.seedprojectchat.model.ChatUser;
import com.iquesoft.andrew.seedprojectchat.view.interfaces.fragments.ISettingsFragment;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.io.File;

import id.zelory.compressor.Compressor;

/**
 * Created by andru on 10/20/2016.
 */
@InjectViewState
public class SettingsFragmentPresenter extends MvpPresenter<ISettingsFragment> {

    private BackendlessUser curentUser = Backendless.UserService.CurrentUser();

    private String uriPhoto;

    public void uploadUserPhoto(File file, CircularImageView circleImageView, String userEMail, Context context) {
        String userMail = userEMail.replace("@", "");
            if (userMail != null) {
                Bitmap compressedImageBitmap = Compressor.getDefault(context).compressToBitmap(file);
                String uri = Backendless.UserService.CurrentUser().getProperty("photo").toString();
                if (Backendless.UserService.CurrentUser().getProperty("photo").toString().equals("")){
                    Backendless.Files.Android.upload(compressedImageBitmap, Bitmap.CompressFormat.PNG, 80, userMail + "-MainPhoto.png", "userPhoto", new AsyncCallback<BackendlessFile>() {
                        @Override
                        public void handleResponse(final BackendlessFile backendlessFile) {
                            uriPhoto = backendlessFile.getFileURL();
                            curentUser.setProperty("photo", uriPhoto);
                            Uri uri = Uri.parse(uriPhoto);
                            Picasso.with(context).load(uri).placeholder(R.drawable.seed_logo).into(circleImageView);
                        }

                        @Override
                        public void handleFault(BackendlessFault backendlessFault) {
                            Toast.makeText(context, backendlessFault.toString(), Toast.LENGTH_SHORT).show();
                            Log.i("response", backendlessFault.toString());
                        }
                    });
                } else {
                    String updateUri = uri.replace("https://api.backendless.com/569FA24A-78A5-D20C-FF44-EC7884E70D00/v1/files/", "");
                    Log.d("response", updateUri);
                    Backendless.Files.remove(updateUri, new DefaultBackendlessCallback<Void>(){
                        @Override
                        public void handleResponse(Void response) {
                            Backendless.Files.Android.upload(compressedImageBitmap, Bitmap.CompressFormat.PNG, 80, userMail + "-MainPhoto.png", "userPhoto", new AsyncCallback<BackendlessFile>() {
                                @Override
                                public void handleResponse(final BackendlessFile backendlessFile) {
                                    uriPhoto = backendlessFile.getFileURL();
                                    Log.i("response", uriPhoto);
                                    curentUser.setProperty("photo", uriPhoto);
                                    Uri uri = Uri.parse(uriPhoto);
                                    Picasso.with(context).load(uri).placeholder(R.drawable.seed_logo).into(circleImageView);
                                }

                                @Override
                                public void handleFault(BackendlessFault backendlessFault) {
                                    Toast.makeText(context, backendlessFault.toString(), Toast.LENGTH_SHORT).show();
                                    Log.i("response", backendlessFault.toString());
                                }
                            });
                            Log.d("response", "HandleResponse");
                            super.handleResponse(response);
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Log.d("response", fault.toString());
                            super.handleFault(fault);
                        }
                    });
                }
            } else {
                Toast.makeText(context, "Insert you eMail", Toast.LENGTH_LONG).show();
            }
    }

    public BackendlessUser getCurentUser() {
        return curentUser;
    }

    public void saveUserInfo(EditText etUserEmail, EditText etUsername, Context context){
        curentUser.setProperty(ChatUser.EMAIL_KEY, etUserEmail.getText().toString());
        curentUser.setProperty(ChatUser.NAME, etUsername.getText().toString());
        Backendless.UserService.update(curentUser, new BackendlessCallback<BackendlessUser>(){
            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(context, fault.getDetail(), Toast.LENGTH_LONG).show();
                super.handleFault(fault);
            }

            @Override
            public void handleResponse(BackendlessUser backendlessUser) {
                Toast.makeText(context, "Update sucsesful", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void saveNewPassword(EditText etNewPassword, EditText etRepeatNewPassword, Context context){
        if (etNewPassword.getText().toString().equals(etRepeatNewPassword.getText().toString())){
            curentUser.setProperty(ChatUser.PASSWORD_KEY, etNewPassword.getText().toString());
            Backendless.UserService.update(curentUser, new BackendlessCallback<BackendlessUser>(){
                @Override
                public void handleFault(BackendlessFault fault) {
                    Toast.makeText(context, fault.getDetail(), Toast.LENGTH_LONG).show();
                    super.handleFault(fault);
                }

                @Override
                public void handleResponse(BackendlessUser backendlessUser) {
                    Toast.makeText(context, "Update sucsesful", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            etNewPassword.setError("Passwords do not match");
        }
    }
}
