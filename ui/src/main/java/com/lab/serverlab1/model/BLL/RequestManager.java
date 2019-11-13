package com.lab.serverlab1.model.BLL;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class RequestManager {
    private static CloseableHttpClient httpClient = HttpClients.createDefault();
    private static Gson gson = new Gson();
    private static String url = "http://model:8080/Model/";
    public RequestManager(){
    }
    public static boolean checkCredentials(String username, String password){

        HttpGet req = new HttpGet(url + "checkCredentials?username=" + username +
                                  "&password=" + password);
        System.out.println("Sending req: " + req.getURI());
        try{
            CloseableHttpResponse httpResponse = httpClient.execute(req);

            try{
                System.out.println(httpResponse.getStatusLine());
                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                System.out.println("result checkCredentials: " + result);
                if(result.equals("true")){
                    return true;
                }else{
                    return false;
                }
            }finally{
                httpResponse.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public static boolean addNewUser(String username, String password, String name, int age) {
        name = name.replace(" ", "%20");
        HttpPost req = new HttpPost(url + "addNewUser?username=" + username +
                "&password=" + password + "&name=" + name + "&age=" + age);
        System.out.println("Sending req: " + req.getURI());
        try{
            CloseableHttpResponse httpResponse = httpClient.execute(req);

            try{
                int statusCode = httpResponse.getStatusLine().getStatusCode();
                if(statusCode!= 201){
                    System.out.println("addNewUser failed");
                    return false;
                }
                System.out.println(httpResponse.getStatusLine());
                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
            }finally{
                httpResponse.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return true;

    }

    public static UserInfo getUserByUsername(String userNameToView) {
        HttpGet req = new HttpGet(url + "getUserByUsername?username=" + userNameToView);
        System.out.println("Sending req: " + req.getURI());
        UserInfo user = null;
        try{
            CloseableHttpResponse httpResponse = httpClient.execute(req);

            try{
                Gson gson = new Gson();
                System.out.println(httpResponse.getStatusLine());
                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                System.out.println("result checkCredentials: " + result);

                user = gson.fromJson(result, UserInfo.class);

                System.out.println(user.getUsername() + " "
                        +user.getName() + " " + user.getAge());

            }finally{
                httpResponse.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        return user;

    }
    public static List<PostInfo> getAllPublicPostsByUser(UserInfo currentUser, UserInfo userInfo) {
        HttpGet req = new HttpGet(url + "getAllPublicPostsByUser?username=" + userInfo.getUsername());
        System.out.println("Sending req: " + req.getURI());
        req.addHeader("username", currentUser.getUsername());
        req.addHeader("password", currentUser.getPassword());
        List<PostInfo> target = new ArrayList<>();
        try{
            CloseableHttpResponse httpResponse = httpClient.execute(req);

            try{
                System.out.println(httpResponse.getStatusLine());
                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                if(httpResponse.getStatusLine().getStatusCode() != 200){
                    System.out.println("Could not request getAllPublicPostsByUser: " +  httpResponse.getStatusLine());
                    return new ArrayList<PostInfo>();
                }
                Type listType = new TypeToken<List<PostInfo>>() {}.getType();
                target = gson.fromJson(result, listType);
                return target;

            }finally{
                httpResponse.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{

        }
        return target;
    }

    public static List<PostInfo> getAllPrivatePostsByUser(UserInfo currentUser, UserInfo userInfo) {
        HttpGet req = new HttpGet(url + "getAllPrivatePostsByUser?username=" + userInfo.getUsername());
        System.out.println("Sending req: " + req.getURI());
        req.addHeader("username", currentUser.getUsername());
        req.addHeader("password", currentUser.getPassword());
        List<PostInfo> target = new ArrayList<>();
        try{
            CloseableHttpResponse httpResponse = httpClient.execute(req);

            try{
                System.out.println(httpResponse.getStatusLine());
                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                if(httpResponse.getStatusLine().getStatusCode() != 200){
                    System.out.println("Could not request getAllPrivatePostsByUser: " +  httpResponse.getStatusLine());
                    return new ArrayList<PostInfo>();
                }
                Type listType = new TypeToken<List<PostInfo>>() {}.getType();
                target = gson.fromJson(result, listType);
                return target;

            }finally{
                httpResponse.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{

        }
        return target;
    }

    public static boolean createNewPost(UserInfo currentUser, PostInfo postInfo) {
        String content = postInfo.getContent();
        content = content.replace(" ",  "%20");
        System.out.println("Content: " + content);
        HttpPost req = new HttpPost(url + "createNewPost?sender=" + postInfo.getSenderUsername() +
                "&receiver=" + postInfo.getReceiverUsername() + "&content=" + content +
                "&private=" + postInfo.isPrivate());
        System.out.println("postinfo private: " + postInfo.isPrivate());
        req.addHeader("username", currentUser.getUsername());
        req.addHeader("password", currentUser.getPassword());
        System.out.println("Sending req: " + req.getURI());
        try{
            CloseableHttpResponse httpResponse = httpClient.execute(req);

            try{
                int statusCode = httpResponse.getStatusLine().getStatusCode();
                if(statusCode!= 201){
                    return false;
                }
                System.out.println(httpResponse.getStatusLine());

            }finally{
                httpResponse.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return true;
    }

    public static List<String> getUsernamesByLetters(UserInfo currentUser, String searchName) {
        HttpGet req = new HttpGet(url + "getUsernamesByLetters?letters=" + searchName);
        req.addHeader("username", currentUser.getUsername());
        req.addHeader("password", currentUser.getPassword());
        System.out.println("Sending req: " + req.getURI());
        List<String> target = new ArrayList<>();
        try{
            CloseableHttpResponse httpResponse = httpClient.execute(req);

            try{
                System.out.println(httpResponse.getStatusLine());
                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                if(httpResponse.getStatusLine().getStatusCode() != 200){
                    System.out.println("Could not request getUsernamesByLetters: " +  httpResponse.getStatusLine());
                    return new ArrayList<String>();
                }
                Type listType = new TypeToken<List<String>>() {}.getType();
                target = gson.fromJson(result, listType);
                return target;

            }finally{
                httpResponse.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{

        }
        return target;

    }

}
