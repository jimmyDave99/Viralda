package org.hbrs.se2.project.hellocar.control;

import org.hbrs.se2.builder.UserBuilder;
import org.hbrs.se2.project.hellocar.dao.UserDAO;
import org.hbrs.se2.project.hellocar.dtos.impl.UserDTOImpl;
import org.hbrs.se2.project.hellocar.services.db.exceptions.DatabaseLayerException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import static org.hbrs.se2.project.hellocar.util.Globals.Roles.STUDENT;
import static org.hbrs.se2.project.hellocar.util.Globals.Roles.UNTERNEHMEN;

public class RegistrationControlTest {

    private RegistrationControl registrationControl = null;
    private UserDAO userDAO = null;

    @BeforeEach
    public void init(){
        registrationControl = new RegistrationControl();
        userDAO = new UserDAO();
    }

    @Test
    void emptyPasswordTest() {
        UserDTOImpl user = UserBuilder
                .getInstance()
                .createNewUser()
                .withRole(STUDENT)
                .withEmail("student@email.de")
                .withFirstName("Max")
                .withLastName("Seth")
                .withPassword("")
                .withConfirmPassword("")
                .build();

        DatabaseLayerException thrown = Assertions.assertThrows(
                DatabaseLayerException.class,
                () -> registrationControl.createUser(user)
        );

        Assertions.assertEquals("Password missing", thrown.getReason());
    }

    @Test
    void emptyLastNameFieldTest() {
        UserDTOImpl user = UserBuilder
                .getInstance()
                .createNewUser()
                .withRole(STUDENT)
                .withEmail("student@email.de")
                .withFirstName("Rich")
                .withLastName("")
                .withPassword("QK21vrNe")
                .withConfirmPassword("QK21vrNe")
                .build();

        DatabaseLayerException thrown = Assertions.assertThrows(
                DatabaseLayerException.class,
                () -> registrationControl.createUser(user)
        );

        Assertions.assertEquals("Lastname missing", thrown.getReason());
    }

    @Test
    void badPasswordTest() {
        UserDTOImpl user2 = UserBuilder
                .getInstance()
                .createNewUser()
                .withRole(UNTERNEHMEN)
                .withEmail("office@email.de")
                .withCompanyName("Viralda")
                .withPassword("pass12")
                .withConfirmPassword("pass12")
                .build();

        DatabaseLayerException thrown = Assertions.assertThrows(
                DatabaseLayerException.class,
                () -> registrationControl.createUser(user2)
        );

        Assertions.assertEquals("invalid password", thrown.getReason());
    }

    @Test
    void emptyCompanyNameFieldTest() {
        UserDTOImpl user3 = UserBuilder
                .getInstance()
                .createNewUser()
                .withRole(UNTERNEHMEN)
                .withEmail("office@email.de")
                .withCompanyName("")
                .withPassword("pass1QA32")
                .withConfirmPassword("pass1QA32")
                .build();

        DatabaseLayerException thrown = Assertions.assertThrows(
                DatabaseLayerException.class,
                () -> registrationControl.createUser(user3)
        );

        Assertions.assertEquals("company name missing", thrown.getReason());
    }

    @Test
    void differentPasswordTest() {
        UserDTOImpl student = UserBuilder
                .getInstance()
                .createNewUser()
                .withRole(STUDENT)
                .withEmail("brich@student.de")
                .withFirstName("Bob")
                .withLastName("Rich")
                .withPassword("Hallo153")
                .withConfirmPassword("Hallo124")
                .build();

        DatabaseLayerException thrown = Assertions.assertThrows(
                DatabaseLayerException.class,
                () -> registrationControl.createUser(student)
        );

        Assertions.assertEquals("password are different", thrown.getReason());
    }

    @Test
    void registrationTest() throws NoSuchAlgorithmException, InvalidKeySpecException {

        try{
            UserDTOImpl student = UserBuilder
                    .getInstance()
                    .createNewUser()
                    .withRole(STUDENT)
                    .withEmail("mmuster@student.de")
                    .withFirstName("Max")
                    .withLastName("Mustermann")
                    .withPassword("Hallo123")
                    .withConfirmPassword("Hallo123")
                    .build();

            UserDTOImpl company = UserBuilder
                    .getInstance()
                    .createDefaultUserCompany()
                    .build();

            Assertions.assertTrue(registrationControl.createUser(student));
            Assertions.assertTrue(registrationControl.createUser(company));
        }catch (DatabaseLayerException ignored){}
    }
}
