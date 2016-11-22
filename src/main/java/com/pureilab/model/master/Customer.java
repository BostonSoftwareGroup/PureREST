package com.pureilab.model.master;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;

/**
 * Created by Julian on 9/28/2016.
 *
 * Tutorial:
 * http://blog.netgloo.com/2014/10/27/using-mysql-in-spring-boot-via-spring-data-jpa-and-hibernate/
 *
 * GitHub Sample:
 * https://github.com/netgloo/spring-boot-samples/blob/master/spring-boot-mysql-springdatajpa-hibernate/src/main/java/netgloo/models/User.java
 *
 * To run:
 *
 *
 * mvn compile
 *
 * mvn spring-boot:run
 *
 */
@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    @Column(name = "fname")
    private String firstName;

    @NotNull
    @Column(name = "lname")
    private String lastName;

    @Column(name = "description")
    private String description;

    public Customer() {}

    public Customer(long id) {

        this.id = id;
    }

    public Customer(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }


    public long getId() {
        return id;
    }

    public void setId(long val) {
        this.id = val;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
