package org.hbrs.se2.project.hellocar.test;

import org.hbrs.se2.project.hellocar.services.db.JDBCConnection;
import org.hbrs.se2.project.hellocar.services.db.exceptions.DatabaseLayerException;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.hbrs.se2.project.hellocar.dao.UserDAO;
import org.hbrs.se2.project.hellocar.dtos.UserDTO;
import org.hbrs.se2.project.hellocar.dtos.impl.UserDTOImpl;

import static org.junit.jupiter.api.Assertions.assertThrows;

class UserDAOTest {

    private UserDAO userDAO;

    @BeforeEach
    void init() {
        userDAO = new UserDAO();
    }


    @Test
    void roundTripCRUD() {
        // create new user instance
        UserDTOImpl newUser = new UserDTOImpl();
        newUser.setEmail("hp@test.de");
        newUser.setFirstName("Hans");
        newUser.setLastName("Peter");
        newUser.setRole("Student");
        newUser.setPassword("test");

        // create user in database
        try {
            userDAO.insertUser(newUser, newUser.getPassword());
        } catch (DatabaseLayerException e) {
            e.printStackTrace();
        }

        // search user with wrong password
        assertThrows(DatabaseLayerException.class, () -> {
            userDAO.findUserByUserEmailAndPassword("hp@test.de", "hallo");
        });

        // search user in database
        UserDTO user = null;
        try {
            user = userDAO.findUserByUserEmailAndPassword("hp@test.de", "test");
        } catch (DatabaseLayerException e) {
            e.printStackTrace();
        }

        assertEquals(newUser.getEmail(), user.getEmail());

        // update user in database
        newUser.setFirstName("Fritz");
        try {
            userDAO.updateUserByEmail(newUser);
        } catch (DatabaseLayerException e) {
            e.printStackTrace();
        }

        try {
            user = userDAO.findUserByUserEmailAndPassword("hp@test.de", "test");
        } catch (DatabaseLayerException e) {
            e.printStackTrace();
        }

        assertEquals(newUser.getFirstName(), user.getFirstName());

        // delete user in database
        try {
            userDAO.deleteUserByEmail("hp@test.de", "Student");
        } catch (DatabaseLayerException e) {
            e.printStackTrace();
        }

        assertThrows(DatabaseLayerException.class, () -> { userDAO.findUserByUserEmailAndPassword("hp@test.de", "test"); });
    }
}