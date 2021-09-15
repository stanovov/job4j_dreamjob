package ru.job4j.dream.store;

import org.apache.commons.dbcp2.BasicDataSource;
import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Post;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class PsqlStore implements Store {

    private final BasicDataSource pool = new BasicDataSource();

    private PsqlStore() {
        Properties cfg = new Properties();
        try (BufferedReader io = new BufferedReader(new FileReader("db.properties"))) {
            cfg.load(io);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        pool.setDriverClassName(cfg.getProperty("jdbc.driver"));
        pool.setUrl(cfg.getProperty("jdbc.url"));
        pool.setUsername(cfg.getProperty("jdbc.username"));
        pool.setPassword(cfg.getProperty("jdbc.password"));
        pool.setMinIdle(5);
        pool.setMaxIdle(10);
        pool.setMaxOpenPreparedStatements(100);
    }

    private static final class Lazy {
        private static final Store INST = new PsqlStore();
    }

    public static Store instOf() {
        return Lazy.INST;
    }

    @Override
    public Collection<Post> findAllPosts() {
        List<Post> posts = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM post")) {
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    posts.add(
                            getPost(resultSet)
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return posts;
    }

    @Override
    public Collection<Candidate> findAllCandidates() {
        List<Candidate> candidates = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM candidates")) {
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    candidates.add(
                            getCandidate(resultSet)
                    );
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return candidates;
    }

    @Override
    public void save(Post post) {
        if (post.getId() == 0) {
            create(post);
        } else {
            update(post);
        }
    }

    @Override
    public void save(Candidate candidate) {
        if (candidate.getId() == 0) {
            create(candidate);
        } else {
            update(candidate);
        }
    }

    @Override
    public Post findPostById(int id) {
        Post post = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement statement = cn.prepareStatement("SELECT * FROM post WHERE id = ?")) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    post = getPost(resultSet);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return post;
    }

    @Override
    public Candidate findCandidateById(int id) {
        Candidate candidate = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement statement = cn.prepareStatement("SELECT * FROM candidates WHERE id = ?")) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    candidate = getCandidate(resultSet);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return candidate;
    }

    @Override
    public void deletePost(int id) {
        try (Connection cn = pool.getConnection();
             PreparedStatement statement
                     = cn.prepareStatement("DELETE FROM post WHERE id = ?")) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteCandidate(int id) {
        try (Connection cn = pool.getConnection();
             PreparedStatement statement
                     = cn.prepareStatement("DELETE FROM candidates WHERE id = ?")) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void create(Post post) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "INSERT INTO post(name) VALUES (?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, post.getName());
            ps.executeUpdate();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    post.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void update(Post post) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "UPDATE post SET name = ? WHERE id = ?")) {
            ps.setString(1, post.getName());
            ps.setInt(2, post.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void create(Candidate candidate) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "INSERT INTO candidates(name) VALUES (?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, candidate.getName());
            ps.executeUpdate();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    candidate.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void update(Candidate candidate) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "UPDATE candidates SET name = ? WHERE id = ?")) {
            ps.setString(1, candidate.getName());
            ps.setInt(2, candidate.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Post getPost(ResultSet resultSet) throws SQLException {
        return new Post(
                resultSet.getInt("id"),
                resultSet.getString("name")
        );
    }

    private Candidate getCandidate(ResultSet resultSet) throws SQLException {
        return new Candidate(
                resultSet.getInt("id"),
                resultSet.getString("name")
        );
    }

}