package com.iquesoft.andrew.seedprojectchat.model;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.persistence.BackendlessDataQuery;
import com.iquesoft.andrew.seedprojectchat.util.Future;

/**
 * Created by andru on 8/31/2016.
 */

public class Friends
{
    private Integer status;
    private java.util.Date created;
    private String objectId;
    private String ownerId;
    private java.util.Date updated;
    private BackendlessUser user_one;
    private BackendlessUser user_two;
    public Integer getStatus()
    {
        return status;
    }

    public void setStatus( Integer status )
    {
        this.status = status;
    }

    public java.util.Date getCreated()
    {
        return created;
    }

    public String getObjectId()
    {
        return objectId;
    }

    public String getOwnerId()
    {
        return ownerId;
    }

    public java.util.Date getUpdated()
    {
        return updated;
    }

    public BackendlessUser getUser_one()
    {
        return user_one;
    }

    public void setUser_one( BackendlessUser user_one )
    {
        this.user_one = user_one;
    }

    public BackendlessUser getUser_two()
    {
        return user_two;
    }

    public void setUser_two( BackendlessUser user_two )
    {
        this.user_two = user_two;
    }


    public Friends save()
    {
        return Backendless.Data.of( Friends.class ).save( this );
    }

    public Future<Friends> saveAsync()
    {
        if( Backendless.isAndroid() )
        {
            throw new UnsupportedOperationException( "Using this method is restricted in Android" );
        }
        else
        {
            Future<Friends> future = new Future<Friends>();
            Backendless.Data.of( Friends.class ).save( this, future );

            return future;
        }
    }

    public void saveAsync( AsyncCallback<Friends> callback )
    {
        Backendless.Data.of( Friends.class ).save( this, callback );
    }

    public Long remove()
    {
        return Backendless.Data.of( Friends.class ).remove( this );
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
            Backendless.Data.of( Friends.class ).remove( this, future );

            return future;
        }
    }

    public void removeAsync( AsyncCallback<Long> callback )
    {
        Backendless.Data.of( Friends.class ).remove( this, callback );
    }

    public static Friends findById( String id )
    {
        return Backendless.Data.of( Friends.class ).findById( id );
    }

    public static Future<Friends> findByIdAsync( String id )
    {
        if( Backendless.isAndroid() )
        {
            throw new UnsupportedOperationException( "Using this method is restricted in Android" );
        }
        else
        {
            Future<Friends> future = new Future<Friends>();
            Backendless.Data.of( Friends.class ).findById( id, future );

            return future;
        }
    }

    public static void findByIdAsync( String id, AsyncCallback<Friends> callback )
    {
        Backendless.Data.of( Friends.class ).findById( id, callback );
    }

    public static Friends findFirst()
    {
        return Backendless.Data.of( Friends.class ).findFirst();
    }

    public static Future<Friends> findFirstAsync()
    {
        if( Backendless.isAndroid() )
        {
            throw new UnsupportedOperationException( "Using this method is restricted in Android" );
        }
        else
        {
            Future<Friends> future = new Future<Friends>();
            Backendless.Data.of( Friends.class ).findFirst( future );

            return future;
        }
    }

    public static void findFirstAsync( AsyncCallback<Friends> callback )
    {
        Backendless.Data.of( Friends.class ).findFirst( callback );
    }

    public static Friends findLast()
    {
        return Backendless.Data.of( Friends.class ).findLast();
    }

    public static Future<Friends> findLastAsync()
    {
        if( Backendless.isAndroid() )
        {
            throw new UnsupportedOperationException( "Using this method is restricted in Android" );
        }
        else
        {
            Future<Friends> future = new Future<Friends>();
            Backendless.Data.of( Friends.class ).findLast( future );

            return future;
        }
    }

    public static void findLastAsync( AsyncCallback<Friends> callback )
    {
        Backendless.Data.of( Friends.class ).findLast( callback );
    }

    public static BackendlessCollection<Friends> find(BackendlessDataQuery query )
    {
        return Backendless.Data.of( Friends.class ).find( query );
    }

    public static Future<BackendlessCollection<Friends>> findAsync( BackendlessDataQuery query )
    {
        if( Backendless.isAndroid() )
        {
            throw new UnsupportedOperationException( "Using this method is restricted in Android" );
        }
        else
        {
            Future<BackendlessCollection<Friends>> future = new Future<BackendlessCollection<Friends>>();
            Backendless.Data.of( Friends.class ).find( query, future );

            return future;
        }
    }

    public static void findAsync( BackendlessDataQuery query, AsyncCallback<BackendlessCollection<Friends>> callback )
    {
        Backendless.Data.of( Friends.class ).find( query, callback );
    }

    public static void findAllAsync(AsyncCallback<BackendlessCollection<Friends>> callback )
    {
        Backendless.Data.of( Friends.class ).find(callback);
    }
}
