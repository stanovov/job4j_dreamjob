package ru.job4j;


public class Candidate {

    private final int id;

    private String name;

    private int age;

    private Resume resume;

    public Candidate(int id, String name, int age, boolean sex, Resume resume) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.resume = resume;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public Resume getResume() {
        return resume;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setResume(Resume resume) {
        this.resume = resume;
    }
}
