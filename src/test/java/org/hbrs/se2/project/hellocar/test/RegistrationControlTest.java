package org.hbrs.se2.project.hellocar.test;

import org.hbrs.se2.builder.UserBuilder;
import org.hbrs.se2.project.hellocar.control.RegistrationControl;
import org.hbrs.se2.project.hellocar.dtos.impl.UserDTOImpl;
import org.hbrs.se2.project.hellocar.services.db.exceptions.DatabaseLayerException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.security.NoSuchAlgorithmException;

public class RegistrationControlTest {

    public static final String STUDENT = "Student";
    public static final String UNTERNEHMEN = "Unternehmen";

    private RegistrationControl registrationControl = null;

    @BeforeEach
    public void init(){
        registrationControl = new RegistrationControl();
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
                .build();

        DatabaseLayerException thrown = Assertions.assertThrows(
                DatabaseLayerException.class,
                () -> registrationControl.createUser(user3)
        );

        Assertions.assertEquals("company name missing", thrown.getReason());
    }

    @Test
    void registrationTest() throws DatabaseLayerException, NoSuchAlgorithmException {

        UserDTOImpl user4 = UserBuilder
                .getInstance()
                .createNewUser()
                .withRole(STUDENT)
                .withEmail("student@email.de")
                .withFirstName("Harry")
                .withLastName("Paul")
                .withPassword("QK21vrNe")
                .build();

        Assertions.assertTrue(registrationControl.createUser(user4));

    }
}
