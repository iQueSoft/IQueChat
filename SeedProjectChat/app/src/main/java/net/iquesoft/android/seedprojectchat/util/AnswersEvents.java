package net.iquesoft.android.seedprojectchat.util;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.crashlytics.android.answers.CustomEvent;
import com.crashlytics.android.answers.LoginEvent;
import com.crashlytics.android.answers.SearchEvent;
import com.crashlytics.android.answers.SignUpEvent;

public class AnswersEvents {
    private static AnswersEvents ourInstance = new AnswersEvents();

    public static AnswersEvents getInstance() {
        return ourInstance;
    }

    private AnswersEvents() {
    }

    public void register(String putMethod){
        Answers.getInstance().logSignUp(new SignUpEvent()
                .putMethod(putMethod)
                .putSuccess(true));
    }

    public void login(String putMethod){
        Answers.getInstance().logLogin(new LoginEvent()
                .putMethod(putMethod)
                .putSuccess(true));
    }

    public void lookContentView(){
        Answers.getInstance().logContentView(new ContentViewEvent()
                .putContentName("Answers setup process super easy!")
                .putContentType("Technical documentation")
                .putContentId("article-350"));
    }

    public void search(){
        Answers.getInstance().logSearch(new SearchEvent()
                .putQuery("mobile analytics"));
    }

    public void customEvent(){
        Answers.getInstance().logCustom(new CustomEvent("Played Song")
                .putCustomAttribute("Custom String", "My String")
                .putCustomAttribute("Custom Number", 25));
    }
}
