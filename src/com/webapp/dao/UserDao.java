package com.webapp.dao;

import com.webapp.DataStore;
import com.webapp.entities.User;

import java.util.List;

// to test using DataStore class. Later will use SQL.
public class UserDao {
    public List<User> getUsers() {
        return DataStore.getUsers();
    }
}
