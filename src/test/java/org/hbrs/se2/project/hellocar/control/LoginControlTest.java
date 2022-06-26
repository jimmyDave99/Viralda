package org.hbrs.se2.project.hellocar.control;

import org.hbrs.se2.project.hellocar.control.exception.DatabaseUserException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class LoginControlTest {
    LoginControl  loginControl;
    @BeforeEach
    public void init(){
      loginControl = new  LoginControl();
    }

    @Test
    void CorrectUsernameAndfalsePasswortTest() throws DatabaseUserException, NoSuchAlgorithmException {
        // Bei unexistierte username und passwort
        DatabaseUserException thrown = Assertions.assertThrows(
                DatabaseUserException.class, () -> loginControl.authentificate("mmuster@student.de","dfd")
        );
//        Assertions.assertThrows(DatabaseUserException.class, () ->{
//            loginControl.authentificate("testusername","testpasswort");
//        });
        Assertions.assertEquals("A failure occured while", thrown.getReason());

    }

    @Test
    void authentificateTest() throws DatabaseUserException, NoSuchAlgorithmException, InvalidKeySpecException {
        // Bei Existierte username und passwort
        Assertions.assertTrue(loginControl.authentificate("mmuster@student.de","Hallo123"));
    }
}
