package com.hadarin.postapp.service;

import com.google.gson.Gson;
import com.hadarin.postapp.entity.Client;
import com.hadarin.postapp.entity.Credit;
import com.hadarin.postapp.entity.Currency;
import com.hadarin.postapp.repos.ClientRepo;
import com.hadarin.postapp.repos.CreditRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 *The main service of the application that processing input data
 */
@Service
public class ClientService {

    @Autowired
    private ClientRepo clientRepo;

    @Autowired
    private CreditRepo creditRepo;

    private Gson gson = new Gson();

    /**
     *Service method that returns all Clients from the database
     */
    public List<Client> getClients(){
        return (List<Client>) clientRepo.findAll();
    }

    /**
     *Final Service method to process all input data
     * @param client is the request body from the post request to application
     */
    public void updateClientInfo(Client client) {
        checkClient(client);
        String logMarker = "CLIENT ID: " + client.getIdClient() + " > ";
        BigDecimal convertedMonthSalary =  convertedMonthSalary(client, getCourses());
        List<Credit> credits = getCreditsByIdClient(client.getIdClient());
        BigDecimal debtSumm;
        if (credits == null || credits.isEmpty()) {
            System.out.println(logMarker + "Кредиты у клиента отсутствуют");
            debtSumm = new BigDecimal(0);
        } else {
            debtSumm = getDebtSumm(client);
            System.out.println(logMarker + "Сумма кредитов = " + debtSumm);
            client.setCredits(credits);
        }
        Double limitItogCoefficient = getLimitItogCoefficient(client.getPhone());
        System.out.println(logMarker + "Коэффициент для рассчета лимита: " + limitItogCoefficient);
        BigDecimal limitItog = getLimitItog(limitItogCoefficient,
                                            convertedMonthSalary,
                                            debtSumm,
                                            client.getRequestLimit(),
                                            client.getDateBirthday());
        System.out.println(logMarker + "Итоговый лимит: " + limitItog);
        if (limitItog.compareTo(BigDecimal.valueOf(0)) > 0) {
            System.out.println(logMarker + "Запрошенный лимит принят");
            client.setDecision("accept");
        } else {
            System.out.println(logMarker + "Запрошенный лимит отклонён");
            client.setDecision("decline");
        }
        client.setLimitITog(limitItog);
        client.setDateCurr(new Date());
        System.out.println(logMarker + "Сохранение данных о клиенте в базу: " + gson.toJson(client));
        clientRepo.save(client);
    }



    /**
     * Taking from the api and mapping to Currency class list of the currencies.
     * Currencies by index:
     *     #0 - USD
     *     #1 - EUR
     *     #2 - RUR
     *     #3 - BTC
     * @return list of currencies
     */
    public List<Currency> getCourses () {
        RestTemplate restTemplate = new RestTemplate();
        String uri = "https://api.privatbank.ua/p24api/pubinfo?json&exchange&coursid=5";
        List<Currency> currencies = restTemplate.exchange(uri, HttpMethod.GET,
                null, new ParameterizedTypeReference<List<Currency>>(){}).getBody();
        return currencies;
    }

    /**
     * @param client is the request body from the post request to application
     * @param currencies list of the currencies taken from the api
     * @return the salary converted to UAH
     */
    public BigDecimal convertedMonthSalary(Client client, List<Currency> currencies) {
        BigDecimal converted = new BigDecimal(0);
        if (client.getCurrSalary().equals("UAH")) return client.getMonthSalary();
        else
        if (client.getCurrSalary().equals("USD")) converted = (client.getMonthSalary().multiply(currencies.get(0).getSale()));
        else
        if (client.getCurrSalary().equals("EUR")) converted = (client.getMonthSalary().multiply(currencies.get(1).getSale()));
        else
        if (client.getCurrSalary().equals("RUR")) converted = (client.getMonthSalary().multiply(currencies.get(2).getSale()));
        else
        if (client.getCurrSalary().equals("BTC")) converted = (client.getMonthSalary().multiply(currencies.get(3).getSale()));
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
    public BigDecimal getDebtSumm(Client client) {
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
    public Double getLimitItogCoefficient(String phone) {
        double k;
        String operator = phone.substring(1, 3);
        if (operator.equals("67") || operator.equals("96") || operator.equals("97") || operator.equals("98") ) k = 0.95;
        else
        if (operator.equals("50") || operator.equals("66") || operator.equals("95") || operator.equals("99") ) k = 0.94;
        else
        if (operator.equals("63") || operator.equals("73") || operator.equals("93")) k = 0.92;
        else k = 0.9;
        return k;
    }

    /**
     * Pprocess limitItog according to the following parameters
     * @param coefficient - k based on mobile operator code
     * @param convertedMonthSalary - month salary of the Client converted to UAH
     * @param debtSumm - summ of the opened Client credits
     * @param requestLimit - requested limit in UAH by Client
     * @param clientBirthday - the birthday of the Client, if the Client isn't adult - limitItog should be nulled.
     * @return processed limitItog
     */
    public BigDecimal getLimitItog (Double coefficient,
                                    BigDecimal convertedMonthSalary,
                                    BigDecimal debtSumm,
                                    BigDecimal requestLimit,
                                    Date clientBirthday) {
        BigDecimal decimalCoeff = BigDecimal.valueOf(coefficient);
        BigDecimal limitItog = decimalCoeff.multiply(convertedMonthSalary.subtract(debtSumm));
        if (limitItog.compareTo(requestLimit) > 0) {
            limitItog = requestLimit;
        }
        BigDecimal sixtyPercentsFromSalary = convertedMonthSalary.multiply(new BigDecimal(0.6));
        if (debtSumm.compareTo(sixtyPercentsFromSalary) > 0) {
            limitItog = new BigDecimal(0);
        }
        if (!isClientAdult(clientBirthday)) {
            limitItog = new BigDecimal(0);
        }
        return limitItog;
    }

    /**
     * Throws exceptions in case of the required fields are not fulfilled.
     * @param client is the request body from the post request to application
     */
    private void checkClient(Client client) {
        if (client == null) throw new IllegalArgumentException("client cannot be null");
        if (client.getIdClient() == null) throw new IllegalArgumentException("idClient cannot be null");
        if (client.getDateBirthday() == null) throw new IllegalArgumentException("dateBirthday cannot be null");
        if (client.getPhone() == null) throw new IllegalArgumentException("phone cannot be null");
        if (client.getMonthSalary() == null) throw new IllegalArgumentException("monthSalary cannot be null");
        if (client.getCurrSalary() == null) throw new IllegalArgumentException("currentSalary cannot be null");
    }

}
