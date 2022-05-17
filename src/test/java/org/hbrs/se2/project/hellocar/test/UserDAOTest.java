package org.hbrs.se2.project.hellocar.test;

import org.hbrs.se2.project.hellocar.services.db.exceptions.DatabaseLayerException;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
/*
    @Test
    @Ignore
    void roundTripCRUD() {
        // create new user instance
        UserDTOImpl newUser = new UserDTOImpl();
        newUser.setUserId(0);
        newUser.setEmail("hp@test.de");
        newUser.setFirstName("Hans");
        newUser.setLastName("Peter");
        newUser.setRole("User");

        // create user in database
        try {
            userDAO.insertUser(newUser, "test");
        } catch (DatabaseLayerException e) {
            e.printStackTrace();
        }

        // search user with wrong password
        assertThrows(DatabaseLayerException.class, () -> { userDAO.findUserByUserEmailAndPassword("hp@test.de", "hallo"); });

        // search user in database
        UserDTO user = null;
        try {
            user = userDAO.findUserByUserEmailAndPassword("hp@test.de", "test");
        } catch (DatabaseLayerException e) {
            e.printStackTrace();
        }

        assertEquals(newUser.getFirstName(), user.getFirstName());
        assertEquals(newUser.getLastName(), user.getLastName());

        // update user in database
        newUser.setFirstName("Fritz");
        userDAO.updateUser(newUser);

        user = userDAO.findUserByUserEmailAndPassword(0, "test");

        assertEquals(newUser.getFirstName(), user.getFirstName());
        assertEquals(newUser.getLastName(), user.getLastName());

        // delete user in database
        userDAO.deleteUser("0");

        assertThrows(DatabaseLayerException.class, () -> { userDAO.findUserByUserEmailAndPassword("hp@test.de", "test"); });
    }
*/
    @Test
    @Ignore("Test muss in RegistrationControllerTest")
    void noFirstNameTest() {
        // create new user instance
        UserDTOImpl newUser = new UserDTOImpl();
        newUser.setUserId(0);
        newUser.setFirstName(""); // ungültig
        newUser.setLastName("Peter");
        newUser.setRole("");

        assertThrows(DatabaseLayerException.class, () -> { userDAO.insertUser(newUser, "test"); });
    }
}