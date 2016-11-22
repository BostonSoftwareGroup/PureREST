package com.pureilab.model.master;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Created by Julian on 9/28/2016.
 */
@RepositoryRestResource(collectionResourceRel = "users", path = "users")
//public interface CountryDao extends CrudRepository<Country, Long> {
public interface UserDao extends PagingAndSortingRepository<User, Long> {
//public interface CountryDao extends JpaRepository<Country, Long> {


    public User findByUsername(String username);

    public User findByUsernameAndPassword(String username, String password);

}



