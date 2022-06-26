package org.hbrs.se2.project.hellocar.control;

import org.hbrs.se2.builder.UserBuilder;
import org.hbrs.se2.project.hellocar.dao.UserDAO;
import org.hbrs.se2.project.hellocar.dtos.UserDTO;
import org.hbrs.se2.project.hellocar.dtos.impl.UserDTOImpl;
import org.hbrs.se2.project.hellocar.services.db.exceptions.DatabaseLayerException;
import org.hbrs.se2.project.hellocar.services.db.exceptions.ViewException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import static org.hbrs.se2.project.hellocar.util.Globals.Roles.STUDENT;

public class ManageExistingUserControlTest {

    private ManageExistingUserControl existingUserControl = null;
    private UserDAO userDAO = null;
    private String email1 = null;
    private String s1 = null;

    @BeforeEach
    public void init(){
        existingUserControl = new ManageExistingUserControl();
        userDAO = new UserDAO();
        email1 = "test@student.com";
        s1 = "Hallo123";
    }

    @Test
    void userDTOisNullThrowsRuntimeExceptionTest(){
        ViewException thrown = Assertions.assertThrows(ViewException.class,
                () -> existingUserControl.updateUserPassword(null, s1, email1)
        );

        Assertions.assertEquals("DTO ist null!", thrown.getReason());
    }

    @Test
    void unchangedNewPasswordTest(){
        UserDTOImpl userDTO = UserBuilder
                .getInstance()
                .createNewUser()
                .withEmail(email1)
                .withPassword(s1)
                .withConfirmPassword(s1)
                .build();

        DatabaseLayerException thrown = Assertions.assertThrows(DatabaseLayerException.class,
                () -> existingUserControl.updateUserPassword(userDTO, s1, email1)
        );

        Assertions.assertEquals("Neues Passwort entspricht dem alten Passwort!", thrown.getReason());
    }

    @Test
    void passwordNotEqualsPasswordConfirmThrowsDatabaseLayerException(){
        UserDTOImpl userDTO = UserBuilder
                .getInstance()
                .createNewUser()
                .withPassword(s1)
                .withConfirmPassword(s1+"3")
                .build();

        DatabaseLayerException thrown = Assertions.assertThrows(DatabaseLayerException.class,
                () -> existingUserControl.updateUserPassword(userDTO, s1+"4", email1)
        );

        Assertions.assertEquals("Neues Passwort und neues Passswort bestätigen stimmen nicht überein!",
                thrown.getReason());
    }

    @Test
    void passwordNotBoundToBeanThrowsDatabaseLayerException(){
        UserDTOImpl userDTO = UserBuilder
                .getInstance()
                .createNewUser()
                .withEmail(email1)
                .withPassword("")
                .withConfirmPassword("")
                .build();

        DatabaseLayerException thrown = Assertions.assertThrows(DatabaseLayerException.class,
                () -> existingUserControl.updateUserPassword(userDTO, s1+"4", email1)
        );

        Assertions.assertEquals("Passwort konnte nicht übertragen werden.",
                thrown.getReason());
    }

    @Test
    void successfulChangePasswordReturnsTrueAndDeleteUser() throws NoSuchAlgorithmException,
            InvalidKeySpecException {

        UserDTOImpl student = UserBuilder
                .getInstance()
                .createNewUser()
                .withRole(STUDENT)
                .withEmail(email1)
                .withFirstName("Max")
                .withLastName("Mustermann")
                .withPassword(s1)
                .withConfirmPassword(s1)
                .build();

        UserDTOImpl userChangePassword = UserBuilder
                .getInstance()
                .createNewUser().withPassword(s1+"4")
                .withConfirmPassword(s1+"4")
                .build();

        try{
            //User in die Datenbank einfuegen
            userDAO.insertUser(student, RegistrationControl.hashPassword(student.getPassword()));

            //Passwort aendern
            Assertions.assertTrue(existingUserControl.updateUserPassword(userChangePassword, s1, email1));

            // User wieder aus Datenbank entfernen
            Assertions.assertTrue(existingUserControl.deleteUser(student));
        } catch (DatabaseLayerException | ViewException ignore){}

    }

    @Test
    void successfulChangeUserDataReturnsTrueAndDeleteUser() {

        UserDTOImpl student = UserBuilder
                .getInstance()
                .createNewUser()
                .withRole(STUDENT)
                .withEmail(email1)
                .withFirstName("Max")
                .withLastName("Mustermann")
                .withPassword(s1)
                .withConfirmPassword(s1)
                .build();

        UserDTOImpl userToUpdate = UserBuilder
                .getInstance()
                .createNewUser()
                .withRole(STUDENT)
                .withEmail(email1)
                .withFirstName("Maxi")
                .withLastName("Mann")
                .withSemester(5)
                .withDescription("Hallo")
                .withSpecialization("CS")
                .withFaculty("Informatik")
                .build();

        try{
            //User in die Datenbank einfuegen
            userDAO.insertUser(student, student.getPassword());

            //User Updaten
            Assertions.assertTrue(existingUserControl.updateUser(userToUpdate));

            //User lesen
            UserDTO temp = userDAO.findUserByUserEmailAndPassword(student.getEmail(), student.getPassword());
            Assertions.assertEquals(userToUpdate.getFirstName(), temp.getFirstName());
            Assertions.assertEquals(userToUpdate.getLastName(), temp.getLastName());
            Assertions.assertEquals(userToUpdate.getDescription(), temp.getDescription());
            Assertions.assertEquals(userToUpdate.getSemester(), temp.getSemester());
            Assertions.assertEquals(userToUpdate.getSpecialization(), temp.getSpecialization());
            Assertions.assertEquals(userToUpdate.getFaculty(), temp.getFaculty());

            // User wieder aus Datenbank entfernen
            Assertions.assertTrue(existingUserControl.deleteUser(student));
        } catch (DatabaseLayerException | ViewException ignore){}

    }

    @Test
    void updateUserCatchUserDTONull(){
        ViewException thrown = Assertions.assertThrows(ViewException.class,
                () -> existingUserControl.updateUser(null)
        );

        Assertions.assertEquals("DTO ist null!", thrown.getReason());
    }

}
