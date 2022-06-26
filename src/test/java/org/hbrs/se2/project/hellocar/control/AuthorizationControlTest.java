package org.hbrs.se2.project.hellocar.control;

import org.hbrs.se2.builder.UserBuilder;
import org.hbrs.se2.project.hellocar.dtos.impl.UserDTOImpl;
import org.hbrs.se2.project.hellocar.util.Globals;
import org.hbrs.se2.project.hellocar.views.LandingPageStudentView;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hbrs.se2.project.hellocar.util.Globals.Pages.LANDING_PAGE_COMPANY_VIEW;
import static org.hbrs.se2.project.hellocar.util.Globals.Pages.LANDING_PAGE_STUDENT_VIEW;
import static org.hbrs.se2.project.hellocar.util.Globals.Roles.STUDENT;
import static org.hbrs.se2.project.hellocar.util.Globals.Roles.UNTERNEHMEN;

public class AuthorizationControlTest {

    private AuthorizationControl authControl;
    private UserDTOImpl user;

    @BeforeEach
    void init(){
        authControl = new AuthorizationControl();
        user = UserBuilder
                .getInstance()
                .createNewUser()
                .withRole(STUDENT)
                .build();
    }

    @Test
    void userIsInRoleReturnsTrue(){
        Assertions.assertTrue(authControl.isUserInRole(user, STUDENT));
    }

    @Test
    void userIsNotInRoleReturnsFalse(){
        Assertions.assertFalse(authControl.isUserInRole(user, UNTERNEHMEN));
    }

    @Test
    void isUserAllowedToAccessTest(){
        Assertions.assertTrue(authControl.isUserisAllowedToAccessThisFeature(user, STUDENT, LANDING_PAGE_STUDENT_VIEW));
        Assertions.assertFalse(authControl.isUserisAllowedToAccessThisFeature(user, STUDENT, LANDING_PAGE_COMPANY_VIEW));
    }


}
