DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS candidates;
DROP TABLE IF EXISTS cities;
DROP TABLE IF EXISTS post;

CREATE TABLE IF NOT EXISTS cities (
    id SERIAL PRIMARY KEY,
    name TEXT
);

CREATE TABLE IF NOT EXISTS post (
    id SERIAL PRIMARY KEY,
    name TEXT,
    description TEXT,
    created TIMESTAMP
);

CREATE TABLE IF NOT EXISTS candidates (
    id SERIAL PRIMARY KEY,
    name TEXT,
    city_id INT REFERENCES cities(id),
    created TIMESTAMP
);

CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    name TEXT,
    email VARCHAR(255) UNIQUE,
    password VARCHAR(255)
);

INSERT INTO cities(name) VALUES ('Москва'), ('Санкт-Петербург'), ('Тверь');