package net.iquesoft.android.seedprojectchat.model;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.persistence.BackendlessDataQuery;
import net.iquesoft.android.seedprojectchat.util.Future;

public class GroupChat extends BaseChatModel
{
    private String ownerId;
    private String chanel;
    private String chatName;
    private BackendlessUser owner;
    private java.util.List<BackendlessUser> users;

    public String getOwnerId()
    {
        return ownerId;
    }

    public String getChanel()
    {
        return chanel;
    }

    public void setChanel( String chanel )
    {
        this.chanel = chanel;
    }

    public String getChatName()
    {
        return chatName;
    }

    public void setChatName( String chatName )
    {
        this.chatName = chatName;
    }

    public BackendlessUser getOwner()
    {
        return owner;
    }

    public void setOwner( BackendlessUser owner )
    {
        this.owner = owner;
    }

    public java.util.List<BackendlessUser> getUsers()
    {
        return users;
    }

    public void setUsers( java.util.List<BackendlessUser> users )
    {
        this.users = users;
    }


    public GroupChat save()
    {
        return Backendless.Data.of( GroupChat.class ).save( this );
    }

    public Future<GroupChat> saveAsync()
    {
        if( Backendless.isAndroid() )
        {
            throw new UnsupportedOperationException( "Using this method is restricted in Android" );
        }
        else
        {
            Future<GroupChat> future = new Future<GroupChat>();
            Backendless.Data.of( GroupChat.class ).save( this, future );

            return future;
        }
    }

    public void saveAsync( AsyncCallback<GroupChat> callback )
    {
        try {
            Backendless.Data.of( GroupChat.class ).save( this, callback );
        } catch (RuntimeException e){
            e.printStackTrace();
        }
    }

    public Long remove()
    {
        return Backendless.Data.of( GroupChat.class ).remove( this );
    }

    public Future<Long> removeAsync()
    {
        if( Backendless.isAndroid() )
        {
            throw new UnsupportedOperationException( "Using this method is restricted in Android" );
        }
        else
        {
            Future<Long> future = new Future<Long>();
            Backendless.Data.of( GroupChat.class ).remove( this, future );

            return future;
        }
    }

    public void removeAsync( AsyncCallback<Long> callback )
    {
        Backendless.Data.of( GroupChat.class ).remove( this, callback );
    }

    public static GroupChat findById( String id )
    {
        return Backendless.Data.of( GroupChat.class ).findById( id );
    }

    public static Future<GroupChat> findByIdAsync( String id )
    {
        if( Backendless.isAndroid() )
        {
            throw new UnsupportedOperationException( "Using this method is restricted in Android" );
        }
        else
        {
            Future<GroupChat> future = new Future<GroupChat>();
            Backendless.Data.of( GroupChat.class ).findById( id, future );

            return future;
        }
    }

    public static void findByIdAsync( String id, AsyncCallback<GroupChat> callback )
    {
        Backendless.Data.of( GroupChat.class ).findById( id, callback );
    }

    public static GroupChat findFirst()
    {
        return Backendless.Data.of( GroupChat.class ).findFirst();
    }

    public static Future<GroupChat> findFirstAsync()
    {
        if( Backendless.isAndroid() )
        {
            throw new UnsupportedOperationException( "Using this method is restricted in Android" );
        }
        else
        {
            Future<GroupChat> future = new Future<GroupChat>();
            Backendless.Data.of( GroupChat.class ).findFirst( future );

            return future;
        }
    }

    public static void findFirstAsync( AsyncCallback<GroupChat> callback )
    {
        Backendless.Data.of( GroupChat.class ).findFirst( callback );
    }

    public static GroupChat findLast()
    {
        return Backendless.Data.of( GroupChat.class ).findLast();
    }

    public static Future<GroupChat> findLastAsync()
    {
        if( Backendless.isAndroid() )
        {
            throw new UnsupportedOperationException( "Using this method is restricted in Android" );
        }
        else
        {
            Future<GroupChat> future = new Future<GroupChat>();
            Backendless.Data.of( GroupChat.class ).findLast( future );

            return future;
        }
    }

    public static void findLastAsync( AsyncCallback<GroupChat> callback )
    {
        Backendless.Data.of( GroupChat.class ).findLast( callback );
    }

    public static BackendlessCollection<GroupChat> find( BackendlessDataQuery query )
    {
        return Backendless.Data.of( GroupChat.class ).find( query );
    }

    public static Future<BackendlessCollection<GroupChat>> findAsync( BackendlessDataQuery query )
    {
        if( Backendless.isAndroid() )
        {
            throw new UnsupportedOperationException( "Using this method is restricted in Android" );
        }
        else
        {
            Future<BackendlessCollection<GroupChat>> future = new Future<BackendlessCollection<GroupChat>>();
            Backendless.Data.of( GroupChat.class ).find( query, future );

            return future;
        }
    }

    public static void findAsync( BackendlessDataQuery query, AsyncCallback<BackendlessCollection<GroupChat>> callback )
    {
        Backendless.Data.of( GroupChat.class ).find( query, callback );
    }
}