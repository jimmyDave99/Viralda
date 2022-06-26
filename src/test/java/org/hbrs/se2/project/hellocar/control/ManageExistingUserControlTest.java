package org.hbrs.se2.project.hellocar.control;

import org.hbrs.se2.builder.UserBuilder;
import org.hbrs.se2.project.hellocar.dao.UserDAO;
import org.hbrs.se2.project.hellocar.dtos.UserDTO;
import org.hbrs.se2.project.hellocar.dtos.impl.UserDTOImpl;
import org.hbrs.se2.project.hellocar.services.db.exceptions.DatabaseLayerException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import static org.hbrs.se2.project.hellocar.util.Globals.Roles.STUDENT;
import static org.hbrs.se2.project.hellocar.util.Globals.Roles.UNTERNEHMEN;


class ManageExistingUserControlTest {

    private ManageExistingUserControl manageExistingUserControl = null;

    private RegistrationControl registrationControl = null;

    private LoginControl loginControl = null;

    private UserDAO userDAO = null;

    private UserDTOImpl student = null;

    private final String NEW_FIRSTNAME = "John";

    private final String NEW_LASTNAME = "Doe";

    private final String NEW_FACULTY = "Informatik";

    private final int NEW_SEMESTER = 4;

    private final String NEW_SPECIALIZATION = "Data Science";

    private final String EMAIL = "mmuster@student.de";

    private final String DESCRIPTION = "...";

    private final String SECRET = "Hallo123";

    @BeforeEach
    public void init(){

        manageExistingUserControl = new ManageExistingUserControl();

        registrationControl = new RegistrationControl();

        loginControl = new LoginControl();

        userDAO = new UserDAO();

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
    void updateUserStudentTest() throws DatabaseLayerException, NoSuchAlgorithmException, InvalidKeySpecException {

        Assertions.assertTrue(manageExistingUserControl.updateUser(student));

        UserDTO user = userDAO.findUserByUserEmailAndPassword(EMAIL, RegistrationControl.hashPassword(SECRET));

        Assertions.assertEquals(EMAIL, user.getEmail());
        Assertions.assertEquals(STUDENT, user.getRole());
        Assertions.assertEquals(DESCRIPTION, user.getDescription());
        Assertions.assertEquals(NEW_FIRSTNAME, user.getFirstName());
        Assertions.assertEquals(NEW_LASTNAME, user.getLastName());
        Assertions.assertEquals(NEW_FACULTY, user.getFaculty());
        Assertions.assertEquals(NEW_SEMESTER, user.getSemester());
        Assertions.assertEquals(NEW_SPECIALIZATION, user.getSpecialization());
    }

    @Test
    void updateUserCompanyTest() throws DatabaseLayerException, NoSuchAlgorithmException, InvalidKeySpecException {

        UserDTOImpl company = UserBuilder
                .getInstance()
                .createDefaultUserCompany()
                .build();

        Assertions.assertTrue(manageExistingUserControl.updateUser(company));

        UserDTO user = userDAO.findUserByUserEmailAndPassword(company.getEmail(), RegistrationControl.hashPassword(company.getPassword()));

        Assertions.assertEquals(company.getEmail(), user.getEmail());
        Assertions.assertEquals(company.getRole(), user.getRole());
        Assertions.assertEquals(company.getDescription(), user.getDescription());
        Assertions.assertEquals(company.getBranche(), user.getBranche());
        Assertions.assertEquals(company.getCompanyName(), user.getCompanyName());
    }



    @Test
    void deleteUserTest() throws DatabaseLayerException, NoSuchAlgorithmException, InvalidKeySpecException {

        UserDTOImpl stud = UserBuilder
                .getInstance()
                .createNewUser()
                .withEmail("bobrich@student.de")
                .withRole(STUDENT)
                .withPassword(SECRET)
                .withConfirmPassword(SECRET)
                .withFirstName("Bob")
                .withLastName("Rich")
                .build();

        UserDTOImpl company = UserBuilder
                .getInstance()
                .createNewUser()
                .withEmail("companytest@company.de")
                .withCompanyName("company")
                .withRole(UNTERNEHMEN)
                .withPassword(SECRET)
                .withConfirmPassword(SECRET)
                .withBranche("IT")
                .build();

        Assertions.assertTrue(registrationControl.createUser(stud));
        Assertions.assertTrue(registrationControl.createUser(company));
        Assertions.assertTrue(manageExistingUserControl.deleteUser(stud));
        Assertions.assertTrue(manageExistingUserControl.deleteUser(company));
    }

    @Test
    void updateProfilPictureTest() {
        Assertions.assertFalse(manageExistingUserControl.updateProfilePicture(student));
    }
}