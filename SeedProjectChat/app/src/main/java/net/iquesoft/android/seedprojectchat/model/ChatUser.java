package net.iquesoft.android.seedprojectchat.model;

import com.backendless.BackendlessUser;

public class ChatUser extends BackendlessUser {

    public static final String NAME = "name";
    public static final String PHOTO = "photo";
    public static final String DEVICEID = "device_id";
    public static final String ONLINE = "online";

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
        return (String) super.getProperty( DEVICEID );
    }

    public void setDevice_id( String device_id )
    {
        super.setProperty( DEVICEID, device_id );
    }

    public String getName()
    {
        return (String) super.getProperty( NAME );
    }

    public void setName( String name )
    {
        super.setProperty( NAME, name );
    }

    public String getPhoto()
    {
        return (String) super.getProperty( PHOTO );
    }

    public void setPhoto( String photo )
    {
        super.setProperty( PHOTO, photo );
    }

    public Boolean getOnline()
    {
        return (Boolean) super.getProperty( ONLINE );
    }

    public void setOnline( Boolean online )
    {
        super.setProperty( ONLINE, online );
    }

}
