package com.pureilab.controller.master;

import com.pureilab.model.master.*;
import com.pureilab.model.second.Country;

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

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.google.gson.Gson;

/**
 * Created by Julian on 11/14/2016.
 */
@RestController
public class APIController {

    @Autowired
    SecurityController securityController;

    @Autowired
    UserController userController;

    @Autowired
    CustomerController customerController;

    @Autowired
    CountryController countryController;

    // -------------- Security Related Controllers --------------
    @CrossOrigin
    @RequestMapping(value = "/sign-up", method = RequestMethod.POST)
    //@ResponseBody
    public APIResponse signUp(@RequestBody String json) {
        return securityController.signUp(json);
    }

    @CrossOrigin
    @RequestMapping(value = "/sign-in", method = RequestMethod.POST)
    //@ResponseBody
    public APIResponse signIn(@RequestBody String json) {
        return securityController.signIn(json);
    }

    @CrossOrigin
    @RequestMapping(value = "/refresh-token", method = RequestMethod.POST)
    //@ResponseBody
    public APIResponse refreshToken(@RequestBody String json) {
        return securityController.refreshToken(json);
    }

    @CrossOrigin
    @RequestMapping(value = "/test", method = RequestMethod.POST)
    //@ResponseBody
    public APIResponse test(@RequestBody String json) {
        return securityController.test(json);
    }

    // -------------- Customer Controllers --------------
    @CrossOrigin
    @RequestMapping(value = "/create-customer", method = RequestMethod.POST)
    //@ResponseBody
    public APIResponse createCustomer(@RequestBody String json) {
        return customerController.createCustomer(json);
    }

    @CrossOrigin
    @RequestMapping(value = "/customers", method = RequestMethod.POST)
    public APIResponse getCustomers(@RequestBody String json) {
        return customerController.getCustomers(json);
    }

    // -------------- User Controllers --------------

    @CrossOrigin
    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public APIResponse getUsers(@RequestBody String json) {
        return userController.getUsers(json);
    }


    @CrossOrigin
    @RequestMapping("/get-by-code")
    //@ResponseBody
    public Country getByCode(String code) {
        return countryController.getByCode(code);
    }

    @CrossOrigin
    @RequestMapping("/get-by-name")
    //@ResponseBody
    public Country getByName(String name) {
        return countryController.getByName(name);
    }

    @CrossOrigin
    @RequestMapping("/delete")
    @ResponseBody
    public String delete(long id) {
        return countryController.delete(id);
    }

    @CrossOrigin
    @RequestMapping("/create")
    @ResponseBody
    public String create(String code, String name) {
        return countryController.create(code, name);
    }

    @CrossOrigin
    @RequestMapping("/update")
    @ResponseBody
    public String updateCountry(long id, String name) {
        return countryController.updateCountry(id, name);
    }
}
