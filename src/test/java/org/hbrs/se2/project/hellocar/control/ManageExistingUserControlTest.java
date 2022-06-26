package org.hbrs.se2.project.hellocar.control;

import org.hbrs.se2.builder.UserBuilder;
import org.hbrs.se2.project.hellocar.dao.UserDAO;
import org.hbrs.se2.project.hellocar.dtos.impl.UserDTOImpl;
import org.hbrs.se2.project.hellocar.services.db.exceptions.DatabaseLayerException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hbrs.se2.project.hellocar.util.Globals.Roles.STUDENT;


class ManageExistingUserControlTest {

    private ManageExistingUserControl manageExistingUserControl = null;

    private UserDTOImpl student = null;

    private final String NEW_FIRSTNAME = "John";

    private final String NEW_LASTNAME = "Doe";

    private final String NEW_FACULTY = "Informatik";

    private final int NEW_SEMESTER = 4;

    private final String NEW_SPECIALIZATION = "Data Science";

    private final String EMAIL = "mmuster@student.de";

    private final String DESCRIPTION = "...";

    private final String SECRET = "...";

    @BeforeEach
    public void init(){

        manageExistingUserControl = new ManageExistingUserControl();

        student = UserBuilder
                .getInstance()
                .createNewUser()
                .withEmail(EMAIL)
                .withDescription(DESCRIPTION)
                .withRole(STUDENT)
                .withFirstName(NEW_FIRSTNAME)
                .withLastName(NEW_LASTNAME)
                .withFaculty(NEW_FACULTY)
                .withSemester(NEW_SEMESTER)
                .withSpecialization(NEW_SPECIALIZATION)
                .withPassword(SECRET)
                .build();
    }

    @Test
    void updateUserPassword()  {

        DatabaseLayerException thrown = Assertions.assertThrows(
                DatabaseLayerException.class,
                () -> manageExistingUserControl.updateUserPassword(student, SECRET, EMAIL)
        );

        Assertions.assertEquals("Neues Passwort entsprciht dem alten Passwort!", thrown.getReason());
    }

    @Test
    void updateUserTest() throws DatabaseLayerException {

        Assertions.assertTrue(manageExistingUserControl.updateUser(student));

    }
}