package ru.job4j.dream.store;

import org.apache.commons.dbcp2.BasicDataSource;
import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.City;
import ru.job4j.dream.model.Post;
import ru.job4j.dream.model.User;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PsqlStore implements Store {

    private static final Logger LOG = LoggerFactory.getLogger(PsqlStore.class.getName());

    private final BasicDataSource pool = new BasicDataSource();

    private PsqlStore() {
        Properties cfg = new Properties();
        try (InputStream in = PsqlStore.class.getClassLoader().getResourceAsStream("db.properties")) {
            cfg.load(in);
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
        System.out.println(cfg.getProperty("jdbc.url"));
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
        String query = "SELECT * FROM post ORDER BY created";
        return findPosts(query);
    }

    @Override
    public Collection<Candidate> findAllCandidates() {
        String query = "SELECT"
                     + " c.id AS id,"
                     + " c.name AS name,"
                     + " c.created AS created,"
                     + " c.city_id AS city_id,"
                     + " cities.name AS city_name "
                     + "FROM candidates AS c"
                     + " LEFT JOIN cities"
                     + "  ON cities.id = c.city_id "
                     + "ORDER BY created";
        return findCandidates(query);
    }

    @Override
    public Collection<City> findAllCities() {
        List<City> cities = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM cities ORDER BY name")) {
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    cities.add(new City(
                            resultSet.getInt("id"),
                            resultSet.getString("name")
                    ));
                }
            }
        } catch (Exception e) {
            LOG.error("Database query failed", e);
        }
        return cities;
    }

    @Override
    public Collection<Post> findPostsForLastDay() {
        String query = "SELECT * "
                     + "FROM post "
                     + "WHERE created BETWEEN NOW() - INTERVAL '1 DAY' AND NOW() "
                     + "ORDER BY created DESC";
        return findPosts(query);
    }

    @Override
    public Collection<Candidate> findCandidatesForLastDay() {
        String query = "SELECT"
                     + " c.id AS id,"
                     + " c.name AS name,"
                     + " c.created AS created,"
                     + " c.city_id AS city_id,"
                     + " cities.name AS city_name "
                     + "FROM candidates AS c"
                     + " LEFT JOIN cities"
                     + "  ON cities.id = c.city_id "
                     + "WHERE  c.created BETWEEN NOW() - INTERVAL '1 DAY' AND NOW() "
                     + "ORDER BY created DESC ";
        return findCandidates(query);
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
    public void save(User user) {
        if (user.getId() == 0) {
            create(user);
        } else {
            update(user);
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
            LOG.error("Database query failed", e);
        }
        return post;
    }

    @Override
    public Candidate findCandidateById(int id) {
        Candidate candidate = null;
        String query = "SELECT"
                + " c.id AS id,"
                + " c.name AS name,"
                + " c.created AS created,"
                + " c.city_id AS city_id,"
                + " cities.name AS city_name "
                + "FROM candidates AS c"
                + " LEFT JOIN cities"
                + "  ON cities.id = c.city_id "
                + "WHERE c.id = ?";
        try (Connection cn = pool.getConnection();
             PreparedStatement statement = cn.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    candidate = getCandidate(resultSet);
                }
            }
        } catch (Exception e) {
            LOG.error("Database query failed", e);
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
            LOG.error("Database query failed", e);
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
            LOG.error("Database query failed", e);
        }
    }

    @Override
    public User findByEmail(String email) {
        User user = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement statement = cn.prepareStatement("SELECT * FROM users WHERE email = ?")) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    user = getUser(resultSet);
                }
            }
        } catch (Exception e) {
            LOG.error("Database query failed", e);
        }
        return user;
    }

    private void create(Post post) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "INSERT INTO post(name, description, created) VALUES (?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, post.getName());
            ps.setString(2, post.getDescription());
            ps.setTimestamp(3, Timestamp.valueOf(post.getCreated()));
            ps.executeUpdate();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    post.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.error("Database query failed", e);
        }
    }

    private void create(Candidate candidate) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "INSERT INTO candidates(name, city_id, created) VALUES (?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, candidate.getName());
            ps.setInt(2, candidate.getCity().getId());
            ps.setTimestamp(3, Timestamp.valueOf(candidate.getCreated()));
            ps.executeUpdate();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    candidate.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.error("Database query failed", e);
        }
    }

    private void create(User user) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "INSERT INTO users(name, email, password) VALUES (?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.executeUpdate();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    user.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.error("Database query failed", e);
        }
    }

    private void update(Post post) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "UPDATE post SET name = ?, description = ? WHERE id = ?")) {
            ps.setString(1, post.getName());
            ps.setString(2, post.getDescription());
            ps.setInt(3, post.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            LOG.error("Database query failed", e);
        }
    }

    private void update(Candidate candidate) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "UPDATE candidates SET name = ?, city_id = ? WHERE id = ?")) {
            ps.setString(1, candidate.getName());
            ps.setInt(2, candidate.getCity().getId());
            ps.setInt(3, candidate.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            LOG.error("Database query failed", e);
        }
    }

    private void update(User user) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "UPDATE users SET name = ?, email = ?, password = ? WHERE id = ?")) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setInt(4, user.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            LOG.error("Database query failed", e);
        }
    }

    private List<Post> findPosts(String query) {
        List<Post> posts = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(query)) {
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    posts.add(
                            getPost(resultSet)
                    );
                }
            }
        } catch (Exception e) {
            LOG.error("Database query failed", e);
        }
        return posts;
    }

    private List<Candidate> findCandidates(String query) {
        List<Candidate> candidates = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(query)) {
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    candidates.add(
                            getCandidate(resultSet)
                    );
                }
            }
        } catch (Exception e) {
            LOG.error("Database query failed", e);
        }
        return candidates;
    }

    private Post getPost(ResultSet resultSet) throws SQLException {
        return new Post(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getTimestamp("created").toLocalDateTime()
        );
    }

    private Candidate getCandidate(ResultSet resultSet) throws SQLException {
        return new Candidate(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                new City(
                        resultSet.getInt("city_id"),
                        resultSet.getString("city_name")
                ),
                resultSet.getTimestamp("created").toLocalDateTime()
        );
    }

    private User getUser(ResultSet resultSet) throws SQLException {
        return new User(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("email"),
                resultSet.getString("password")
        );
    }
}