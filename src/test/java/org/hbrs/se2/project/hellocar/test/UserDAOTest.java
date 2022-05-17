package org.hbrs.se2.project.hellocar.test;

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

    @Test
    void roundTripCRUD() {
        // create new user instance
        UserDTOImpl newUser = new UserDTOImpl();
        newUser.setUserId(0);
        newUser.setFirstName("Hans");
        newUser.setLastName("Peter");

        List<RolleDTO> roles = new ArrayList<>();
        RolleDTOImpl role = new RolleDTOImpl();
        role.setBezeichnung("User");
        roles.add(role);
        newUser.setRoles(roles);

        // create user in database
        userDAO.createUser(newUser, "test");

        // search user with wrong password
        assertThrows(Exception.class, () -> { userDAO.findUser(0, "hallo"); });

        // search user in database
        UserDTO user = userDAO.findUser(0, "test");

        assertEquals(newUser.getFirstName(), user.getFirstName());
        assertEquals(newUser.getLastName(), user.getLastName());

        // update user in database
        newUser.setFirstName("Fritz");
        userDAO.updateUser(newUser);

        user = userDAO.findUser("0", "test");

        assertEquals(newUser.getFirstName(), user.getFirstName());
        assertEquals(newUser.getLastName(), user.getLastName());

        // delete user in database
        userDAO.deleteUser("0");

        assertThrows(Exception.class, () -> { userDAO.findUser("0", "test"); });
    }

    @Test
    @Ignore("Test muss in RegistrationControllerTest")
    void noFirstNameTest() {
        // create new user instance
        UserDTOImpl newUser = new UserDTOImpl();
        newUser.setUserId(0);
        newUser.setFirstName(""); // ungültig
        newUser.setLastName("Peter");

        List<RolleDTO> roles = new ArrayList<>();
        RolleDTOImpl role = new RolleDTOImpl();
        role.setBezeichnung("User");
        roles.add(role);
        newUser.setRoles(roles);

        assertThrows(Exception.class, () -> { userDAO.createUser(newUser, "test"); });
    }
}