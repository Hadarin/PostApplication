package com.hadarin.postapp;

import com.hadarin.postapp.entity.Client;
import com.hadarin.postapp.entity.Credit;
import com.hadarin.postapp.service.ClientService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class PostAppApplicationTests {

	@InjectMocks
	private ClientService clientService;

	/**
	 * Testing the summ of the open credits
	 */
	@Test
	public void debtSummTest() {

		Client client1 = new Client();

		Credit credit1 = new Credit(new BigDecimal(1000), "C");
		Credit credit2 = new Credit(new BigDecimal(5000), "O");

		List<Credit> credits = new ArrayList<>();
		credits.add(credit1);
		credits.add(credit2);

		client1.setCredits(credits);

		assertEquals(new BigDecimal(5000), clientService.debtSumm(client1));
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
		assertEquals( "USD", clientService.getCourses().get(0).getCcy());
		assertEquals( "EUR", clientService.getCourses().get(1).getCcy());
		assertEquals( "RUR", clientService.getCourses().get(2).getCcy());
		assertEquals( "BTC", clientService.getCourses().get(3).getCcy());
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

}
