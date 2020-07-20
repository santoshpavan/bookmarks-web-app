package com.webapp.dao;

import com.webapp.DataStore;
import com.webapp.entities.User;

// to test using DataStore class. Later will use SQL.
public class UserDao {
    public User[] getUsers() {
        return DataStore.getUsers();
    }
}
