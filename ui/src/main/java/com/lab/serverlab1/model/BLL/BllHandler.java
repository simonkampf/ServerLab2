package com.lab.serverlab1.model.BLL;

import com.lab.serverlab1.model.DAL.DBLogic;
import com.lab.serverlab1.model.DAL.TPostEntity;
import com.lab.serverlab1.model.DAL.TUserEntity;
import com.lab.serverlab1.model.DAL.TUserpostEntity;
import com.lab.serverlab1.model.ManagedBeans.UserBean;
import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.List;

public class BllHandler {
    private static DBLogic dbLogic = new DBLogic();
    public static void main(String [] args){
        UserInfo user = getUserByUsername("simon");
        List<PostInfo> posts = getAllPublicPostsByUser(user);
        System.out.println("Printing user: " + user.getUsername() + " posts");
        for(PostInfo p : posts){
            System.out.println(p.toString());
        }
        System.out.println();
    }


    public static boolean checkCredentials(String username, String password){
        List<TUserEntity> users = dbLogic.getUsers();
        for(TUserEntity t : users){
            System.out.println(t.getUsername() + ", " + username);
            if(t.getUsername().equals(username) && t.getPassword().equals(password)){
                return true;
            }
        }
        return false;
    }
    
    public static boolean addNewUser(String username, String password, String name, int age){
        TUserEntity user = new TUserEntity();
        user.setUsername(username);
        user.setAge(age);
        user.setName(name);
        user.setPassword(password);
        try{
            dbLogic.addUser(user);
        }catch(Exception e){
            System.out.println("Adding new user failed");
            return false;
        }
        return true;
    }

    public static UserInfo getUserByUsername(String userNameToView) {
        TUserEntity user = dbLogic.getUserByUsername(userNameToView);
        return tUserEntityToUserInfo(user);
    }

    public static List<PostInfo> getAllPublicPostsByUser(UserInfo userInfo) {
        TUserEntity user = dbLogic.getUserByUsername(userInfo.getUsername());
        List<TPostEntity> posts = dbLogic.getAllPostsByUser(user);
        List<TUserEntity> users = dbLogic.getUsers();
        List<PostInfo> postInfos = new ArrayList<>();
        for(TPostEntity p : posts){
            if (!booleanFromByte(p.getPrivate())) {
                PostInfo postInfo = new PostInfo();
                for (TUserEntity u : users) {
                    if (p.getReceiverId() == u.getIdTUser()) {
                        postInfo.setReceiverUsername(u.getUsername());
                    }
                    if (p.getSenderId() == u.getIdTUser()) {
                        postInfo.setSenderUsername(u.getUsername());
                    }
                }
                postInfo.setDate(p.getTime());
                postInfo.setContent(p.getMessage());
                postInfos.add(postInfo);
                System.out.println(postInfo.toString());
            }
        }

        return postInfos;
    }
    public static List<PostInfo> getAllPrivatePostsByUser(UserInfo userInfo) {
        TUserEntity user = dbLogic.getUserByUsername(userInfo.getUsername());
        List<TPostEntity> posts = dbLogic.getAllPostsByUser(user);
        List<TUserEntity> users = dbLogic.getUsers();
        List<PostInfo> postInfos = new ArrayList<>();
        for(TPostEntity p : posts){
            if (booleanFromByte(p.getPrivate())) {
                PostInfo postInfo = new PostInfo();
                for (TUserEntity u : users) {
                    if (p.getReceiverId() == u.getIdTUser()) {
                        postInfo.setReceiverUsername(u.getUsername());
                        for(TUserEntity sender : users){
                            if(sender.getIdTUser() == p.getSenderId()){
                                postInfo.setSenderUsername(sender.getUsername());
                                break;
                            }
                        }
                        break;
                    }
                }
                postInfo.setDate(p.getTime());
                postInfo.setContent(p.getMessage());
                postInfos.add(postInfo);
                System.out.println(postInfo.toString());
            }
        }

        return postInfos;
    }


    private static UserInfo tUserEntityToUserInfo(TUserEntity tUserEntity){
        return new UserInfo(tUserEntity.getUsername(),
                "", tUserEntity.getName(), tUserEntity.getAge());
    }

    public static List<String> getUsernamesByLetters(String searchName) {
        if(searchName.length() == 0){
            return new ArrayList<String>();
        }
        List<TUserEntity> users = dbLogic.getUsers();
        List<String> usernames = new ArrayList<>();
        for(TUserEntity u : users){
            if(u.getUsername().toLowerCase().startsWith(searchName.toLowerCase())){
                usernames.add(u.getUsername());
            }
        }

        return usernames;

    }

    private static boolean booleanFromByte(Byte b){
        return b!=0;
    }
    public static void createNewPost(PostInfo postInfo) {
        TPostEntity post = new TPostEntity();
        TUserEntity sender = dbLogic.getUserByUsername(postInfo.getSenderUsername());
        TUserEntity receiver = dbLogic.getUserByUsername(postInfo.getReceiverUsername());
        post.setSenderId(sender.getIdTUser());
        post.setReceiverId(receiver.getIdTUser());
        post.setMessage(postInfo.getContent());
        post.setTime(new Timestamp(postInfo.getDate().getTime()));
        post.setPrivate((byte) (postInfo.isPrivate() ? 1 : 0 ));
        System.out.println("Byte value of isPrivate: " + (byte) (postInfo.isPrivate() ? 1 : 0 ));
        try{
            dbLogic.createNewPost(post);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
