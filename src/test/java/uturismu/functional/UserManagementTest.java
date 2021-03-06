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

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static uturismu.ServiceFactory.getCityService;
import static uturismu.ServiceFactory.getHolidayTagService;
import static uturismu.ServiceFactory.getUserService;

import java.util.Date;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import uturismu.HashUtil;
import uturismu.dto.Account;
import uturismu.dto.Booker;
import uturismu.dto.City;
import uturismu.dto.HolidayPackage;
import uturismu.dto.HolidayTag;
import uturismu.dto.TourOperator;
import uturismu.dto.enumtype.AccountType;
import uturismu.dto.enumtype.Gender;
import uturismu.dto.enumtype.Status;
import uturismu.exception.AccountException;

/**
 * @author "LagrecaSpaccarotella" team.
 */
public class UserManagementTest {

	private static City city;

	@BeforeClass
	public static void init() {
		city = new City();
		city.setName("Springfield");
		city.setProvince("USA");
		getCityService().save(city);
		HolidayTag tag = null;
		for (int i = 1; i <= 10; i++) {
			tag = new HolidayTag();
			tag.setName("tag" + i);
			tag.setDescription("desc1");
			getHolidayTagService().save(tag);
		}
	}

	/**
	 * Create a booker account and check whether the login service works.
	 */
	@Test
	public void createBookerAccountAndLogin() {
		String email = "test@volunia.eu";
		String password = "livuoiqueikiwiyankeecoikisayayaiani";
		String taxCode = "SSSHHH80S33H324L";
		// creates the account
		Account account = createAccount(email, password);
		// creates the booker
		Booker booker = createBooker(taxCode, account);
		// persist the account and the booker objects
		getUserService().createAccount(account, booker);
		// log in the user
		Account sameAccount = getUserService().logIn(email, password);
		// asserts that the account is on the DB
		assertThat(account.getId(), is(equalTo(sameAccount.getId())));
	}

	/**
	 * Create a booker account and checks whether the login service throws an
	 * InvalidCredentialException in response of an invalid password.
	 */
	@Test(expected = AccountException.class)
	public void createBookerAccountAndLoginWithException() {
		String email = "account@volunia.eu";
		String password = "livuoiqueikiwiyankeecoikisayayaiani";
		String taxCode = "GPGLLL11D89G039A";
		String invalidPassword = "invalidpassword";
		// creates the account
		Account account = createAccount(email, password);
		// creates the booker
		Booker booker = createBooker(taxCode, account);
		// persist the account and the booker data
		getUserService().createAccount(account, booker);
		// try to log in, but the password is invalid. An exception is expected.
		account = getUserService().logIn(email, invalidPassword);
	}

	@Test
	public void showHolidayPackage() {
		// retrieves all the holiday tags
		List<HolidayTag> tags = getHolidayTagService().findAll();
		// create the first account
		Account account1 = createAccount("homer@simpson.com", "dehihiho");
		TourOperator to1 = createTourOperator("to1", account1);
		// create the second account
		Account account2 = createAccount("marge@bouvier.com", "mmmmmmmmmm");
		TourOperator to2 = createTourOperator("to2", account2);
		// create five holiday package and two of those aren't published yet.
		HolidayPackage pack1 = createHolidayPackage("Cetraro Beach", Status.PUBLISHED, to1);
		pack1.addHolidayTag(tags.get(0));
		pack1.addHolidayTag(tags.get(1));
		pack1.addHolidayTag(tags.get(2));
		pack1.addHolidayTag(tags.get(3));

		HolidayPackage pack2 = createHolidayPackage("La riviera dei cedri", Status.PUBLISHED, to1);
		pack2.addHolidayTag(tags.get(0));

		HolidayPackage pack3 = createHolidayPackage("Settimana di piacere", Status.DRAFT, to1);
		pack3.addHolidayTag(tags.get(1));
		pack3.addHolidayTag(tags.get(2));

		HolidayPackage pack4 = createHolidayPackage("U Sazizzu", Status.PUBLISHED, to2);
		pack4.addHolidayTag(tags.get(2));
		pack4.addHolidayTag(tags.get(3));

		HolidayPackage pack5 = createHolidayPackage("Simu i Cusenza", Status.DRAFT, to2);
		pack5.addHolidayTag(tags.get(3));

		// save the detached objects on the DB
		getUserService().createAccount(account1, to1);
		getUserService().createAccount(account2, to2);

		// the total amount of published holiday packages is 3, even though the
		// total rows number in the HolidayPackage table is 5.
		assertThat(getUserService().getHolidayPackagesNumber(), is(equalTo(3L)));
		// the published packages of the Tour Operator with id number 1 are two
		List<HolidayPackage> list = getUserService().getHolidayPackagesByTourOperator(to1.getId());
		assertThat(list.size(), is(equalTo(2)));
		// the published packages annotated with the third tag of the list are
		// two, because the one called "Settimana di piacere" annotated with the
		// same tag is a DRAFT.
		list = getUserService().getHolidayPackagesByTags(tags.get(2).getId());
		assertThat(list.size(), is(equalTo(2)));
	}

	private static Account createAccount(String email, String password) {
		Account account = new Account();
		account.setActive(true);
		account.setEmail(email);
		String salt = HashUtil.generateSalt();
		account.setSalt(salt);
		account.setPassword(HashUtil.getHash(password, salt));
		account.setLastAccessTimestamp(new Date());
		account.setRegistrationTimestamp(new Date());
		return account;
	}
	
	private static TourOperator createTourOperator(String vatNumber, String name, Account account) {
		TourOperator tourOperator = new TourOperator();
		tourOperator.setVatNumber(vatNumber);
		tourOperator.setName(name);
		tourOperator.setHolderName("Dr. Lemuel Gulliver");
		tourOperator.setAccount(account);
		account.setTourOperator(tourOperator);
		return tourOperator;
	}

	private static Booker createBooker(String taxCode, Account account) {
		Booker booker = new Booker();
		booker.setFirstName("Name");
		booker.setLastName("Surname");
		booker.setTaxCode(taxCode);
		booker.setBirthDate(new Date());
		booker.setBirthPlace(city);
		booker.setGender(Gender.FEMALE);
		booker.setAccount(account);
		account.setBooker(booker);
		return booker;
	}

	private static TourOperator createTourOperator(String vatNumber, Account account) {
		TourOperator tourOperator = new TourOperator();
		tourOperator.setVatNumber(vatNumber);
		tourOperator.setName("Gulliver Travel");
		tourOperator.setHolderName("Dr. Lemuel Gulliver");
		tourOperator.setAccount(account);
		account.setTourOperator(tourOperator);
		return tourOperator;
	}

	private static HolidayPackage createHolidayPackage(String name, Status status,
			TourOperator tourOperator) {
		HolidayPackage pack = new HolidayPackage();
		pack.setName(name);
		pack.setStatus(status);
		pack.setDueDate(new Date());
		pack.setCustomerNumber(2);
		pack.setAvailability(10);
		pack.setTourOperator(tourOperator);
		tourOperator.addHolidayPackage(pack);
		return pack;
	}

}