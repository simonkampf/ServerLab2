package com.lab.serverlab1.model.BLL;

import java.util.ArrayList;
import java.util.List;
import com.lab.serverlab1.model.DAL.DBLogic;
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
    public void addNewUserPass(){


    }

}