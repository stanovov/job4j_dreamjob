package ru.job4j.dream.store;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Post;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Store {

    private static final Store INST = new Store();

    private final Map<Integer, Post> posts = new ConcurrentHashMap<>();

    private final Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();

    private Store() {
        String ls = System.lineSeparator();
        posts.put(
                1,
                new Post(
                        1,
                        "Junior Java Job",
                        "Наши ожидания:" + ls
                                + "-глубокие теоретические познания Java;" + ls
                                + "-обязательно наличие оконченных курсов по разработке на Java;" + ls
                                + "-желателен опыт работы в области разработки на Java от полугода;" + ls
                                + "-уровень английского Intermediate и выше.",
                        LocalDateTime.of(2021, 9, 13, 19, 30, 0)
                )
        );
        posts.put(
                2,
                new Post(
                        2,
                        "Middle Java Job",
                        "Что мы ожидаем от вас:" + ls
                                + " отличные знания Java 8-11, Spring Framework и Spring Boot и опыт их применения для "
                                + " разработки распределенных систем;" + ls
                                + " опыт работы c технологиями контейнеризации (Docker и т.п.) и с системами "
                                + "оркестрации (Kubernetes, OpenShift и т.п.);" + ls
                                + "опыт проектирования или участие в разработке высоконагруженных и отказоустойчивых "
                                + "распределенных систем.",
                        LocalDateTime.of(2021, 9, 12, 11, 0, 0)
                )
        );
        posts.put(
                3,
                new Post(
                        3,
                        "Senior Java Job",
                        "Мы занимаемся разработкой собственной инновационной платформы для "
                                + "финтех-организаций и банков по всему миру. Наши заказчики из Европы, Африки и "
                                + "Ближнего Востока запускают новые платежные сервисы, а мы помогаем им в этом от "
                                + "стадии проектирования до запуска в эксплуатацию." + ls
                                + ls
                                + "От вас требуется:" + ls
                                + "-Отличное знание Java 8/11+, опыт коммерческой разработки на Java от 4х лет." + ls
                                + "-Опыт работы со Spring (DI, Security, MVC, Boot / Actuator), Hibernate, Maven / "
                                + "Gradle, PostgreSQL / MySQL, Kafka / RabbitMQ или аналогами;" + ls
                                + "-Хорошее знание базовых алгоритмов и структур данных, принципов ООП и функционального"
                                + " программирования, навыки многопоточного программирования;" + ls
                                + "-Умение самостоятельно проектировать приложения или отдельные сервисы;" + ls
                                + "-Владение письменным английским языком.",
                        LocalDateTime.of(2021, 8, 28, 15, 45, 25))
        );
        candidates.put(1, new Candidate(1, "Junior Java"));
        candidates.put(2, new Candidate(2, "Middle Java"));
        candidates.put(3, new Candidate(3, "Senior Java"));
    }

    public static Store instOf() {
        return INST;
    }

    public Collection<Post> findAllPosts() {
        return posts.values();
    }

    public Collection<Candidate> findAllCandidates() {
        return candidates.values();
    }
}
