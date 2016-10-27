package com.iquesoft.andrew.seedprojectchat.util;

/**
 * Created by andru on 10/27/2016.
 */
public class SelectedUri {
    private static SelectedUri ourInstance = new SelectedUri();

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    private String uri;

    public static SelectedUri getInstance() {
        return ourInstance;
    }

    private SelectedUri() {
    }
}
