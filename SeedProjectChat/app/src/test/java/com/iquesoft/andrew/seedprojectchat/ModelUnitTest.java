package com.iquesoft.andrew.seedprojectchat;

import com.backendless.BackendlessUser;
import com.iquesoft.andrew.seedprojectchat.model.BaseChatModel;
import com.iquesoft.andrew.seedprojectchat.model.ChatUser;
import com.iquesoft.andrew.seedprojectchat.model.Friends;
import com.iquesoft.andrew.seedprojectchat.model.GroupChat;
import com.iquesoft.andrew.seedprojectchat.model.Messages;
import com.iquesoft.andrew.seedprojectchat.util.Future;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ModelUnitTest extends Mockito {

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
        Messages messages = mock(Messages.class);
        List<Messages> messagesList = new ArrayList<>();
        messagesList.add(messages);
        Date date = new Date();
        when(mockBaseChatModel.getCreated()).thenReturn(date);
        when(mockBaseChatModel.getMessages()).thenReturn(messagesList);
        when(mockBaseChatModel.getObjectId()).thenReturn("TestID");
        when(mockBaseChatModel.getUpdated()).thenReturn(date);
        assertNotNull(mockBaseChatModel);
        assertNotNull(mockBaseChatModel.getCreated());
        assertNotNull(mockBaseChatModel.getMessages());
        assertEquals(date, mockBaseChatModel.getCreated());
        assertEquals(date, mockBaseChatModel.getUpdated());
        assertEquals("TestID", mockBaseChatModel.getObjectId());
        assertEquals(messagesList, mockBaseChatModel.getMessages());
        List<BaseChatModel> baseChatModels = new ArrayList<>();
        baseChatModels.add(mockBaseChatModel);
    }

    @Test
    public void modelChatUser_isCorrect() throws Exception{
        ChatUser chatUser = new ChatUser();
        chatUser.setEmail("Test@Test");
        chatUser.setPassword("Test");
        chatUser.setDevice_id("Test");
        chatUser.setName("Test");
        chatUser.setOnline(false);
        chatUser.setPhoto("Test");
        assertNotNull(chatUser);
        assertNotNull(chatUser.getEmail());
        assertNotNull(chatUser.getDevice_id());
        assertNotNull(chatUser.getName());
        assertNotNull(chatUser.getOnline());
        assertNotNull(chatUser.getPassword());
        assertNotNull(chatUser.getPhoto());
        assertEquals("Test@Test", chatUser.getEmail());
        assertEquals("Test", chatUser.getPassword());
        assertEquals("Test", chatUser.getDevice_id());
        assertEquals("Test", chatUser.getName());
        assertEquals("Test", chatUser.getPhoto());
        assertEquals(false, chatUser.getOnline());
    }

    @Test
    public void modelFriends_isCorrect(){
        BackendlessUser user1 = mock(BackendlessUser.class);
        BackendlessUser user2 = mock(BackendlessUser.class);
        Messages message = mock(Messages.class);
        List<Messages> messages = new ArrayList<>();
        for (int i = 0; i<50 ; i++){
            messages.add(message);
        }
        Date date = new Date();
        Friends friends = new Friends();
        friends.setMessages(messages);
        friends.setUser_one(user1);
        friends.setUser_two(user2);
        friends.setSelected(false);
        friends.setUpdated(date);
        friends.setSubtopic("Test");
        assertNotNull(friends);
        assertNotNull(friends.getMessages());
        assertNotNull(friends.getUpdated());
        assertNotNull(friends.getSelected());
        assertNotNull(friends.getSubtopic());
        assertNotNull(friends.getUser_one());
        assertNotNull(friends.getUser_two());
        assertEquals(user1, friends.getUser_one());
        assertEquals(user2, friends.getUser_two());
        assertEquals(50, friends.getMessages().size());
        assertEquals(messages, friends.getMessages());
        assertEquals(date, friends.getUpdated());
        assertEquals(false, friends.getSelected());
        assertEquals("Test", friends.getSubtopic());
    }

    @Test
    public void modelGroupChat_isCorrect(){
        BackendlessUser owner = mock(BackendlessUser.class);
        List<BackendlessUser> users = new LinkedList<>();
        for (int i = 0; i<50; i++){
            users.add(owner);
        }
        Messages message = mock(Messages.class);
        List<Messages> messages = new LinkedList<>();
        for (int i = 0; i<50; i++){
            messages.add(message);
        }
        Date date = new Date();
        GroupChat groupChat = new GroupChat();
        groupChat.setOwner(owner);
        groupChat.setUsers(users);
        groupChat.setMessages(messages);
        groupChat.setChatName("Test");
        groupChat.setChanel("Test");
        groupChat.setUpdated(date);
        assertNotNull(groupChat);
        assertNotNull(groupChat.getOwner());
        assertNotNull(groupChat.getUsers());
        assertNotNull(groupChat.getMessages());
        assertNotNull(groupChat.getChanel());
        assertNotNull(groupChat.getChatName());
        assertNotNull(groupChat.getUpdated());
        assertEquals(owner, groupChat.getOwner());
        assertEquals(50, groupChat.getUsers().size());
        assertEquals(50, groupChat.getMessages().size());
        assertEquals("Test", groupChat.getChanel());
        assertEquals("Test", groupChat.getChatName());
        assertEquals(date, groupChat.getUpdated());
    }
}