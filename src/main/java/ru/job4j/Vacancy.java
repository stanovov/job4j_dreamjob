package ru.job4j;

public class Vacancy {

    private final int id;

    private String title;

    private String description;

    private double salary;

    public Vacancy(int id, String title, String description, double salary) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.salary = salary;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public double getSalary() {
        return salary;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }
}
