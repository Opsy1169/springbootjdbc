package com.example.springbootjdbc.dao;

import com.example.springbootjdbc.model.Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CountryDao {

    public static final String[][] COUNTRY_INIT_DATA = {{"Australia", "AU"},
            {"Canada", "CA"}, {"France", "FR"}, {"Hong Kong", "HK"},
            {"Iceland", "IC"}, {"Japan", "JP"}, {"Nepal", "NP"},
            {"Russian Federation", "RU"}, {"Sweden", "SE"},
            {"Switzerland", "CH"}, {"United Kingdom", "GB"},
            {"United States", "US"}};

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Country> getCountryList() {
        List<Country> countries = jdbcTemplate.query("select * from country", (rs, rowNum) ->
                new Country(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("code_name")));
        return countries;
    }

    public List<Country> getCountryListStartWith(String name) {
        List<Country> countries = jdbcTemplate.query("select * from country where name like ?", (rs, rowNum) ->
                new Country(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("code_name")), name + "%");
        return countries;
    }

    public void updateCountryName(String codeName, String newCountryName) {
        jdbcTemplate.update("update country set name=? where code_name = ? ",
                newCountryName, codeName);
    }

    public void loadCountries() {
        for (String[] countryData : COUNTRY_INIT_DATA) {
            jdbcTemplate.update("insert into country (name, code_name) values (?, ?) ",
                    countryData[0], countryData[1]);
        }
    }

    public Country getCountryByCodeName(String codeName) throws CountryNotFoundException {
        List<Country> countries = jdbcTemplate.query("select * from country where code_name = ?", (rs, rowNum) ->
                new Country(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("code_name")), codeName);
        if (countries.isEmpty()) {
            throw new CountryNotFoundException();
        }
        return countries.get(0);
    }

    public Country getCountryByName(String name)
            throws CountryNotFoundException {
        List<Country> countries = jdbcTemplate.query("select * from country where name = ?", (rs, rowNum) ->
                new Country(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("code_name")), name);
        if (countries.isEmpty()) {
            throw new CountryNotFoundException();
        }
        return countries.get(0);
    }
}
