/*
 * This file is part of "U Turismu" project. 
 * 
 * U Turismu is an enterprise application in support of calabrian tour operators.
 * This system aims to promote tourist services provided by the operators
 * and to develop and improve tourism in Calabria.
 *
 * Copyright (C) 2012 "LagrecaSpaccarotella" team.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package uturismu.functional;

import java.util.Date;

import org.junit.BeforeClass;
import org.junit.Test;

import uturismu.ServiceFactory;
import uturismu.dto.Account;
import uturismu.dto.Booker;
import uturismu.dto.City;
import uturismu.dto.Customer;
import uturismu.dto.enumtype.AccountType;
import uturismu.dto.enumtype.Gender;
import uturismu.dto.enumtype.IDType;
import uturismu.service.usecase.BookerRegistrationService;

/**
 * @author "LagrecaSpaccarotella" team.
 * 
 */
public class RegistraionTest {

	private static BookerRegistrationService bookerService;
	private static City city;

	@BeforeClass
	public static void init() {
		city = new City();
		city.setName("Springfield");
		city.setProvince("USA");
		ServiceFactory.getCityService().save(city);
		bookerService = ServiceFactory.getBookerRegistrationService();
	}

	@Test
	public void registerBooker() {
		Account account = createAccount();
		Customer customer = createCustomer();
		Booker booker = createBooker(account, customer);
		bookerService.registerBooker(account, customer, booker);
	}

	private Account createAccount() {
		Account account = new Account();
		account.setActive(true);
		account.setEmail("account@gmail.com");
		account.setPassword("livuoiqueikiwiyankeecoikiwayawayani");
		account.setLastAccessTimestamp(new Date());
		account.setRegistrationTimestamp(new Date());
		account.setSalt("A3DFF002958901AFF20190");
		account.setType(AccountType.BOOKER);
		return account;
	}

	private Customer createCustomer() {
		Customer customer = new Customer();
		customer.setTaxCode("SMPHMR89T31Z404B");
		customer.setFirstName("Homer");
		customer.setLastName("Simpson");
		customer.setGender(Gender.MALE);
		customer.setBirthPlace(city);
		customer.setBirthDate(new Date());
		customer.setIdentificationDocumentNumber("1103D2");
		customer.setIdentificationDocumentType(IDType.PASSPORT);
		customer.setIssuingAuthority("Police");
		return customer;
	}

	private Booker createBooker(Account account, Customer customer) {
		Booker booker = new Booker();
		account.setBooker(booker);
		booker.setAccount(account);
		customer.setBooker(booker);
		booker.setCustomer(customer);
		return booker;
	}

}