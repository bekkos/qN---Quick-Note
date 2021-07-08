package com.example.qn.repositories;


import com.example.qn.models.Note;
import com.example.qn.models.Notebook;
import com.example.qn.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class DatabaseRepository {

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

    public ArrayList<Notebook> getNotebooksFromDatabase(String email) {
        User user = getUserFromDatabase(email);
        String sql = String.format("SELECT * FROM notebook WHERE owner_id=%s", user.getId());
        List<Notebook> notebooks = db.query(sql, new BeanPropertyRowMapper<>(Notebook.class));

        return (ArrayList<Notebook>) notebooks;
    }

    public ArrayList<Note> getNotesFromDatabase(int notebook_id) {
        String sql = String.format("SELECT * FROM note WHERE notebook_id=%s", notebook_id);
        List<Note> notebooks = db.query(sql, new BeanPropertyRowMapper<>(Note.class));

        return (ArrayList<Note>) notebooks;
    }




}