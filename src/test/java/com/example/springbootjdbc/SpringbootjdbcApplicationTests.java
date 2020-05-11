package com.example.springbootjdbc;

import com.example.springbootjdbc.dao.CountryDao;
import com.example.springbootjdbc.dao.CountryNotFoundException;
import com.example.springbootjdbc.model.Country;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest

class SpringbootjdbcApplicationTests {

	@BeforeEach
	public void setup() {
		initExpectedCountryLists();
		countryDao.loadCountries();
	}


	@Autowired
	private CountryDao countryDao;

	private List<Country> expectedCountryList = new ArrayList<Country>();
	private List<Country> expectedCountryListStartsWithA = new ArrayList<Country>();
	private Country countryWithChangedName = new Country(1, "Russia", "RU");




	@Test
	@DirtiesContext
	public void testCountryList() {
		List<Country> countryList = countryDao.getCountryList();
		assertNotNull(countryList);
		assertEquals(expectedCountryList.size(), countryList.size());
		for (int i = 0; i < expectedCountryList.size(); i++) {
			assertEquals(expectedCountryList.get(i), countryList.get(i));
		}
	}

	@Test
	@DirtiesContext
	public void testCountryListStartsWithA() {
		List<Country> countryList = countryDao.getCountryListStartWith("A");
		assertNotNull(countryList);
		assertEquals(expectedCountryListStartsWithA.size(), countryList.size());
		for (int i = 0; i < expectedCountryListStartsWithA.size(); i++) {
			assertEquals(expectedCountryListStartsWithA.get(i), countryList.get(i));
		}
	}

	@Test
	@DirtiesContext
	public void testCountryChange() throws CountryNotFoundException {
		countryDao.updateCountryName("RU", "Russia");
		assertEquals(countryWithChangedName, countryDao.getCountryByCodeName("RU"));
	}

	private void initExpectedCountryLists() {
		for (int i = 0; i < CountryDao.COUNTRY_INIT_DATA.length; i++) {
			String[] countryInitData = CountryDao.COUNTRY_INIT_DATA[i];
			Country country = new Country(i, countryInitData[0], countryInitData[1]);
			expectedCountryList.add(country);
			if (country.getName().startsWith("A")) {
				expectedCountryListStartsWithA.add(country);
			}
		}
	}

}
