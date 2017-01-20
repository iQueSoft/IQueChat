package net.iquesoft.android.seedprojectchat.view.classes.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.mikhaellopez.circularimageview.CircularImageView;

import net.iquesoft.android.seedprojectchat.common.BaseFragment;
import net.iquesoft.android.seedprojectchat.di.components.ILoginActivityComponent;
import net.iquesoft.android.seedprojectchat.presenter.classes.fragments.RegisterFragmentPresenter;
import net.iquesoft.android.seedprojectchat.util.ValidateUtil;
import net.iquesoft.android.seedprojectchat.view.classes.activity.LoginActivity;
import net.iquesoft.android.seedprojectchat.view.interfaces.fragments.IRegisterFragment;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.zelory.compressor.FileUtil;

import static android.app.Activity.RESULT_OK;

public class RegisterFragment extends BaseFragment implements IRegisterFragment {

    @InjectPresenter
    RegisterFragmentPresenter presenter;

    @Inject
    ValidateUtil validateUtil;

    @BindView(net.iquesoft.android.seedprojectchat.R.id.e_mail_tv)
    TextView eMailTV;
    @BindView(net.iquesoft.android.seedprojectchat.R.id.username_tv)
    TextView usernameTV;
    @BindView(net.iquesoft.android.seedprojectchat.R.id.password_tv)
    TextView passwordTV;
    @BindView(net.iquesoft.android.seedprojectchat.R.id.cim_photo_view)
    CircularImageView circleImageView;

    private View rootView;

    static final int GALLERY_REQUEST = 1;

    @OnClick(net.iquesoft.android.seedprojectchat.R.id.register_button)
    void registerButtonClick(View view) {
        presenter.onRegisterButtonClicked(eMailTV, usernameTV, passwordTV, validateUtil,(LoginActivity) getActivity());
    }

    @OnClick(net.iquesoft.android.seedprojectchat.R.id.cim_photo_view)
    void choosePhotoClick() {
        photoSelector();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(net.iquesoft.android.seedprojectchat.R.layout.fragment_registration, container, false);
            ButterKnife.bind(this, rootView);
        }
        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.getComponent(ILoginActivityComponent.class).inject(this);
    }

    public void photoSelector() {
        if (eMailTV.getText().toString().equals("")) {
            eMailTV.setError("Insert you eMail");
            eMailTV.requestFocus();
        } else {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        File image = null;
        try {
             image = FileUtil.from(getActivity(), imageReturnedIntent.getData());
        } catch (IOException e) {
            e.printStackTrace();
        }

        switch (requestCode) {
            case GALLERY_REQUEST:
                if (resultCode == RESULT_OK) {
                    presenter.uploadUserPhoto(image, circleImageView, eMailTV.getText().toString(), getActivity());
                }
        }
    }

}
