package com.pureilab.controller.master;

import com.google.gson.Gson;
import com.pureilab.model.master.APIRequest;
import com.pureilab.model.master.APIResponse;
import com.pureilab.model.master.User;
import com.pureilab.model.master.UserDao;
import com.pureilab.security.JWTHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
public class UserController {

    // ------------------------
    @Autowired
    private JWTHelper jwtHelper;

    @Autowired
    private UserDao userDao;


    /**
     * User sign in
     * @param json
     * @return
     */
    public APIResponse getUsers(String json) {

        APIResponse response = new APIResponse();
        try {

            // Step 1. parse and get request object from JSON request
            APIRequest request = APIRequest.parse(json);

            // Step 2. validate api key
            if(!validateAPIKey(request.getApiKey())) throw new SecurityException("invalid API key");

            System.out.println("apiKey = " + request.getApiKey() );
            System.out.println("operator = " + request.getOperator());
            System.out.println("token = " + request.getToken());


            // TODO: Check API key and Token


            String token = request.getToken();
            String username = jwtHelper.getUsernameFromToken(token);
            System.out.println("Get User ID from Token: " + username);
            System.out.println("Created on: " + jwtHelper.getCreatedDateFromToken(token));
            System.out.println("Expired on: " + jwtHelper.getExpirationDateFromToken(token));

            response.setCode("200");
            response.setMessage("Success");
            response.setBody("successfully complete the task");

        }
        catch (SecurityException e) {
            response.setCode("300");
            response.setMessage(e.getMessage());
            response.setBody(null);
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

            // TODO: Check API key and Token


            String token = request.getToken();
            String username = jwtHelper.getUsernameFromToken(token);
            System.out.println("Get User ID from Token: " + username);
            System.out.println("Created on: " + jwtHelper.getCreatedDateFromToken(token));
            System.out.println("Expired on: " + jwtHelper.getExpirationDateFromToken(token));

            token = jwtHelper.generateToken(username);

            response.setCode("200");
            response.setMessage("Success");
            response.setBody(token);

        }
        catch (SecurityException e) {
            response.setCode("300");
            response.setMessage(e.getMessage());
            response.setBody(null);
        }

        System.out.println(response.getBody());

        return response;
    }

    /**
     * Validate API Key
     * The current impleemntation: always return true.
     * TODO:
     * The future implementation can validate API keys against database table.
     *
     * @param apiKey
     * @return
     */
    private boolean validateAPIKey(String apiKey) {
            return true;
    }

}
