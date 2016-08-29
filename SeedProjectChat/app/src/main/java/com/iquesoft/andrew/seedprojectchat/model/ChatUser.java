package com.iquesoft.andrew.seedprojectchat.model;

import com.backendless.BackendlessUser;

/**
 * Created by Andrew on 17.08.2016.
 */

public class ChatUser extends BackendlessUser {

    public String getEmail()
    {
        return super.getEmail();
    }

    public void setEmail( String email )
    {
        super.setEmail( email );
    }

    public String getPassword()
    {
        return super.getPassword();
    }

    public String getDevice_id()
    {
        return (String) super.getProperty( "device_id" );
    }

    public void setDevice_id( String device_id )
    {
        super.setProperty( "device_id", device_id );
    }

    public String getName()
    {
        return (String) super.getProperty( "name" );
    }

    public void setName( String name )
    {
        super.setProperty( "name", name );
    }

    public String getPhoto()
    {
        return (String) super.getProperty( "photo" );
    }

    public void setPhoto( String photo )
    {
        super.setProperty( "photo", photo );
    }

    public Boolean getOnline()
    {
        return (Boolean) super.getProperty( "online" );
    }

    public void setOnline( Boolean online )
    {
        super.setProperty( "online", online );
    }

}
