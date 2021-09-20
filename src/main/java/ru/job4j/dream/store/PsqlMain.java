package ru.job4j.dream.store;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.City;
import ru.job4j.dream.model.Post;

public class PsqlMain {
    public static void main(String[] args) {
        Store store = PsqlStore.instOf();
        Post newPost = new Post(0, "Java Job", "");
        Candidate newCandidate = new Candidate(0, "Java Candidate", new City(1));
        store.save(newPost);
        store.save(newCandidate);
        for (Post post : store.findAllPosts()) {
            System.out.println(post.getId() + " " + post.getName());
        }
        for (Candidate candidate : store.findAllCandidates()) {
            System.out.println(candidate.getId() + " " + candidate.getName());
        }
        Post foundPost = store.findPostById(newPost.getId());
        System.out.printf("Found post by ID %d : %s%n", foundPost.getId(), foundPost.getName());
        Candidate foundCandidate = store.findCandidateById(newCandidate.getId());
        System.out.printf("Found candidate by ID %d : %s%n", foundCandidate.getId(), foundCandidate.getName());
        System.out.println("Delete post with ID " + newPost.getId());
        store.deletePost(newPost.getId());
        System.out.println("Delete candidate with ID " + newCandidate.getId());
        store.deleteCandidate(foundCandidate.getId());
    }
}