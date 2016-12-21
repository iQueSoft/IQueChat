package com.iquesoft.andrew.seedprojectchat;

import com.iquesoft.andrew.seedprojectchat.model.BaseChatModel;
import com.iquesoft.andrew.seedprojectchat.model.Messages;
import com.iquesoft.andrew.seedprojectchat.util.Future;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest extends Mockito {

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void modelMessages_isCorrect() throws Exception{
        Messages mockedMessages = mock(Messages.class);
        Future<Messages> messagesFuture = mock(Future.class);
        when(mockedMessages.saveAsync()).thenReturn(messagesFuture);
        when(mockedMessages.getRead()).thenReturn(true);
        when(mockedMessages.getTimestamp()).thenReturn(new Date());
        when(mockedMessages.getData()).thenReturn("TestData");
        when(mockedMessages.getPublisher_id()).thenReturn("Test");
        when(mockedMessages.getMessage_id()).thenReturn("Test");
        assertNotNull(mockedMessages);
        assertNotNull(mockedMessages.getRead());
        assertNotNull(mockedMessages.getTimestamp());
        assertNotNull(mockedMessages.getData());
        assertEquals("TestData", mockedMessages.getData());
        assertNotNull(mockedMessages.saveAsync());
    }

    @Test
    public void modelBaseChatModel_isCorrect() throws Exception{
        BaseChatModel mockBaseChatModel = mock(BaseChatModel.class);
        Date date = new Date();
        when(mockBaseChatModel.getCreated()).thenReturn(date);
        assertNotNull(mockBaseChatModel);
        assertNotNull(mockBaseChatModel.getCreated());
        assertEquals(date, mockBaseChatModel.getCreated());
    }
}