package ru.job4j;

import java.util.HashMap;
import java.util.Map;

public class Resume {

    private final int id;

    private String name;

    private String description;

    private final Map<String, String> contacts;

    public Resume(int id, String name, String description) {
        this(id, name, description, new HashMap<>());
    }

    public Resume(int id, String name, String description, Map<String, String> contacts) {
        this.id = id;
        this.contacts = contacts;
        this.name = name;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Map<String, String> getContacts() {
        return contacts;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean putContact(String key, String value) {
        return contacts.put(key, value) != null;
    }
}
