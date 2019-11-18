package com.lab.serverlab1.model.BLL;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import com.lab.serverlab1.model.DAL.DBLogic;
import com.lab.serverlab1.model.DAL.TPostEntity;
import com.lab.serverlab1.model.DAL.TUserEntity;
import org.junit.*;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;

public class BllHandlerTest {


    @Mock
    DBLogic DBLogicMock;

    @Before
    public void setUp(){
        DBLogicMock = mock(DBLogic.class);
        BllHandler.dbLogic = DBLogicMock;
    }

    @Test
    public void checkCredentialsTrue()  {

        String inputUsername = "alex";
        String inputPassword = "hej";

        TUserEntity testUser = new TUserEntity();
        testUser.setUsername("alex");
        testUser.setPassword("hej");

        List<TUserEntity> testUserList = new ArrayList<>();
        testUserList.add(testUser);

        when(DBLogicMock.getUsers()).thenReturn(testUserList);

        assertEquals(BllHandler.checkCredentials(inputUsername, inputPassword), true);
    }

    @Test
    public void checkCredentialsFalse()  {

        String inputUsername = "alex";
        String inputPassword = "hej";

        TUserEntity testUser = new TUserEntity();
        testUser.setUsername("alex");
        testUser.setPassword("hje");

        List<TUserEntity> testUserList = new ArrayList<>();
        testUserList.add(testUser);

        when(DBLogicMock.getUsers()).thenReturn(testUserList);

        assertEquals(BllHandler.checkCredentials(inputUsername, inputPassword), false);
    }

    @Test
    public void addNewUserFail() {
        String inputUsername = "alex";
        String inputPassword = null;
        int inputAge = 50;
        String inputName = "Alex Evert";

        TUserEntity testUser = new TUserEntity();
        testUser.setUsername(inputUsername);
        testUser.setAge(inputAge);
        testUser.setName(inputName);
        testUser.setPassword(inputPassword);

        if(testUser.getUsername() == null || testUser.getPassword() == null
                || testUser.getName() == null || (Integer) testUser.getAge() == null){
            doThrow(Exception.class).when(DBLogicMock).addUser(testUser);
        }

        assertEquals(BllHandler.addNewUser(inputUsername, inputPassword, inputName, inputAge), false);

    }


    @Test
    public void addNewUserPass() {
        String inputUsername = "alex";
        String inputPassword = "hej";
        int inputAge = 50;
        String inputName = "Alex Evert";

        TUserEntity testUser = new TUserEntity();
        testUser.setUsername(inputUsername);
        testUser.setAge(inputAge);
        testUser.setName(inputName);
        testUser.setPassword(inputPassword);

        if(testUser.getUsername() == null || testUser.getPassword() == null
                || testUser.getName() == null || (Integer) testUser.getAge() == null){
            doThrow(Exception.class).when(DBLogicMock).addUser(testUser);
        }

        assertEquals(BllHandler.addNewUser(inputUsername, inputPassword, inputName, inputAge), true);

    }

    @Test
    public void getAllPublicPostsByUser(){

        String inputUsername = "alex";
        List<TUserEntity> users = new ArrayList<TUserEntity>();
        List<TPostEntity> posts = new ArrayList<TPostEntity>();
        List<PostInfo> expectedPosts = new ArrayList();

        TUserEntity testUser = new TUserEntity();
        testUser.setIdTUser(1);
        testUser.setUsername(inputUsername);
        testUser.setAge(66);
        testUser.setName("Alex Evert");
        testUser.setPassword("hej");

        users.add(testUser);

        TPostEntity testPost1 = new TPostEntity();
        testPost1.setMessage("public");
        testPost1.setPrivate((byte) (false ? 1 : 0 ));
        testPost1.setReceiverId(1);
        testPost1.setSenderId(1);
        testPost1.setTime(new Timestamp(System.currentTimeMillis()));

        TPostEntity testPost2 = new TPostEntity();
        testPost2.setMessage("private");
        testPost2.setPrivate((byte) (true ? 1 : 0 ));
        testPost2.setReceiverId(1);
        testPost2.setSenderId(1);
        testPost2.setTime(new Timestamp(System.currentTimeMillis()));

        posts.add(testPost1);
        posts.add(testPost2);

        PostInfo expectedPost = new PostInfo("alex", "alex", "public", testPost1.getTime(), false);

        expectedPosts.add(expectedPost);

        when(DBLogicMock.getUserByUsername(inputUsername)).thenReturn(testUser);
        when(DBLogicMock.getAllPostsByUser(testUser)).thenReturn(posts);
        when(DBLogicMock.getUsers()).thenReturn(users);

        assertEquals(BllHandler.getAllPublicPostsByUser(inputUsername).get(0).getContent(), expectedPosts.get(0).getContent());

    }

    @Test
    public void getAllPrivatePostsByUser(){

        String inputUsername = "alex";
        List<TUserEntity> users = new ArrayList<TUserEntity>();
        List<TPostEntity> posts = new ArrayList<TPostEntity>();
        List<PostInfo> expectedPosts = new ArrayList();

        TUserEntity testUser = new TUserEntity();
        testUser.setIdTUser(1);
        testUser.setUsername(inputUsername);
        testUser.setAge(66);
        testUser.setName("Alex Evert");
        testUser.setPassword("hej");

        users.add(testUser);

        TPostEntity testPost1 = new TPostEntity();
        testPost1.setMessage("public");
        testPost1.setPrivate((byte) (false ? 1 : 0 ));
        testPost1.setReceiverId(1);
        testPost1.setSenderId(1);
        testPost1.setTime(new Timestamp(System.currentTimeMillis()));

        TPostEntity testPost2 = new TPostEntity();
        testPost2.setMessage("private");
        testPost2.setPrivate((byte) (true ? 1 : 0 ));
        testPost2.setReceiverId(1);
        testPost2.setSenderId(1);
        testPost2.setTime(new Timestamp(System.currentTimeMillis()));

        posts.add(testPost1);
        posts.add(testPost2);

        PostInfo expectedPost = new PostInfo("alex", "alex", "private", testPost2.getTime(), true);

        expectedPosts.add(expectedPost);

        System.out.println("EXPECTED: " + expectedPosts.get(0).getContent());

        when(DBLogicMock.getUserByUsername(inputUsername)).thenReturn(testUser);
        when(DBLogicMock.getAllPostsByUser(testUser)).thenReturn(posts);
        when(DBLogicMock.getUsers()).thenReturn(users);

        assertEquals(BllHandler.getAllPrivatePostsByUser(inputUsername).get(0).getContent(), expectedPosts.get(0).getContent());

    }

    



}