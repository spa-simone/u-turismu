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
package uturismu.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import uturismu.ServiceFactory;
import uturismu.dto.HolidayPackage;
import uturismu.dto.OvernightStay;
import uturismu.dto.TourOperator;
import uturismu.dto.enumtype.ServiceType;
import uturismu.service.backup.HolidayPackageService;
import uturismu.service.backup.OvernightStayService;
import uturismu.service.backup.TourOperatorService;

/**
 * @author "LagrecaSpaccarotella" team.
 * 
 */
public class HolidayPackageTest {

	private static HolidayPackageService holidayPackageService;
	private static OvernightStayService overnightStayService;
	private static TourOperatorService tourOperatorService;

	@BeforeClass
	public static void init() {
		holidayPackageService = ServiceFactory.getHolidayPackageService();
		overnightStayService = ServiceFactory.getOvernightStayService();
		tourOperatorService = ServiceFactory.getTourOperatorService();
	}

	/*
	 * Il problema sta in questo metodo. Non puoi salvare un Holiday Package quì
	 * perché i metodi annotati con @Before vengono richiamati tante volte quanti
	 * sono i metodi annotati con @Test. Siccome c'è un vincolo di unicità sulla
	 * coppia (name, id_tour_operator), la seconda volta che viene eseguito
	 * questo codice, verrà lanciata un'eccezione del tipo
	 * "DataIntegrityViolationException".
	 */
	@Before
	public void create_HolidayPakage() {

		System.out.println("######## CIAO CIAO CIAO ########");

		HolidayPackage holidayPackage = createHolidayPackage();
		Long id = holidayPackageService.save(holidayPackage);

		HolidayPackage queried = holidayPackageService.findById(id);
		assertThat(holidayPackage.getId(), is(equalTo(queried.getId())));

	}

	/*
	 * Non hai la garanzia che lo stato del DB sia come ti aspetti. I metodi di
	 * test vengono eseguiti con un ordine al di fuori del controllo dello
	 * sviluppatore. Non è detto che venga eseguito prima il test di creazione,
	 * poi quello di update e poi quello di delete.
	 */
	@Test
	public void updateHP() {
		String descr = "una prova di testing unit";
		HolidayPackage holidayPackage = createHolidayPackage();
		Long Id = holidayPackageService.save(holidayPackage);

		HolidayPackage hpTest = holidayPackageService.findById(Id);
		assertThat(hpTest, notNullValue());

		hpTest.setDescription(descr);
		holidayPackageService.update(holidayPackage);

		HolidayPackage hpUpdated = holidayPackageService.findById(Id);

		assertThat(hpTest.getDescription(), is(equalTo(hpUpdated.getDescription())));

	}

	/*
	 * Non hai la garanzia che lo stato del DB sia come ti aspetti. I metodi di
	 * test vengono eseguiti con un ordine al di fuori del controllo dello
	 * sviluppatore. Non è detto che venga eseguito prima il test di creazione,
	 * poi quello di update e poi quello di delete.
	 */
	@Test
	public void deleteHolidayPackage() {
		Long id = holidayPackageService.save(createHolidayPackage());
		HolidayPackage hp = holidayPackageService.findById(id);

		List<HolidayPackage> queryList = holidayPackageService.findAll();
		Long rowCount = holidayPackageService.rowCount();

		assertThat(queryList, is(org.hamcrest.Matchers.not(null)));
		assertThat(rowCount, is(equalTo(1L)));

		for (HolidayPackage holidayPackage : queryList) {
			holidayPackageService.delete(holidayPackage);
		}

		queryList = holidayPackageService.findAll();

		assertThat(queryList.size(), is(equalTo(0)));

	}

	private OvernightStay createovernightStay() {
		OvernightStay overnightStay = new OvernightStay();
		overnightStay.setArrivalDate(new GregorianCalendar().getTime());
		overnightStay.setLeavingDate(new GregorianCalendar().getTime());
		overnightStay.setServiceType(ServiceType.FULL_SERVICE);
		overnightStay.setPrice(35d);
		return overnightStay;
	}

	private TourOperator createTourOperator() {
		TourOperator top = new TourOperator();
		top.setName("Il Viaggio");
		return top;

	}

	private HolidayPackage createHolidayPackage() {
		HolidayPackage holidayPackage = new HolidayPackage();
		TourOperator top = createTourOperator();
		OvernightStay overnightStay = createovernightStay();

		holidayPackage.setName("alpiMe");
		holidayPackage.setTourOperator(top);
		holidayPackage.addService(overnightStay);
		holidayPackage.setCustomerNumber(1);

		overnightStayService.save(overnightStay);
		tourOperatorService.save(top);
		return holidayPackage;
	}

}
