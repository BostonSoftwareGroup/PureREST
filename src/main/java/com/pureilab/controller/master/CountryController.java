package com.pureilab.controller.master;

import com.pureilab.model.master.*;
import com.pureilab.model.second.Country;
import com.pureilab.model.second.CountryDao;
import com.pureilab.security.JWTHelper;

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


/**
 * Created by Julian on 9/28/2016.
 */
//@Controller
@Component
public class CountryController extends SecurityController {

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
