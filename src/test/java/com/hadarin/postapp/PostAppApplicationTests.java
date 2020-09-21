package com.hadarin.postapp;

import com.hadarin.postapp.entity.Client;
import com.hadarin.postapp.entity.Credit;
import com.hadarin.postapp.entity.Currency;
import com.hadarin.postapp.repos.ClientRepo;
import com.hadarin.postapp.repos.CreditRepo;
import com.hadarin.postapp.service.ClientService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class PostAppApplicationTests {

	@Mock
	private CreditRepo creditRepo;

	@InjectMocks
	private ClientService clientService;

	/**
	 * Testing the summ of the open credits
	 */
	@Test
	public void debtSummTest() {

		Client client = new Client();

		Credit credit1 = new Credit(new BigDecimal(1000), "C");
		Credit credit2 = new Credit(new BigDecimal(5000), "O");

		List<Credit> credits = new ArrayList<>();
		credits.add(credit1);
		credits.add(credit2);

		client.setCredits(credits);

		Mockito.when(creditRepo.findAllByClient_IdClient(client.getIdClient())).thenReturn(credits);

		assertEquals(new BigDecimal(5000), clientService.getDebtSumm(client));
	}

	/**
	 * Testing the taken coefficient by the mobile operator code
	 */
	@Test
	public void coefficientTest() {
		String phone = "0732420621";
		assertEquals(new Double(0.92), clientService.getLimitItogCoefficient(phone));
	}

	/**
	 * Test of the getCourses method.
	 * Currencies by index should be the following:
	 *      *     #0 - USD
	 *      *     #1 - EUR
	 *      *     #2 - RUR
	 *      *     #3 - BTC
	 */
	@Test
	public void getCoursesTest() {

		ArrayList<Currency> currencies = clientService.getCourses();

		assertEquals( "USD", currencies.get(0).getCcy());
		assertEquals( "EUR", currencies.get(1).getCcy());
		assertEquals( "RUR", currencies.get(2).getCcy());
		assertEquals( "BTC", currencies.get(3).getCcy());
	}

	/**
	 * Test of the isClientAdult method
	 */
	@Test
	public void testIsAdult() {
		System.out.println("BIRTHDAY IS: " + new Date(1504588505812L));
		System.out.println(clientService.isClientAdult(new Date(1504588505812L)));

		System.out.println("BIRTHDAY IS: " + new Date(704588505812L));
		System.out.println(clientService.isClientAdult(new Date(704588505812L)));
	}

	/*
	@Test
	public void ditch() {


		Map<String, String> env = System.getenv();
		for (Map.Entry<String, String> envName : env.entrySet()) {
			System.out.println(envName.getKey() + "=" + envName.getValue());
		}


		System.out.println(System.getenv("USER"));
	}
	 */

}
