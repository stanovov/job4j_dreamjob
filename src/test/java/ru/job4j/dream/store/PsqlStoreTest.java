package ru.job4j.dream.store;

import org.junit.Test;
import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.City;
import ru.job4j.dream.model.Post;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class PsqlStoreTest {
    @Test
    public void whenPostCreateAndUpdateAndFindByIdAndDeleteIt() {
        Store store = PsqlStore.instOf();
        Post newPost = new Post(0, "Java Job", "Desc");
        store.save(newPost);
        int idPost = newPost.getId();
        assertThat(
                store.findPostById(idPost).getName(),
                is(newPost.getName())
        );
        Post updatedPost = new Post(idPost, "Python Job", "Desc");
        store.save(updatedPost);
        assertThat(
                store.findPostById(idPost).getName(),
                is(updatedPost.getName())
        );
        store.deletePost(idPost);
        assertThat(
                store.findPostById(idPost),
                is(nullValue())
        );
    }

    @Test
    public void whenCandidateCreateAndUpdateAndFindByIdAndDeleteIt() {
        Store store = PsqlStore.instOf();
        City moscow = new City(1);
        Candidate newCandidate = new Candidate(0, "Java Developer", moscow);
        store.save(newCandidate);
        int idCandidate = newCandidate.getId();
        assertThat(
                store.findCandidateById(idCandidate).getName(),
                is(newCandidate.getName())
        );
        Candidate updatedCandidate = new Candidate(idCandidate, "Python Developer", moscow);
        store.save(updatedCandidate);
        assertThat(
                store.findCandidateById(idCandidate).getName(),
                is(updatedCandidate.getName())
        );
        store.deleteCandidate(idCandidate);
        assertThat(
                store.findCandidateById(idCandidate),
                is(nullValue())
        );
    }
}