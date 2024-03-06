package com.hadarin.postapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hadarin.postapp.entity.Client;
import com.hadarin.postapp.repos.ClientRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class ClientControllerItTest extends AbstractItTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    ClientRepo repo;

    @Autowired
    @Qualifier("customMapper")
    ObjectMapper customMapper;

    @Test
    public void mvc_is_not_null () {
        assertThat(mvc).isNotNull();
    }

    private String getTokenForTests () throws Exception {
        MvcResult result = this.mvc.perform(post("/token")
                        .with(httpBasic("hadarin", "password1")))
                .andExpect(status().isOk())
                .andReturn();

        return result.getResponse().getContentAsString();
    }

    @Test
    public void should_return_jsonArray_of_clients () throws Exception {

        repo.save(Client.builder()
                .phone("380676666666")
                .mail("mail1@test.com")
                .build());
        repo.save(Client.builder()
                .phone("380676666666")
                .mail("mail2@test.com")
                .build());
        repo.save(Client.builder()
                .phone("380676666666")
                .mail("mail3@test.com")
                .birthdayDate(LocalDateTime.of(1994, 1, 16, 0, 0))
                .build());
        repo.save(Client.builder().build());
        repo.save(Client.builder().build());
        repo.save(Client.builder().build());

        mvc.perform(get("/api/get-clients").header("Authorization",
                        "Bearer " + getTokenForTests()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(6)))
                .andExpect(jsonPath("$[0].phone").isNotEmpty());
    }

    @Test
    public void bad_good_request_test () throws Exception {

        mvc.perform(post("/api/save-client-info")
                .header("Authorization", "Bearer " + getTokenForTests())
                .content(String.valueOf(customMapper.writeValueAsString(new Client())))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        Client client = Client.builder()
                .idClient(1L)
                .salaryCurrency("EUR")
                .address("test")
                .birthdayDate(LocalDateTime.of(1994,
                        1,
                        16,
                        0,
                        0,
                        0,
                        0))
                .phone("380676666666")
                .requestedLimit(BigDecimal.valueOf(40000))
                .monthSalary(BigDecimal.valueOf(2000))
                .mail("testMail")
                .build();

        mvc.perform(post("/api/save-client-info")
                        .header("Authorization", "Bearer " + getTokenForTests())
                        .content(customMapper.writeValueAsString(client))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        client = repo.findClientByIdClient(1L);
        assertThat(client.getDecision()).isEqualTo("accept");
    }

}
