package com.webapp.managers;

import com.webapp.constants.GenderType;
import com.webapp.constants.UserType;
import com.webapp.dao.UserDao;
import com.webapp.entities.User;

import java.util.List;

// Singleton Pattern
public class UserManager {
    // static as getter is static
    private static UserManager userManagerInstance = new UserManager();
    // MVC policy => Manager/Model communicating with DAO
    private static UserDao userDao = new UserDao();

    // private constructor for Singleton
    private UserManager() {
    }

    // static as we cannot create an instance of UserManager
    public static UserManager getInstance() {
        return userManagerInstance;
    }

    public User createUser(long id,
                           String email,
                           String password,
                           String firstName,
                           String lastName,
                           GenderType gender,
                           UserType userType) {
        User user = new User();
        user.setId(id);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setGender(gender);
        user.setUserType(userType);

        return user;
    }

	public User getUser(long userId) {
		// TODO Auto-generated method stub
		return userDao.getUser(userId);
	}
}
