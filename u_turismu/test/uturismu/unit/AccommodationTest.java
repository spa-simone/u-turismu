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
import static org.junit.Assert.assertThat;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.dao.DataIntegrityViolationException;

import uturismu.dto.Accommodation;
import uturismu.dto.util.AccommodationType;
import uturismu.service.AccommodationService;

/**
 * @author "LagrecaSpaccarotella" team.
 * 
 */
public class AccommodationTest extends BaseTest {

	private static AccommodationService service;
	
	@BeforeClass
	public static void initService() {
		service = context.getBean(AccommodationService.class);
	}

	@Test
	public void checkSave() {
		// create an Accommodation
		String vatNumber = "0123456";
		String name = "Mercure S.r.l.";
		Accommodation a1 = new Accommodation();
		a1.setVatNumber(vatNumber);
		a1.setName(name);
		a1.setType(AccommodationType.HOTEL);
		// save it to the DB
		service.save(a1);
		// retrieve an object with the same vatNumber
		Accommodation a2 = service.findByVatNumber(vatNumber);
		// assert that the two objects are the same
		assertThat(a2.getId(), is(equalTo(a1.getId())));
		assertThat(service.rowCount(), is(equalTo(1L)));
	}

	@Test(expected = DataIntegrityViolationException.class)
	public void checkUniqueConstraintValidation() {
		Accommodation a1 = new Accommodation();
		a1.setVatNumber("007");
		a1.setName("Moe's");
		service.save(a1);

		Accommodation a2 = new Accommodation();
		a2.setVatNumber("007");
		a1.setName("Homer Motel");
		service.save(a2);
	}

}
