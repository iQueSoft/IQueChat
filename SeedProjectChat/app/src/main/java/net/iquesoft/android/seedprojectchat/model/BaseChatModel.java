package net.iquesoft.android.seedprojectchat.model;

import java.util.Date;

public class BaseChatModel {
    private java.util.Date created;
    private java.util.Date updated;
    private Double serialVersionUID;
    private String objectId;
    private java.util.List<Messages> messages;

    public java.util.Date getCreated()
    {
        return created;
    }

    public java.util.Date getUpdated()
    {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Double getSerialVersionUID()
    {
        return serialVersionUID;
    }

    public void setSerialVersionUID( Double serialVersionUID )
    {
        this.serialVersionUID = serialVersionUID;
    }

    public String getObjectId()
    {
        return objectId;
    }

    public java.util.List<Messages> getMessages()
    {
        return messages;
    }

    public void setMessages( java.util.List<Messages> messages )
    {
        this.messages = messages;
    }

}
