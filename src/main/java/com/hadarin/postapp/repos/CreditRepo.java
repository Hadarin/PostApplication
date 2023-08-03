package com.hadarin.postapp.repos;

import com.hadarin.postapp.entity.Credit;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository enable access to the data in the database (Table credit),
 */
@Repository
public interface CreditRepo extends CrudRepository<Credit, Long> {

    /**
     * Finds credits of the client by idClient
     *
     * @param idClient id of the client that taken from the post request
     * @return list of credits
     */
    List<Credit> findAllByClient_IdClient(Long idClient);
}
