package org.hbrs.se2.project.hellocar.test;

import org.hbrs.se2.project.hellocar.control.LoginControl;
import org.hbrs.se2.project.hellocar.control.exception.DatabaseUserException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.constraints.AssertTrue;
import java.security.NoSuchAlgorithmException;

public class LoginControlTest {

    @BeforeEach
    public void init(){
      LoginControl  loginControl = new  LoginControl();
    }

    @Test
    void authentificateTest() throws DatabaseUserException, NoSuchAlgorithmException {
        LoginControl  loginControl = new  LoginControl();
        Assertions.assertFalse(loginControl.authentificate("testusename","testpasswort"));
    }
}
