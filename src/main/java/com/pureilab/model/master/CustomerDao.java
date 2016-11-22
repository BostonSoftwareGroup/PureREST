package com.pureilab.model.master;

import org.springframework.data.repository.PagingAndSortingRepository;

import javax.transaction.Transactional;

import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import org.springframework.web.bind.annotation.CrossOrigin;

/**
 * Created by Julian on 9/28/2016.
 */
@RepositoryRestResource(collectionResourceRel = "customers", path = "customers")
//public interface CountryDao extends CrudRepository<Country, Long> {
public interface CustomerDao extends PagingAndSortingRepository<Customer, Long> {
//public interface CountryDao extends JpaRepository<Country, Long> {

    public Customer save(Customer obj);
}



