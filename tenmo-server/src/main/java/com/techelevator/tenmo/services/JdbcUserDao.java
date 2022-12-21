package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.security.SecurityUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcUserDao implements UserDao {

    private static final BigDecimal STARTING_BALANCE = new BigDecimal("1000.00");
    private JdbcTemplate jdbcTemplate;

    public JdbcUserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int findIdByUsername(String username) {
        String sql = "SELECT user_id FROM tenmo_user WHERE username LIKE ?;";
        Integer id = jdbcTemplate.queryForObject(sql, Integer.class, username);
        if (id != null) {
            return id;
        } else {
            return -1;
        }
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT user_id, username, password_hash FROM tenmo_user;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while (results.next()) {
            User user = mapRowToUser(results);
            users.add(user);
        }
        return users;
    }

    @Override
    public User findByUsername(String username) throws UsernameNotFoundException {
        String sql = "SELECT user_id, username, password_hash FROM tenmo_user WHERE username LIKE ?;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, username);
        if (rowSet.next()) {
            return mapRowToUser(rowSet);
        }
        throw new UsernameNotFoundException("User " + username + " was not found.");
    }

    @Override
    @Modifying
    @Transactional
    public boolean create(String username, String password) {

        // create user
        String sql = "INSERT INTO tenmo_user (username, password_hash) VALUES (?, ?);";
        String password_hash = new BCryptPasswordEncoder().encode(password);
        int rowsAffected;
        try {
            rowsAffected = jdbcTemplate.update(sql, username, password_hash);

        } catch (DataAccessException e) {
            //TODO - replace System.err with Logger
            System.err.println("Failed to register new user: "+e.getMessage());
            return false;
        }
        if(rowsAffected == 0){
            return false;
        }
        // create account
        sql ="SELECT LAST_INSERT_ID()";
       String sqlbalance = "INSERT INTO account (user_id, balance) values(?, ?)";
        Integer newUserId = jdbcTemplate.queryForObject(sql, Integer.class);
        try {
            jdbcTemplate.update(sqlbalance, newUserId, STARTING_BALANCE);
        } catch (DataAccessException e) {
            //TODO - replace System.err with Logger
            System.err.println("Failed to initialize account balance: "+ e.getMessage());
            return false;
        }
        return true;
    }

    //Returns a list of users excluding current user.
    @Override
    public List<User> findTransferList() {

        String username = SecurityUtils.getCurrentUsername().map(Object::toString).orElse("");
        List<User> users = new ArrayList<>();
        String sql = "SELECT user_id, username, password_hash FROM tenmo_user WHERE username != ?;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, username);
        while (results.next()) {
            User user = mapRowToUser(results);
            users.add(user);
        }
        return users;
    }

    @Override
    public User findById(int to) {

        String sql = "SELECT * FROM tenmo_user WHERE user_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, to);
        if (results.next()) {
            return mapRowToUser(results);
        }
        throw new RuntimeException("User " + to + " was not found.");
    }


    @Override
    public User findUserByAccountid(int id) {
        String sql = "SELECT *\n" +
                "FROM tenmo_user\n" +
                "JOIN account\n" +
                "ON account.user_id = tenmo_user.user_id\n" +
                "WHERE account.account_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
        if (results.next()) {
            return mapRowToUser(results);
        }
        throw new RuntimeException("User was not found");
    }

    private User mapRowToUser(SqlRowSet rs) {
        User user = new User();
        user.setId(rs.getLong("user_id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password_hash"));
        user.setActivated(true);
        user.setAuthorities("USER");
        return user;
    }
}
