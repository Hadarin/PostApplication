package com.hadarin.postapp.repos;

import com.hadarin.postapp.entity.Client;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

/**
 * Repository enable access to the data in the database (Table client),
 */
@Repository
@RepositoryRestResource(path = "clients")
public interface ClientRepo extends PagingAndSortingRepository<Client, Long> {

}
