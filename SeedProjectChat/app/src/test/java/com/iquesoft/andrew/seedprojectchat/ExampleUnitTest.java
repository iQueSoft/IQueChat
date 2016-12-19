package com.iquesoft.andrew.seedprojectchat;

import com.iquesoft.andrew.seedprojectchat.model.Messages;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void model_isCorrect() throws Exception{
        Messages messages = new Messages();
        assertNotNull(messages);
        messages.setRead(true);
        assertNotNull(messages.getRead());
        messages.setTimestamp(new Date());
        assertNotNull(messages.getTimestamp());
        messages.setData("TestData");
        assertNotNull(messages.getData());
        assertEquals("TestData", messages.getData());
        messages.setPublisher_id("Test");
        messages.setMessage_id("Test");
    }
}