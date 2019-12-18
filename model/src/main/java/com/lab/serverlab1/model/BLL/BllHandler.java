package com.lab.serverlab1.model.BLL;

import com.lab.serverlab1.model.DAL.*;

import java.sql.Timestamp;

import java.util.*;

/**
 * Business logic layer
 */
public class BllHandler {
    public static DBLogic dbLogic = new DBLogic();

    //WHEN TESTING..
    //public static DBLogic dbLogic;

    /**
     * Validates the credentials of a user
     * @param username the username
     * @param password the password
     * @return boolean representing the result
     */
    public static boolean checkCredentials(String username, String password){
        List<TUserEntity> users = dbLogic.getUsers();
        for(TUserEntity t : users){
            System.out.println(t.getUsername() + ", " + username);
            if(t.getUsername().equals(username) && t.getPassword().equals(password)){
                System.out.println("Found match");
                TLoginEntity login = new TLoginEntity();
                login.settDate(new Timestamp(System.currentTimeMillis()));
                login.settUserId(t.getIdTUser());
                dbLogic.createNewLogin(login);
                return true;
            }
        }
        return false;
    }

    /**
     * Adds a new user to the database according to the parameters
     * @param username
     * @param password
     * @param name
     * @param age
     * @return boolean representing the outcome
     */
    
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

    /**
     * Get a user corresponding to the parameter
     * @param userNameToView
     * @return UserInfo if user was found, otherwise null
     */

    public static UserInfo getUserByUsername(String userNameToView) {
        TUserEntity user = dbLogic.getUserByUsername(userNameToView);
        return tUserEntityToUserInfo(user);
    }

    /**
     * Get all the public posts made by corresponding user
     * @param username the user to view
     * @return a list with posts, empty if none
     */
    public static List<PostInfo> getAllPublicPostsByUser(String username) {
        TUserEntity user = dbLogic.getUserByUsername(username);
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

    /**
     * Get all the public posts made by corresponding user
     * @param username the user to view
     * @return a list with posts, empty if none
     */
    public static List<PostInfo> getAllPrivatePostsByUser(String username) {
        TUserEntity user = dbLogic.getUserByUsername(username);
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

    /**
     * Get all usernames that starts with or equals the search string
     * @param searchName
     * @return
     */

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

    /**
     * Creates a new post with the parameters
     * @param senderName
     * @param receiverName
     * @param content
     * @param isPrivate
     * @return a boolean representing the result
     */
    public static boolean createNewPost(String senderName, String receiverName, String content, boolean isPrivate) {
        TPostEntity post = new TPostEntity();
        TUserEntity sender = dbLogic.getUserByUsername(senderName);
        TUserEntity receiver = dbLogic.getUserByUsername(receiverName);
        post.setSenderId(sender.getIdTUser());
        post.setReceiverId(receiver.getIdTUser());
        post.setMessage(content);
        post.setTime(new Timestamp(System.currentTimeMillis()));
        post.setPrivate((byte) (isPrivate ? 1 : 0 ));
        System.out.println("Byte value of isPrivate: " + (byte) (isPrivate? 1 : 0 ));
        try{
            dbLogic.createNewPost(post);
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static Integer [] getOneWeekLoginHistoryByUser(String userName){
        TUserEntity user = dbLogic.getUserByUsername(userName);
        List<TLoginEntity> result = dbLogic.getAllLoginHistoryByUser(user.getIdTUser());
        for(int i = 0; i < result.size(); i++){
            System.out.println("Login history for user: " + result.get(i).gettUserId() + result.get(i).gettDate());
        }
        return loginListToWeekArray(result);
    }

    private static Integer [] loginListToWeekArray(List<TLoginEntity> list){
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        Date today = new Date();
        Integer [] result = {
            0, 0, 0, 0, 0, 0, 0
        };

        Date [] dates = new Date[7];

        dates[0] = today;
        dates[1] = new Date(today.getTime() - (86400000 * 1));
        dates[2] = new Date(today.getTime() - (86400000 * 2));
        dates[3] = new Date(today.getTime() - (86400000 * 3));
        dates[4] = new Date(today.getTime() - (86400000 * 4));
        dates[5] = new Date(today.getTime() - (86400000 * 5));
        dates[6] = new Date(today.getTime() - (86400000 * 6));

        for(int i = 0; i < dates.length; i++){
            System.out.println(dates[i].toString());
        }
        for(TLoginEntity t: list){
            cal1.setTime(t.gettDate());
            for(int i = 0; i<dates.length; i++){
                cal2.setTime(dates[i]);
                if(cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) &&
                             cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)){
                    result[i]++;
                }
            }
        }

        return result;

    }

}
