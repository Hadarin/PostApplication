package com.hadarin.postapp.service;

import com.google.gson.Gson;
import com.hadarin.postapp.entity.Client;
import com.hadarin.postapp.entity.Credit;
import com.hadarin.postapp.entity.Currency;
import com.hadarin.postapp.repos.ClientRepo;
import com.hadarin.postapp.repos.CreditRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *The main service of the application that processing input data
 */
@Service
@Slf4j
public class ClientService {

    private final ClientRepo clientRepo;
    private final CreditRepo creditRepo;


    @Autowired
    public  ClientService (CreditRepo creditRepo, ClientRepo clientRepo){
        this.clientRepo = clientRepo;
        this.creditRepo = creditRepo;
    }

    private final Gson gson = new Gson();

    /**
     *Service method that returns all Clients from the database
     */
    public List<Client> getClients(){
        return (List<Client>) clientRepo.findAll();
    }

    /**
     *
     * @param idClient
     * @return client by idClient
     */
    public Client getClientById(Long idClient){
        Client client = clientRepo.findClientByIdClient(idClient);
        if(client != null){
            return client;
        } else {
            throw new IllegalArgumentException("The client isn't found in base by id " + idClient +".");
        }
    }

    /**
     *Final Service method to process all input data
     * @param client is the request body from the post request to application
     */
    public void updateClientInfo(Client client) {
        checkClientAndServices(client);
        String logMarker = "CLIENT ID: " + client.getIdClient() + " > ";
        BigDecimal convertedMonthSalary =  convertedMonthSalary(client, getCourses());
        List<Credit> credits = getCreditsByIdClient(client.getIdClient());
        BigDecimal debtSum;
        if (credits == null || credits.isEmpty()) {
            log.debug(logMarker + "doesn't have debts");
            debtSum = new BigDecimal(0);
        } else {
            debtSum = getDebtSum(client);
            log.debug(logMarker + "debt sum = " + debtSum);
            client.setCredits(credits);
        }
        Double limitCoefficient = getLimitCoefficient(client.getPhone());
        log.debug(logMarker + "coefficient for the limit calculation: " + limitCoefficient);
        BigDecimal limit = calculateLimit(limitCoefficient,
                                            convertedMonthSalary,
                                            debtSum,
                                            client.getRequestLimit(),
                                            client.getDateBirthday());
        log.debug(logMarker + "calculated limit: " + limit);
        if (limit.compareTo(BigDecimal.valueOf(0)) > 0) {
            log.debug(logMarker + "requested limit accepted");
            client.setDecision("accept");
        } else {
            System.out.println(logMarker + "requested limit declined");
            client.setDecision("decline");
        }
        client.setLimitITog(limit);
        client.setDateCurr(new Date());
        log.debug(logMarker + "saving client data: " + gson.toJson(client));
        clientRepo.save(client);
    }



    /**
     * Taking from the api and mapping to Currency class list of the currencies.
     * Currencies by index:
     *     #0 - EUR
     *     #1 - USD
     * @return list of currencies
     */
    public ArrayList<Currency> getCourses () {
        RestTemplate restTemplate = new RestTemplate();
        String uri = "https://api.privatbank.ua/p24api/pubinfo?json&exchange&coursid=5";
         return restTemplate.exchange(uri, HttpMethod.GET,
                    null, new ParameterizedTypeReference<ArrayList<Currency>>() {
                    }).getBody();
    }

    /**
     * @param client is the request body from the post request to application
     * @param currencies list of the currencies taken from the api
     * @return the salary converted to UAH
     */
    public BigDecimal convertedMonthSalary(Client client, List<Currency> currencies) {
        BigDecimal converted = new BigDecimal(0);
        BigDecimal monthSalary = client.getMonthSalary();
        for(Currency currency : currencies){
            if(currency.getCcy().equals(client.getCurrSalary())){
                converted = monthSalary.multiply(currency.getSale());
                break;
            }
        }
        return converted;
    }

    /**
     *
     * @param birthDay - birthday of the Client
     * @return true - if the CLient adult, false - if the Client is underage
     */
    public boolean isClientAdult(Date birthDay) {
        LocalDate birthDayLocal = birthDay.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
         return ((Period.between(birthDayLocal, LocalDate.now())).getYears() >= 18);
    }

    /**
     *Counting all opened credits of the Client
     * @param client is the request body from the post request to application
     * @return summ of the all opened credits
     */
    public BigDecimal getDebtSum(Client client) {
        BigDecimal summ = new BigDecimal(0);
        for (Credit credit: creditRepo.findAllByClient_IdClient(client.getIdClient())) {
            if (credit.getStateCredit().equals("O")) {
                summ = summ.add(credit.getAmtCredit());
            }
        }
        return  summ;
    }

    public List<Credit> getCreditsByIdClient(Long idClient) {
        return creditRepo.findAllByClient_IdClient(idClient);
    }

    /**
     * Processing coefficient k, that necessary to limitItog processing
     * @param phone - telephone number of the Client
     * @return coefficient k
     */
    public Double getLimitCoefficient(String phone) {
        double k;
        String operator = phone.substring(1, 3);
        switch (operator) {
            case "67":
            case "96":
            case "97":
            case "98":
                k = 0.95;
                break;
            case "50":
            case "66":
            case "95":
            case "99":
                k = 0.94;
                break;
            case "63":
            case "73":
            case "93":
                k = 0.92;
                break;
            default:
                k = 0.9;
                break;
        }
        return k;
    }

    /**
     * Pprocess limit according to the following parameters
     * @param coefficient - k based on mobile operator code
     * @param convertedMonthSalary - month salary of the Client converted to UAH
     * @param debtSumm - summ of the opened Client credits
     * @param requestLimit - requested limit in UAH by Client
     * @param clientBirthday - the birthday of the Client, if the Client isn't adult - limit should be null.
     * @return processed limit
     */
    public BigDecimal calculateLimit(Double coefficient,
                                     BigDecimal convertedMonthSalary,
                                     BigDecimal debtSumm,
                                     BigDecimal requestLimit,
                                     Date clientBirthday) {
        BigDecimal decimalCoefficient = BigDecimal.valueOf(coefficient);
        BigDecimal limit = decimalCoefficient.multiply(convertedMonthSalary.subtract(debtSumm));
        if (limit.compareTo(requestLimit) > 0) {
            limit = requestLimit;
        }
        BigDecimal sixtyPercentsFromSalary = convertedMonthSalary.multiply(new BigDecimal("0.6"));
        if (debtSumm.compareTo(sixtyPercentsFromSalary) > 0) {
            limit = new BigDecimal(0);
        }
        if (!isClientAdult(clientBirthday)) {
            limit = new BigDecimal(0);
        }
        return limit;
    }

    /**
     * Throws exceptions in case of the required fields are not fulfilled.
     * @param client is the request body from the post request to application
     */
    private void checkClientAndServices(Client client) {
        if (client == null) throw new IllegalArgumentException("client cannot be null");
        if (client.getIdClient() == null) throw new IllegalArgumentException("idClient cannot be null");
        if (client.getDateBirthday() == null) throw new IllegalArgumentException("dateBirthday cannot be null");
        if (client.getPhone() == null) throw new IllegalArgumentException("phone cannot be null");
        if (client.getMonthSalary() == null) throw new IllegalArgumentException("monthSalary cannot be null");
        if (client.getCurrSalary() == null) throw new IllegalArgumentException("currentSalary cannot be null");
        if (getCourses() == null) throw new HttpServerErrorException(HttpStatus.SERVICE_UNAVAILABLE);
    }

}
