package ru.job4j;

import java.util.ArrayList;
import java.util.List;

public class HRManager {

    private final int id;

    private String name;

    private Company company;

    private String phoneNumber;

    private final List<Vacancy> vacancies;

    public HRManager(int id, String name, Company company, String phoneNumber) {
        this(id, name, company, phoneNumber, new ArrayList<>());
    }

    public HRManager(int id, String name, Company company, String phoneNumber, List<Vacancy> vacancies) {
        this.id = id;
        this.name = name;
        this.company = company;
        this.phoneNumber = phoneNumber;
        this.vacancies = vacancies;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Company getCompany() {
        return company;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public List<Vacancy> getVacancies() {
        return vacancies;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void addVacancy(Vacancy vacancy) {
        this.vacancies.add(vacancy);
    }
}
