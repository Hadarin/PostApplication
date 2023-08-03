package com.hadarin.postapp.repos;

import com.hadarin.postapp.entity.Client;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository enable access to the data in the database (Table client),
 */
@Repository
@RepositoryRestResource(path = "clients")
public interface ClientRepo extends CrudRepository<Client, Long> {

    Client findClientByIdClient(Long idClient);

    Client findByMail(String mail);

    @Query(value = "SELECT * FROM client c where c.phone is not null;", nativeQuery = true)
    List<Client> getClientsWithPhone();


}
