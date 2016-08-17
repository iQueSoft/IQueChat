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

    public String getName()
    {
        return (String) super.getProperty( "name" );
    }

    public void setName( String name )
    {
        super.setProperty( "name", name );
    }
}
