package com.hadarin.postapp.controller;

import com.hadarin.postapp.entity.Client;
import com.hadarin.postapp.service.ClientService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    /**
     *Handle the Post request++
     * @param client - the request body
     * @return {@link ResponseEntity<String>} - returns "OK" if the request completed without errors
     */
    @PostMapping("/save-client-info")
    ResponseEntity<String> saveClientInfo(@RequestBody Client client) {
        service.updateClientInfo(client);
        return ResponseEntity.ok("OK");
    }

    /**
     *Handle hte Get request
     * @return list of the clients
     */
    @GetMapping("/get-clients")
    public List<Client> getClients(){
        log.info("Getting all clients...");
        return service.getClients();
    }

    @GetMapping("/get-clients/{id}")
    public Client getClientById(@PathVariable Long id){
        return service.getClientById(id);
    }
}


