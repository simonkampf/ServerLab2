package com.lab.serverlab1.model.ManagedBeans;

import com.lab.serverlab1.model.BLL.BllHandler;
import com.lab.serverlab1.model.BLL.PostInfo;
import com.lab.serverlab1.model.BLL.UserInfo;
import com.mysql.cj.Session;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SessionScoped
@ManagedBean
public class UserBean implements Serializable {
    private UserInfo userInfo;
    private String userNameToView;
    private String searchName = "";
    private List<String> userNames = new ArrayList<>();
    private String receiverName;
    private boolean sendPrivate;
    private String newPostContent;
    private UserInfo userInfoLog;

    public String getNewPostContent() {
        return newPostContent;
    }

    public void setNewPostContent(String newPostContent) {
        this.newPostContent = newPostContent;
    }
    public UserInfo getUserInfoLog(){
        return userInfoLog;
    }
    public boolean isSendPrivate() {
        return sendPrivate;
    }

    public void setSendPrivate(boolean sendPrivate) {
        this.sendPrivate = sendPrivate;
    }

    public UserBean(){
        userInfo = new UserInfo();
        userNameToView = userInfo.getUsername();
    }
    public String getUsername(){
        return userInfo.getUsername();
    }
    public String getPassword(){
        return userInfo.getPassword();
    }
    public String getUserNameToView(){
        return userNameToView;
    }
    public UserInfo getUserInfo(){
        return userInfo;
    }

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public boolean isLoggedIn(){
        String res = checkCookie();
        System.out.println("cookie result:" + res);
        if(res.equals("")) return false;
        userInfo.setUsername(res);
        return true;
    }
    public String checkCredentials(){
        System.out.println("Get details");
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
        if(BllHandler.checkCredentials(userInfo.getUsername(), userInfo.getPassword())){
            Cookie usernameCookie = new Cookie("username", userInfo.getUsername());
            usernameCookie.setMaxAge(3600);
            response.addCookie(usernameCookie);
            userNameToView = userInfo.getUsername();
            return "Success";
        }
        return "Failure";
    }
    public void setUserInfoLog(){
        userInfoLog = BllHandler.getUserByUsername(userNameToView);
    }
    public List<String> getUserNames() {
        System.out.println("I getusernames");
        userNames = BllHandler.getUsernamesByLetters(searchName);
        return userNames;
    }

    public void setUserNames(List<String> userNames) {
        this.userNames = userNames;
    }

    public String getUserName(){
        return userInfo.getUsername();
    }
    public String logOut(){
        System.out.println("log out");
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        for(Cookie c : request.getCookies()){
            if(c.getName().equalsIgnoreCase("username")){
                c.setMaxAge(0);
                response.addCookie(c);
            }
        }
        userNameToView = "";
        return "LogOut";
    }

    public String checkCookie(){
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        String username = "";
        Cookie cookies[] = request.getCookies();
        if(cookies != null){
            for(Cookie c : cookies){
                if(c.getName().equalsIgnoreCase("username")){
                    username = c.getValue();
                }
            }
        }
        return username;
    }

    public String showOwnLog(){
        userNameToView = userInfo.getUsername();
        return "Log";
    }

    public String showUserLog(String userNameToView){
        this.userNameToView = userNameToView;
        return "Log";
    }

    public String createNewPost(){
        this.receiverName = userNameToView;
        return "SendMessage";
    }

    public String sendNewPost(){
        System.out.println("sender: " + userInfo.getUsername() + ", receiver: " + receiverName + ", content: " + newPostContent + ", send private:" + sendPrivate);
        BllHandler.createNewPost(new PostInfo(userInfo.getUsername(), receiverName, newPostContent, new Date(), sendPrivate));
        return "log";
    }

    public String confirmNewUser(){
        boolean result = BllHandler.addNewUser(userInfo.getUsername(), userInfo.getPassword(), userInfo.getName(), userInfo.getAge());
        if(result == true){
            return "Success";
        }
        return "Failure";
    }
}
