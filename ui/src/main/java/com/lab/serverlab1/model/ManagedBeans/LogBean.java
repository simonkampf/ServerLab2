package com.lab.serverlab1.model.ManagedBeans;

import com.lab.serverlab1.model.BLL.BllHandler;
import com.lab.serverlab1.model.BLL.PostInfo;
import com.lab.serverlab1.model.BLL.UserInfo;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SessionScoped
@ManagedBean
public class LogBean implements Serializable {
    private List<PostInfo> posts;
    private UserInfo userInfo;
    private List<PostInfo> privatePosts;

    public LogBean(){
        posts = new ArrayList<>();
        userInfo = new UserInfo();
    }

    public List<PostInfo> getPosts() {
        return posts;
    }

    public List<PostInfo> getPrivatePosts() {
        return privatePosts;
    }
    public void populatePrivatePosts(String userNameToView){
        userInfo = BllHandler.getUserByUsername(userNameToView);
        posts = BllHandler.getAllPrivatePostsByUser(userInfo);
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void populate(String userNameToView){
        userInfo = BllHandler.getUserByUsername(userNameToView);
        posts = BllHandler.getAllPublicPostsByUser(userInfo);
    }
}
