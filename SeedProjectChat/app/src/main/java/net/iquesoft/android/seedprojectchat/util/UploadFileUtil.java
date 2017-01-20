package net.iquesoft.android.seedprojectchat.util;

import android.content.Context;

import com.backendless.Backendless;
import com.backendless.async.callback.BackendlessCallback;
import com.backendless.files.BackendlessFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import id.zelory.compressor.Compressor;
import rx.subjects.PublishSubject;

public class UploadFileUtil {
    public static synchronized PublishSubject<String> getAndCompressImageWithUri(List<String> uriList, Context context){
        ArrayList<File> compressedImageFile = new ArrayList<>();
        for (String uri : uriList){
            File image = new File(uri);
            File compressedImage = Compressor.getDefault(context).compressToFile(image);
            compressedImageFile.add(compressedImage);
        }
        PublishSubject<String> ps = PublishSubject.create();
        if (compressedImageFile.size() == uriList.size()){
            ArrayList<String> serverUriList = new ArrayList<>();
            for (File file : compressedImageFile){
                Backendless.Files.upload(file, Backendless.UserService.CurrentUser().getEmail() + "_send_images", true, new BackendlessCallback<BackendlessFile>() {
                    @Override
                    public void handleResponse(BackendlessFile backendlessFile) {
                        serverUriList.add(backendlessFile.getFileURL());
                        ps.onNext(backendlessFile.getFileURL());
                    }
                });
            }
        }

        return ps;
    }

    public static synchronized PublishSubject<String> uploadFilesToServer(List<String> uriList, Context context){
        ArrayList<File> documentList = new ArrayList<>();
        for (String uri : uriList){
            File file = new File(uri);
            documentList.add(file);
        }

        PublishSubject<String> ps = PublishSubject.create();
        for (File file : documentList){
            Backendless.Files.upload(file, Backendless.UserService.CurrentUser().getEmail() + "_send_files", true, new BackendlessCallback<BackendlessFile>() {
                @Override
                public void handleResponse(BackendlessFile backendlessFile) {
                    ps.onNext(backendlessFile.getFileURL());
                }
            });
        }

        return ps;
    }
}
