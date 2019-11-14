package com.lab.serverlab1.model.BLL;

import com.lab.serverlab1.model.DAL.DBLogic;
import com.lab.serverlab1.model.DAL.TUserEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BllHandlerTest {

    @org.junit.jupiter.api.Test
    void checkCredentials() {
        String inputUsername = "alex";
        String inputPassword = "hej";

        DBLogic DBLogicMock = mock(DBLogic.class);
        BllHandler.setDbLogic(DBLogicMock);

        TUserEntity testUser = new TUserEntity();s
        testUser.setUsername("alex");
        testUser.setPassword("hej");

        List<TUserEntity> testUserList = new ArrayList<>();
        testUserList.add(testUser);

        when(DBLogicMock.getUsers()).thenReturn(testUserList);

        assertEquals(BllHandler.checkCredentials(inputUsername, inputPassword), true);
    }
}