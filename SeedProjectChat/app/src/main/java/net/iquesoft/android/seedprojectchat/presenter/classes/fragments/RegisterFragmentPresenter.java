package net.iquesoft.android.seedprojectchat.presenter.classes.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.mvp.MvpPresenter;
import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.files.BackendlessFile;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import net.iquesoft.android.seedprojectchat.common.DefaultBackendlessCallback;
import net.iquesoft.android.seedprojectchat.model.ChatUser;
import net.iquesoft.android.seedprojectchat.presenter.interfaces.fragments.IRegisterFragmentPresenter;
import net.iquesoft.android.seedprojectchat.util.ValidateUtil;
import net.iquesoft.android.seedprojectchat.view.classes.activity.LoginActivity;
import net.iquesoft.android.seedprojectchat.view.interfaces.fragments.IRegisterFragment;

import java.io.File;

import id.zelory.compressor.Compressor;

public class RegisterFragmentPresenter extends MvpPresenter<IRegisterFragment> implements IRegisterFragmentPresenter {

    private String uriPhoto;

    public void uploadUserPhoto(File file, CircularImageView circleImageView, String userEMail, Context context) {
        String userMail = userEMail.replace("@", "");
        Thread uploadPhotoThread = new Thread(() -> {
            if (userMail != null) {
                Bitmap compressedImageBitmap = Compressor.getDefault(context).compressToBitmap(file);
                Backendless.Files.Android.upload(compressedImageBitmap, Bitmap.CompressFormat.PNG, 80, userMail + "-MainPhoto.png", "userPhoto", new AsyncCallback<BackendlessFile>() {
                    @Override
                    public void handleResponse(final BackendlessFile backendlessFile) {
                        uriPhoto = backendlessFile.getFileURL();
                        Log.i("response", uriPhoto);
                        Uri uri = Uri.parse(uriPhoto);
                        Picasso.with(context).load(uri).placeholder(net.iquesoft.android.seedprojectchat.R.drawable.seed_logo).into(circleImageView);
                    }

                    @Override
                    public void handleFault(BackendlessFault backendlessFault) {
                        Toast.makeText(context, backendlessFault.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(context, "Insert you eMail", Toast.LENGTH_LONG).show();
            }
        });
        uploadPhotoThread.start();
    }

    public void onRegisterButtonClicked(TextView emailText, TextView nameText, TextView passwordText, ValidateUtil validateUtil, LoginActivity loginActivity) {
        String email = null, name = null, password = null;

        if (!emailText.getText().toString().isEmpty() & validateUtil.isEmailValid(emailText.getText().toString())) {
            email = emailText.getText().toString();
        } else {
            emailText.setError("Field 'email' cannot be empty or invalid email format");
        }

        if (!nameText.getText().toString().isEmpty()) {
            name = nameText.getText().toString();
        } else {
            nameText.setError("Field 'Username' cannot be empty");
        }

        if (!passwordText.getText().toString().isEmpty() & validateUtil.isPasswordValid(passwordText.getText().toString())) {
            password = passwordText.getText().toString();
        } else {
            passwordText.setError("Field 'password' cannot be empty or contain less 4 characters");
        }

        if (email != null & name != null & password != null) {
            ChatUser chatUser = new ChatUser();
            chatUser.setEmail(email);
            chatUser.setName(name);
            chatUser.setPassword(password);
            chatUser.setPhoto(uriPhoto);
            Backendless.UserService.register(chatUser, new DefaultBackendlessCallback<BackendlessUser>() {
                @Override
                public void handleResponse(BackendlessUser response) {
                    super.handleResponse(response);
                    Log.i("response", response.toString());
                    showToast("You sucsesfull registred", loginActivity.getBaseContext());
                    loginActivity.setLoginFragment();
                }

                @Override
                public void handleFault(BackendlessFault fault) {
                    super.handleFault(fault);
                    showToast(fault.toString(), loginActivity.getBaseContext());
                }
            });
        }
    }

    private void showToast(String msg, Context context) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
}
