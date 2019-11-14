package com.lab.serverlab1.model.ManagedBeans;

import com.lab.serverlab1.model.BLL.PostInfo;
import com.lab.serverlab1.model.BLL.RequestManager;
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

    /**
     * Populates the current users inbox
     * @param userNameToView
     */
    public void populatePrivatePosts(String userNameToView){
        userInfo = RequestManager.getUserByUsername(userNameToView);
        posts = RequestManager.getAllPrivatePostsByUser(userInfo, userInfo);
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    /**
     * Populates the user to view's log
     * @param userNameToView
     */
    public void populate(String userNameToView){
        userInfo = RequestManager.getUserByUsername(userNameToView);
        System.out.println("Userinfo from populate: " + userInfo.getUsername() + ", " + userInfo.getName() );
        posts = RequestManager.getAllPublicPostsByUser(userInfo, userInfo);
    }
}
