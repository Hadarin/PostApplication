package com.hadarin.postapp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hadarin.postapp.entity.Client;
import com.hadarin.postapp.service.ClientService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 *The rest controller accepts the post, get request
 */
@RestController
@RequestMapping("/api")
@AllArgsConstructor
@Slf4j
public class ClientController {

    private ClientService service;

    @GetMapping(path = "/get-principal")
    public String securityTest(Principal principal) {
        return "Secured get mapping! Name is: " + principal.getName();
    }

    @GetMapping(path = "/is-working")
    public String isAppWorks () {
        return "Application is working!";
    }

    /**
     *Handle the Post request++
     * @param client - the request body
     * @return {@link ResponseEntity<String>} - returns "OK" if the request completed without errors
     */
    @PostMapping(path = "/save-client-info", consumes = "application/json", produces = "application/json")
    ResponseEntity<String> saveClientInfo(@RequestBody Client client) throws JsonProcessingException {
        service.updateClientInfo(client);
        return ResponseEntity.ok("OK");
    }

    /**
     *Handle hte Get request
     * @return list of the clients
     */
    @GetMapping(path = "/get-clients", produces = "application/json")
    public List<Client> getClients(){
        log.info("Getting all clients");
        return service.getClients();
    }

    @GetMapping(path = "/get-clients/{id}", produces = "application/json")
    public Client getClientById(@PathVariable Long id){
        return service.getClientById(id);
    }
}


