package com.hadarin.postapp;

import com.hadarin.postapp.entity.Client;
import com.hadarin.postapp.entity.Credit;
import com.hadarin.postapp.entity.Currency;
import com.hadarin.postapp.repos.CreditRepo;
import com.hadarin.postapp.service.ClientService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class ClientServiceTest {

    @Mock
    private CreditRepo creditRepo;

    @InjectMocks
    private ClientService clientService;

    private Client client;
    private List<Credit> credits;

    /**
     * Testing the summ of the open credits
     */

    @Before
    public void before_test() {
        this.client = new Client();

        Credit credit1 = new Credit(new BigDecimal(1000), "C");
        Credit credit2 = new Credit(new BigDecimal(5000), "O");

        this.credits = new ArrayList<>();
        this.credits.add(credit1);
        this.credits.add(credit2);

        this.client.setCredits(credits);
    }

    @Test
    public void debtSumTest() {

        Mockito.when(creditRepo.findAllByClient_IdClient(client.getIdClient())).thenReturn(credits);

        assertEquals(BigDecimal.valueOf(5000), clientService.getDebtSum(client));
    }

    /**
     * Testing the taken coefficient by the mobile operator code
     */
    @Test
    public void coefficientTest() {
        String phone = "0732420699";
        assertEquals(Double.valueOf(0.92), clientService.getLimitCoefficient(phone));
    }

    /**
     * Test of the getCourses method.
     * Currencies by index should be the following:
     * *     #0 - EUR
     * *     #1 - USD
     */
    @Test
    public void getCoursesTest() {
        ArrayList<Currency> currencies = clientService.getCourses();

        assertEquals("EUR", currencies.get(0).getCcy());
        assertEquals("USD", currencies.get(1).getCcy());
    }

    /**
     * Test of the isClientAdult method
     */
    @Test
    public void testIsAdult() {
        assertFalse(clientService.isClientAdult(LocalDateTime.now()));
        assertTrue(clientService.isClientAdult(LocalDateTime.of(1994, 1, 16, 0, 0)));
    }

}
