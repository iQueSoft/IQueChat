package com.iquesoft.andrew.seedprojectchat.model;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.persistence.BackendlessDataQuery;
import com.iquesoft.andrew.seedprojectchat.util.Future;

import static com.backendless.Backendless.Data;

/**
 * Created by andru on 9/13/2016.
 */

public class Messages
{
    private String publisher_id;
    private String ownerId;
    private String message_id;
    private java.util.Date updated;
    private String objectId;
    private java.util.Date timestamp;
    private String data;
    private String header;
    private java.util.Date created;

    public String getPublisher_id()
    {
        return publisher_id;
    }

    public void setPublisher_id( String publisher_id )
    {
        this.publisher_id = publisher_id;
    }

    public String getOwnerId()
    {
        return ownerId;
    }

    public String getMessage_id()
    {
        return message_id;
    }

    public void setMessage_id( String message_id )
    {
        this.message_id = message_id;
    }

    public java.util.Date getUpdated()
    {
        return updated;
    }

    public String getObjectId()
    {
        return objectId;
    }

    public java.util.Date getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp( java.util.Date timestamp )
    {
        this.timestamp = timestamp;
    }

    public String getData()
    {
        return data;
    }

    public void setData( String data )
    {
        this.data = data;
    }

    public String getHeader()
    {
        return header;
    }

    public void setHeader( String header )
    {
        this.header = header;
    }

    public java.util.Date getCreated()
    {
        return created;
    }


    public Messages save()
    {
        return Data.of( Messages.class ).save( this );
    }

    public Future<Messages> saveAsync()
    {
        if( Backendless.isAndroid() )
        {
            throw new UnsupportedOperationException( "Using this method is restricted in Android" );
        }
        else
        {
            Future<Messages> future = new Future<Messages>();
            Data.of( Messages.class ).save( this, future );

            return future;
        }
    }

    public void saveAsync( AsyncCallback<Messages> callback )
    {
        Data.of( Messages.class ).save( this, callback );
    }

    public Long remove()
    {
        return Data.of( Messages.class ).remove( this );
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
            Data.of( Messages.class ).remove( this, future );

            return future;
        }
    }

    public void removeAsync( AsyncCallback<Long> callback )
    {
        Data.of( Messages.class ).remove( this, callback );
    }

    public static Messages findById( String id )
    {
        return Data.of( Messages.class ).findById( id );
    }

    public static Future<Messages> findByIdAsync( String id )
    {
        if( Backendless.isAndroid() )
        {
            throw new UnsupportedOperationException( "Using this method is restricted in Android" );
        }
        else
        {
            Future<Messages> future = new Future<Messages>();
            Data.of( Messages.class ).findById( id, future );

            return future;
        }
    }

    public static void findByIdAsync( String id, AsyncCallback<Messages> callback )
    {
        Data.of( Messages.class ).findById( id, callback );
    }

    public static Messages findFirst()
    {
        return Data.of( Messages.class ).findFirst();
    }

    public static Future<Messages> findFirstAsync()
    {
        if( Backendless.isAndroid() )
        {
            throw new UnsupportedOperationException( "Using this method is restricted in Android" );
        }
        else
        {
            Future<Messages> future = new Future<Messages>();
            Data.of( Messages.class ).findFirst( future );

            return future;
        }
    }

    public static void findFirstAsync( AsyncCallback<Messages> callback )
    {
        Data.of( Messages.class ).findFirst( callback );
    }

    public static Messages findLast()
    {
        return Data.of( Messages.class ).findLast();
    }

    public static Future<Messages> findLastAsync()
    {
        if( Backendless.isAndroid() )
        {
            throw new UnsupportedOperationException( "Using this method is restricted in Android" );
        }
        else
        {
            Future<Messages> future = new Future<Messages>();
            Data.of( Messages.class ).findLast( future );

            return future;
        }
    }

    public static void findLastAsync( AsyncCallback<Messages> callback )
    {
        Data.of( Messages.class ).findLast( callback );
    }

    public static BackendlessCollection<Messages> find(BackendlessDataQuery query )
    {
        return Data.of( Messages.class ).find( query );
    }

    public static Future<BackendlessCollection<Messages>> findAsync( BackendlessDataQuery query )
    {
        if( Backendless.isAndroid() )
        {
            throw new UnsupportedOperationException( "Using this method is restricted in Android" );
        }
        else
        {
            Future<BackendlessCollection<Messages>> future = new Future<BackendlessCollection<Messages>>();
            Data.of( Messages.class ).find( query, future );

            return future;
        }
    }

    public static void findAsync( BackendlessDataQuery query, AsyncCallback<BackendlessCollection<Messages>> callback )
    {
        Data.of( Messages.class ).find( query, callback );
    }
}
