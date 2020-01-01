package com.lab.serverlab1.model.ManagedBeans;

import com.lab.serverlab1.model.BLL.PostInfo;
import com.lab.serverlab1.model.BLL.RequestManager;
import com.lab.serverlab1.model.BLL.UserInfo;
import com.mysql.cj.Session;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Base64;
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
    public boolean isLoggedIn = false;
    public String diagramType;
    private Part uploadFile;

    public List<SimilarImage> getSimilarImages() {
        return similarImages;
    }

    public void setSimilarImages(List<SimilarImage> similarImages) {
        this.similarImages = similarImages;
    }

    private List<SimilarImage> similarImages;
    public Part getUploadFile() {
        return uploadFile;
    }

    public void setUploadFile(Part uploadFile) {
        this.uploadFile = uploadFile;
    }


    public void handleImage(){

        System.out.println(uploadFile.getSubmittedFileName() + ", " +  uploadFile.getSize());
        String fileName = uploadFile.getSubmittedFileName();
        String fileEnding = fileName.substring(fileName.lastIndexOf('.') + 1);
        if(!fileEnding.equals("jpg")){

            return;
        }
        try (InputStream input = uploadFile.getInputStream()) {
            byte [] result = new byte[(int)uploadFile.getSize()];
            input.read(result);

            String encodedString = Base64.getEncoder().encodeToString(result);
            similarImages = RequestManager.postImageForComparison(encodedString);

        }catch(IOException e){
            e.printStackTrace();
        }

    }

    public String getDiagramType() {
        return diagramType;
    }

    public void setDiagramType(String diagramType) {
        this.diagramType = diagramType;
    }

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
        return isLoggedIn;
    }

    public String getDiagramUrl(){
        String numbersToPlot = RequestManager.getLoginHistoryToPlot(userInfo, userNameToView);
        String url = "http://localhost:8091/showData?values=" + numbersToPlot + "&plotType=";
        if(diagramType == null || diagramType.equals("")){
            url += "pie";

        }else{
            url += diagramType;
        }
        System.out.println("Diagram url: " + url);
        return url;
    }
    /**
     * Confirms credentials for current user
     * @return a String corresponding to the success or failure of the check
     */
    public String checkCredentials(){
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
        if(RequestManager.checkCredentials(userInfo.getUsername(), userInfo.getPassword())){
            isLoggedIn = true;
            userNameToView = userInfo.getUsername();
            return "Success";
        }
        return "Failure";
    }

    /**
     * Sets the userInfo to be displayed in a log
     */
    public void setUserInfoLog(){
        userInfoLog = RequestManager.getUserByUsername(userNameToView);
    }

    /**
     * returns a list of all usernames for the current search string (searchName)
     * @return
     */
    public List<String> getUserNames() {
        userNames = RequestManager.getUsernamesByLetters(userInfo, searchName);
        return userNames;
    }


    public void setUserNames(List<String> userNames) {
        this.userNames = userNames;
    }

    public String getUserName(){
        return userInfo.getUsername();
    }
    public String logOut(){
        isLoggedIn = false;
        userNameToView = "";
        return "LogOut";
    }
    /*
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
    }*/

    /**
     * Sets current user to be the username to view
     * @return "Log"
     */
    public String showOwnLog(){
        userNameToView = userInfo.getUsername();
        getDiagramUrl();
        return "Log";
    }

    /**
     * Sets the username to view for log
     * @param userNameToView
     * @return "Log"
     */
    public String showUserLog(String userNameToView){
        this.userNameToView = userNameToView;
        return "Log";
    }

    /**
     * Sets up to create a new post
     * @return
     */
    public String createNewPost(){
        this.receiverName = userNameToView;
        return "SendMessage";
    }

    /**
     * Send a new post corresponding to the given information
     * @return "log"
     */
    public String sendNewPost(){
        System.out.println("sender: " + userInfo.getUsername() + ", receiver: " + receiverName + ", content: " + newPostContent + ", send private:" + sendPrivate);
        RequestManager.createNewPost(userInfo, new PostInfo(userInfo.getUsername(), receiverName, newPostContent, new Date(), sendPrivate));
        return "log";
    }

    /**
     * Confirms that a new user has been created
     * @return a boolean representing the Success or Failure of the operation
     */
    public String confirmNewUser(){
        boolean result = RequestManager.addNewUser(userInfo.getUsername(), userInfo.getPassword(), userInfo.getName(), userInfo.getAge());
        if(result == true){
            return "Success";
        }
        return "Failure";
    }
}
