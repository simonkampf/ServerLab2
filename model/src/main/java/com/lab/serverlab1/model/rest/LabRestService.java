package com.lab.serverlab1.model.rest;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lab.serverlab1.model.BLL.BllHandler;
import com.lab.serverlab1.model.BLL.PostInfo;
import com.lab.serverlab1.model.BLL.UserInfo;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Path("/")
public class LabRestService {
    private Gson gson = new Gson();
    private boolean isAuthenticated = false;

    /**
     * Returns a UserInfo corresponding to the given username
     * Returns 200 if successful
     * @param username
     * @return
     */
    @GET
    @Path("/getUserByUsername")
    public Response getUserByUsername(@QueryParam("username") String username) {
        System.out.println("isAuthenticated: " + isAuthenticated);
        UserInfo user = BllHandler.getUserByUsername(username);
        String json = gson.toJson(user);

        return Response.status(200).entity(json).build();
    }


    @GET
    @Path("/goodbye")
    public Response goodbye() {
        return Response.status(200).entity("goodbye").build();
    }

    /**
     * Returns a json document containing a list with the given password
     * Requires user to be logged in with credentials in header
     * @param username
     * @param password
     * @param userToView
     * @return 200 if successful, 401 if unauthorized
     */
    @GET
    @Path("/getAllPublicPostsByUser")
    public Response getAllPublicPostsByUser(@HeaderParam("username") String username, @HeaderParam("password") String password,
                                            @QueryParam("username") String userToView){
        if(BllHandler.checkCredentials(username, password)){
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        List<PostInfo> posts = BllHandler.getAllPublicPostsByUser(userToView);
        Type listType = new TypeToken<List<PostInfo>>() {}.getType();
        String json = gson.toJson(posts, listType);
        return Response.status(200).entity(json).build();
    }


    /**
     * Returns all private posts to the logged in user
     * Requires User to be logged in and credentials
     * Credentials are checked for the parameter privateUser and password
     * @param username
     * @param password
     * @param privateUser
     * @return
     */
    @GET
    @Path("/getAllPrivatePostsByUser")
    public Response getAllPrivatePostsByUser(@HeaderParam("username") String username, @HeaderParam("password") String password,
            @QueryParam("username") String privateUser){
        if(BllHandler.checkCredentials(privateUser, password)){
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        List<PostInfo> posts = BllHandler.getAllPrivatePostsByUser(privateUser);
        Type listType = new TypeToken<List<PostInfo>>() {}.getType();
        String json = gson.toJson(posts, listType);
        return Response.status(200).entity(json).build();
    }

    /**
     * Returns a boolean corresponding to the result
     * Response status 200 if successful, 401 otherwise
     * @param username
     * @param password
     * @return
     */
    @GET // This annotation indicates GET request
    @Path("/checkCredentials")
    public Response checkCredentials(@QueryParam("username") String username, @QueryParam("password") String password) {
        boolean result = BllHandler.checkLogin(username, password);
        if(result == true){
            String json = gson.toJson(result);
            isAuthenticated = true;
            return Response.status(200).entity(json).build();
        }else{
            return Response.status(401).entity(result + ": Not authorized").build();
        }
    }

    /**
     * Adds a user with given parameters
     * Returns 201 if successful
     * Returns 400 if bad request
     * @param username
     * @param password
     * @param name
     * @param age
     * @return
     */
    @POST
    @Path("/addNewUser")
    public Response addNewUser(@QueryParam("username") String username, @QueryParam("password") String password,
                                      @QueryParam("name") String name, @QueryParam("age") int age) {
        boolean result = BllHandler.addNewUser(username, password, name, age);
        if(result == true){
            return Response.status(Response.Status.CREATED).build();
        }else{
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

    }

    /**
     * Returns a list of all usernames that starts with and/or equals the given letters
     * @param username
     * @param password
     * @param letters
     * @return
     */
    @GET
    @Path("/getUsernamesByLetters")
    public Response getUsernamesByLetters(@HeaderParam("username") String username, @HeaderParam("password") String password,
                                          @QueryParam("letters") String letters){
        if(!BllHandler.checkCredentials(username, password)){
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        List<String> users = BllHandler.getUsernamesByLetters(letters);
        Type listType = new TypeToken<List<String>>() {}.getType();
        String json = gson.toJson(users, listType);
        return Response.status(200).entity(json).build();

    }

    /**
     * Adds a new post with the given parameters
     * Returns 201 if successful
     * Returns 400 if bad request
     * @param username
     * @param password
     * @param sender
     * @param receiver
     * @param content
     * @param isPrivate
     * @return
     */
    @POST
    @Path("/createNewPost")
    public Response createNewPost(@HeaderParam("username") String username, @HeaderParam("password") String password,
                                 @QueryParam("sender") String sender, @QueryParam("receiver") String receiver,
                               @QueryParam("content") String content, @QueryParam("private") boolean isPrivate) {
        System.out.println("is private: " + isPrivate);
        if(!BllHandler.checkCredentials(username, password)){
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        boolean result = BllHandler.createNewPost(sender, receiver, content, isPrivate);
        if(result == true){
            return Response.status(Response.Status.CREATED).build();
        }else{
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

    }

    @GET
    @Path("/getLoginHistory")
    public Response getLoginHistory(@HeaderParam("username") String username, @HeaderParam("password") String password,
                                            @QueryParam("username") String userToView){
        System.out.println("login history: " + username + ", " + password);
        if(!BllHandler.checkCredentials(username, password)){
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        Integer[] result = BllHandler.getOneWeekLoginHistoryByUser(userToView);
        Type listType = new TypeToken<Integer[]>() {}.getType();
        String json = gson.toJson(result, listType);
        return Response.status(200).entity(json).build();
    }


}

