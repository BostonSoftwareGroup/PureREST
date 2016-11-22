package com.pureilab.controller.master;

import com.pureilab.model.master.*;
import com.pureilab.model.second.Country;
import com.pureilab.model.second.CountryDao;
import com.pureilab.security.JWTHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;


/**
 * Created by Julian on 9/28/2016.
 */
//@Controller
@Component
public class CustomerController extends SecurityController {

    // ------------------------
    // PRIVATE FIELDS
    // ------------------------
    @Autowired
    private JWTHelper jwtHelper;

    @Autowired
    private CountryDao countryDao;

    @Autowired
    private CustomerDao customerDao;

    // ------------------------
    // PUBLIC METHODS
    // ------------------------

    /**
     * /create  --> Create a new user and save it in the database.
     *
     * @param email User's email
     * @param name User's name
     * @return A string describing if the user is succesfully created or not.
     */
    public String create(String code, String name) {
        Country obj = null;
        try {
            obj = new Country(code, name);
            countryDao.save(obj);
        }
        catch (Exception ex) {
            return "Error creating the country: " + ex.toString();
        }
        return "Country succesfully created! (id = " + obj.getId() + ")";
    }

    /**
     * /delete  --> Delete the user having the passed id.
     *
     * @param id The id of the user to delete
     * @return A string describing if the user is succesfully deleted or not.
     */
    public String delete(long id) {
        try {
            Country obj = new Country(id);
            countryDao.delete(obj);
        }
        catch (Exception ex) {
            return "Error deleting the user: " + ex.toString();
        }
        return "Country succesfully deleted!";
    }

    public Country getByName(String name) {
        String objId;
        Country obj = null;
        try {
            obj = countryDao.findByName(name);
            objId = String.valueOf(obj.getId());
        }
        catch (Exception ex) {
            //return "Country not found";
            return null;
        }
        //return "The country id is: " + objId;
        return obj;
    }

    public Country getByCode(String code) {
        String objId;
        Country obj = null;
        try {
            obj = countryDao.findByCode(code);
            objId = String.valueOf(obj.getId());
        }
        catch (Exception ex) {
            //return "Country not found";
            return null;
        }
        //return "The country id is: " + objId;
        return obj;
    }

    public APIResponse getAllObjects() {
        APIResponse response = new APIResponse();
        Iterable allObjs = null;
        try {
            //allObjs = countryDao.findAll();
            //allObjs = countryDao.findAll(new PageRequest(0,2));
            allObjs = customerDao.findAll(new Sort("firstName"));

            String token = jwtHelper.generateToken("Julian");
            System.out.println("TOKEN: " + token);
            String userID = jwtHelper.getUsernameFromToken(token);
            System.out.println("Get User ID from Token: " + userID);

            // Hash Password testing
            String hashed =  User.getSaltedHash("123456");
            System.out.println("Hash: " + hashed );

            //boolean checkPassword(String password, String stored)
        }
        catch (Exception ex) {
            //return "Country not found";
            return null;
        }
        //return "The country id is: " + objId;
        response.setCode("200");
        response.setMessage("Success");
        response.setBody(allObjs);
        //return allObjs;
        return response;
    }

    public APIResponse createCustomer(String json) {

        APIResponse response = new APIResponse();

        try {
            //allObjs = countryDao.findAll();
            //allObjs = countryDao.findAll(new PageRequest(0,2));

            // Step 1. parse and get request object from JSON request
            APIRequest request = APIRequest.parse(json);

            // Step 2. validate api key
            // TODO: Check API key and Token
            if(!validateAPIKey(request.getApiKey())) {
                throw new SecurityException("invalid API key");
            }

            String token = request.getToken();

            String body = request.getBody().toString();


            Customer customer = (Customer) APIRequest.parse(body, Customer.class);

            if(customer != null) {
                System.out.println("id: " + customer.getId());
                System.out.println("fname: " + customer.getFirstName());
                System.out.println("lname: " + customer.getLastName());
                System.out.println("description: " + customer.getDescription());

                customerDao.save(customer);
            }

            token = jwtHelper.refreshToken(token);
            response.setCode("200");
            response.setMessage("Success");
            response.setToken(token);
            response.setBody(customer);

        } catch (SecurityException e) {
            response.setCode("300");
            response.setMessage(e.getMessage());
            response.setToken("");
            response.setBody(e.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setCode("300");
            response.setMessage("Unknown problem");
            response.setToken("");
            response.setBody(ex.getMessage());
        }

        return response;
    }

    public APIResponse getCustomers(String json) {
        APIResponse response = new APIResponse();

        Iterable allObjs = null;

        try {

            APIRequest request = APIRequest.parse(json);
            // TODO: Check API key and Token
            if(!validateAPIKey(request.getApiKey())) {
                throw new SecurityException("invalid API key");
            }

            String token = request.getToken();

            allObjs = customerDao.findAll(new Sort("firstName"));

            token = jwtHelper.refreshToken(token);

            response.setCode("200");
            response.setMessage("Success");
            response.setToken(token);
            response.setBody(allObjs);

        } catch (SecurityException e) {
            response.setCode("300");
            response.setMessage(e.getMessage());
            response.setToken("");
            response.setBody(e.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setCode("500");
            response.setMessage("Unknown problem");
            response.setToken("");
            response.setBody(ex.getMessage());
        }
        //return "The country id is: " + objId;

        //return allObjs;
        return response;
    }

    public Iterable getAllObjects2() {
        Iterable allObjs = null;
        try {
            //allObjs = countryDao.findAll();
            //allObjs = countryDao.findAll(new PageRequest(0,2));
            allObjs = customerDao.findAll(new Sort("firstName"));

        }
        catch (Exception ex) {
            //return "Country not found";
            return null;
        }
        //return "The country id is: " + objId;
        return allObjs;
    }

    /**
     * /update  --> Update the email and the name for the user in the database
     * having the passed id.
     *
     * @param id The id for the user to update.
     * @param email The new email.
     * @param name The new name.
     * @return A string describing if the user is succesfully updated or not.
     */
    public String updateCountry(long id, String name) {
        try {
            Country obj = countryDao.findOne(id);
            obj.setName(name);
            countryDao.save(obj);
        }
        catch (Exception ex) {
            return "Error updating the country: " + ex.toString();
        }
        return "Country succesfully updated!";
    }
}
