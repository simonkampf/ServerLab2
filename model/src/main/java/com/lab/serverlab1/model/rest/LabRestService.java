package com.lab.serverlab1.model.rest;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lab.serverlab1.model.BLL.BllHandler;
import com.lab.serverlab1.model.BLL.PostInfo;
import com.lab.serverlab1.model.BLL.UserInfo;

import java.lang.reflect.Type;
import java.util.List;

@Path("/")
public class LabRestService {
    private Gson gson = new Gson();
    private boolean isAuthenticated = false;
    @GET // This annotation indicates GET request
    @Path("/getUserByUsername")
    public Response getUserByUsername(@QueryParam("username") String username) {
        System.out.println("isAuthenticated: " + isAuthenticated);
        Gson gson = new Gson();
        UserInfo user = BllHandler.getUserByUsername(username);
        String json = gson.toJson(user);

        return Response.status(200).entity(json).build();
    }

    @GET
    @Path("/goodbye")
    public Response goodbye() {
        return Response.status(200).entity("goodbye").build();
    }
    @GET
    @Path("/getAllPublicPostsByUser")
    public Response getAllPublicPostsByUser(@QueryParam("username") String username){
        List<PostInfo> posts = BllHandler.getAllPublicPostsByUser(username);
        Type listType = new TypeToken<List<PostInfo>>() {}.getType();
        String json = gson.toJson(posts, listType);
        return Response.status(200).entity(json).build();
    }
    @GET
    @Path("/getAllPrivatePostsByUser")
    public Response getAllPrivatePostsByUser(@QueryParam("username") String username){

        List<PostInfo> posts = BllHandler.getAllPrivatePostsByUser(username);
        Type listType = new TypeToken<List<PostInfo>>() {}.getType();
        String json = gson.toJson(posts, listType);
        return Response.status(200).entity(json).build();
    }
    @GET // This annotation indicates GET request
    @Path("/checkCredentials")
    public Response checkCredentials(@QueryParam("username") String username, @QueryParam("password") String password) {
        Gson gson = new Gson();
        boolean result = BllHandler.checkCredentials(username, password);
        if(result == true){
            String json = gson.toJson(result);
            isAuthenticated = true;
            return Response.status(200).entity(json).build();
        }else{
            return Response.status(401).entity(result + ": Not authorized").build();
        }
    }
    @POST
    @Path("/addNewUser")
    public Response addNewUser(@QueryParam("username") String username, @QueryParam("password") String password,
                                      @QueryParam("name") String name, @QueryParam("age") int age) {
        Gson gson = new Gson();
        boolean result = BllHandler.addNewUser(username, password, name, age);
        if(result == true){
            return Response.status(Response.Status.CREATED).build();
        }else{
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

    }

    @GET
    @Path("/getUsernamesByLetters")
    public Response getUsernamesByLetters(@QueryParam("letters") String letters){
        List<String> users = BllHandler.getUsernamesByLetters(letters);
        Type listType = new TypeToken<List<String>>() {}.getType();
        String json = gson.toJson(users, listType);
        return Response.status(200).entity(json).build();

    }

    @POST
    @Path("/createNewPost")
    public Response createNewPost(@QueryParam("sender") String sender, @QueryParam("receiver") String receiver,
                               @QueryParam("content") String content, @QueryParam("private") boolean isPrivate) {
        boolean result = BllHandler.createNewPost(sender, receiver, content, isPrivate);
        if(result == true){
            return Response.status(Response.Status.CREATED).build();
        }else{
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

    }



}

