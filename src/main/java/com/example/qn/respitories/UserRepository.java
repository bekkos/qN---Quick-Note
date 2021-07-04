package com.example.qn.respitories;


import com.example.qn.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

    @Autowired
    JdbcTemplate db;

    public void addUser(User user) {
        String sql = "INSERT INTO user (username, email, passwordhash) VALUES (?, ?, ?)";
        db.update(sql, user.getUsername(), user.getEmail(), user.getPasswordHash());
    }

    public boolean checkCredentials(String email, String passwordHash) {
        String sql = String.format("SELECT COUNT(*) FROM user WHERE email='%s' AND passwordHash='%s'", email, passwordHash);
        int amount = db.queryForObject(sql, Integer.class);

        if (amount > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkIfEmailTaken(String email) {
        String sql = String.format("SELECT COUNT(*) FROM user WHERE email='%s'", email);
        int amount = db.queryForObject(sql, Integer.class);

        if (amount > 0) {
            return true;
        } else {
            return false;
        }
    }

    public User getUserFromDatabase(String email) {
        String sql = String.format("SELECT * FROM user WHERE email='%s'", email);
        return db.queryForObject(sql, new BeanPropertyRowMapper<>(User.class));
    }
}
