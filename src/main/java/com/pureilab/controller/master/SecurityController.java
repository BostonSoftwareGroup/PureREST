package com.pureilab.controller.master;

import com.pureilab.model.master.*;
import com.pureilab.security.JWTHelper;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import org.springframework.stereotype.Component;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.google.gson.Gson;

import javax.sound.midi.SysexMessage;

import com.google.gson.JsonObject;
/**
 * Created by Julian on 11/14/2016.
 *
 * {
 *     "apiKey":               "DLKC89Xl+!w9djkxlkdSDKJLKD",
 *     "token":                 "skfjalsdkladkjlkcjlkxc...adsfasdasda..........asdfasd",
 *     "operation":         "USER",
 *     ["param1", "param2" .... ]
 * }
 *
 *
 */
@Component
public class SecurityController extends AbstractController  {

    // ------------------------
    @Autowired
    private JWTHelper jwtHelper;

    @Autowired
    private UserDao userDao;

    /**
     *  New User sign up - create new user record
     * @param json
     * @return
     */
    public APIResponse signUp(String json) {

        APIResponse response = new APIResponse();
        try {
            //allObjs = countryDao.findAll();
            //allObjs = countryDao.findAll(new PageRequest(0,2));
            Gson gson = new Gson();
            User user = gson.fromJson(json, User.class);
            System.out.println("JSON: " + json);

            // TODO: check if the user exists and data validation ..
            User storedUser = userDao.findByUsername(user.getUsername());

            if(storedUser != null) {
                response.setCode("300");
                response.setMessage("User name exists ...");
                response.setBody(null);
            } else {

                String token = jwtHelper.generateToken(user.getUsername());
                System.out.println("TOKEN: " + token);
                //String userID = jwtHelper.getUsernameFromToken(token);
                //System.out.println("Get User ID from Token: " + userID);

                // Hash Password testing
                String hashedPassword = User.getSaltedHash(user.getPassword());
                System.out.println("Hash: " + hashedPassword);

                user.setPassword(hashedPassword);
                userDao.save(user);

                response.setCode("200");
                response.setMessage("Success");
                response.setBody(user);

            }
        }
        catch (Exception ex) {
            //return "Country not found";
            ex.printStackTrace();
            return null;
        }
        System.out.println(response.getBody());

        return response;
    }

    /**
     * User sign in
     * @param json
     * @return
     */
    public APIResponse signIn(String json) {

        APIResponse response = new APIResponse();
        try {

            // Step 1. parse and get request object from JSON request
            APIRequest request = APIRequest.parse(json);

            // Step 2. validate api key
            if(!validateAPIKey(request.getApiKey())) {
                throw new SecurityException("invalid API key");
            }

            System.out.println("apiKey = " + request.getApiKey() );
            System.out.println("operator = " + request.getOperator());
            System.out.println("token = " + request.getToken());
            System.out.println("body = " + request.getBody().toString());

            String body = request.getBody().toString();

            // TODO: Check API key and Token

            User user = (User) APIRequest.parse(body, User.class);

            System.out.println("id  = " + user.getId());
            System.out.println("username = " + user.getUsername());
            System.out.println("password = " + user.getPassword());

            String username = user.getUsername();
            String password =user.getPassword();

            //String hashedPassword =  User.getSaltedHash(params[1]);
            //User user = userDao.findByUsernameAndPassword(params[0], hashedPassword);

            User storedUser = userDao.findByUsername(username);
            if(storedUser != null && User.check(password, storedUser.getPassword())) {

                System.out.println("password: " + storedUser.getPassword());
                String token = jwtHelper.generateToken(storedUser.getUsername());
                System.out.println("TOKEN: " + token);
                response.setCode("200");
                response.setMessage("Success");
                response.setToken(token);
                response.setBody(token);
            } else {
                response.setCode("300");
                response.setMessage("User name or password is invalid");
                response.setToken("");
                response.setBody("");
            }

        } catch (SecurityException e) {
            response.setCode("300");
            response.setMessage(e.getMessage());
            response.setToken("");
            response.setBody("");
        } catch (Exception e) {
            response.setCode("300");
            response.setMessage("Unknown problem");
            response.setToken("");
            response.setBody("");
        }

        System.out.println(response.getBody());

        return response;
    }

    /**
     * User sign in
     * @param json
     * @return
     */
    public APIResponse test(String json) {

        APIResponse response = new APIResponse();
        try {

            // Step 1. parse and get request object from JSON request
            APIRequest request = APIRequest.parse(json);

            // Step 2. validate api key
            if(!validateAPIKey(request.getApiKey())) throw new SecurityException("invalid API key");

            System.out.println("apiKey = " + request.getApiKey() );
            System.out.println("operator = " + request.getOperator());
            System.out.println("token = " + request.getToken());
            System.out.println("body = " + request.getBody().toString());

            // TODO: Check API key and Token

            String token = request.getToken();
            String username = jwtHelper.getUsernameFromToken(token);
            System.out.println("Get User ID from Token: " + username);
            System.out.println("Created on: " + jwtHelper.getCreatedDateFromToken(token));
            System.out.println("Expired on: " + jwtHelper.getExpirationDateFromToken(token));

            token = jwtHelper.refreshToken(token);
            response.setCode("200");
            response.setMessage("Success");
            response.setToken(token);
            response.setBody("successfully complete the task");
        }
        catch (SecurityException e) {
            response.setCode("300");
            response.setMessage(e.getMessage());
            response.setToken("");
            response.setBody("");
        }

        System.out.println(response.getBody());

        return response;
    }

    /**
     * User sign in
     * @param json
     * @return
     */
    public APIResponse refreshToken(String json) {

        APIResponse response = new APIResponse();
        try {

            // Step 1. parse and get request object from JSON request
            APIRequest request = APIRequest.parse(json);

            // Step 2. validate api key
            if(!validateAPIKey(request.getApiKey())) throw new SecurityException("invalid API key");

            //System.out.println("apiKey = " + request.getApiKey() );
            //System.out.println("operator = " + request.getOperator());
            //System.out.println("token = " + request.getToken());
            //System.out.println("params = " + request.getParams().length);
            //String params[] = request.getParams();

            // TODO: Check API key
            // TODO: Token (Jason Web Token: who makes the request?


            String token = request.getToken();
            String username = jwtHelper.getUsernameFromToken(token);
            System.out.println("Get User ID from Token: " + username);
            System.out.println("Created on: " + jwtHelper.getCreatedDateFromToken(token));
            System.out.println("Expired on: " + jwtHelper.getExpirationDateFromToken(token));

            //token = jwtHelper.generateToken(username);
            token = jwtHelper.refreshToken(token);

            response.setCode("200");
            response.setMessage("Success");
            response.setToken(token);
            response.setBody("");

        }
        catch (SecurityException e) {
            response.setCode("300");
            response.setMessage(e.getMessage());
            response.setToken("");
            response.setBody("");
        }

        System.out.println(response.getBody());

        return response;
    }

}
