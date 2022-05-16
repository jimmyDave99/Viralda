package org.hbrs.se2.project.hellocar.test;

import org.hbrs.se2.project.hellocar.dao.UserDAO;
import org.hbrs.se2.project.hellocar.dtos.UserDTO;
import org.hbrs.se2.project.hellocar.entities.User;
import org.hbrs.se2.project.hellocar.repository.UserRepository;
import org.hbrs.se2.project.hellocar.services.db.exceptions.DatabaseLayerException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

@SpringBootTest
class HellocarApplicationTests {

    @Autowired
    private UserRepository userRepository;


    @Test
    void testUserDTOByAttribute() {
        UserDTO personDTO = userRepository.getUserByOccupation("Professor").get(0);
        System.out.println(personDTO.getFirstName());
        assertEquals("Sascha", personDTO.getFirstName());
        assertEquals(1 , personDTO.getId());
    }

    @Test
    void testUserDTOByPassword() {
        UserDTO userDTO = userRepository.findUserByEmailAndPassword("user@test.de" , "123");
        System.out.println(userDTO.getFirstName());
        assertEquals("Sascha", userDTO.getFirstName());
    }


    @Test
    void testPersonLoad() {
        Optional<User> wrapper = userRepository.findById(1);
        if ( wrapper.isPresent() ) {
            User user = wrapper.get();
            assertEquals("Alda" , user.getLastName());
        }
    }


    @Test
    void testFindUserWithJDBC() {
        UserDAO userDAO = new UserDAO();
        try {
            UserDTO userDTO = userDAO.findUserByUserEmailAndPassword("user@test.de" , "123");
            System.out.println(userDTO.toString());

            assertEquals("Sascha", userDTO.getFirstName());
        } catch (DatabaseLayerException e) {
            e.printStackTrace();
        }

    }

}
