package org.hbrs.se2.project.hellocar.control;

import org.hbrs.se2.builder.JobBuilder;
import org.hbrs.se2.builder.UserBuilder;
import org.hbrs.se2.project.hellocar.dao.BewerbungDAO;
import org.hbrs.se2.project.hellocar.dtos.impl.StellenanzeigeDTOImpl;
import org.hbrs.se2.project.hellocar.dtos.impl.UserDTOImpl;
import org.hbrs.se2.project.hellocar.services.db.exceptions.DatabaseLayerException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hbrs.se2.project.hellocar.util.Globals.JobStatus.*;
import static org.junit.jupiter.api.Assertions.*;

class JobApplicationControlTest {

    private JobApplicationControl jobApplicationControl = null;

    private BewerbungDAO bewerbungDAO = null;

    private StellenanzeigeDTOImpl stellenanzeige = null;

    private UserDTOImpl student = null;

    private UserDTOImpl company = null;

    @BeforeEach
    public void init(){
        jobApplicationControl = new JobApplicationControl();
        bewerbungDAO = new BewerbungDAO();

        stellenanzeige = JobBuilder
                .getInstance()
                .createNewJob()
                .withStellenID(91)
                .build();

        student = UserBuilder
                .getInstance()
                .createNewUser()
                .withStudentID(234)
                .build();

        company = UserBuilder
                .getInstance()
                .createDefaultUserCompany()
                .build();
    }

    @Test
    void createJobApplicationTest() throws DatabaseLayerException {
        assertTrue(jobApplicationControl.createJobApplication(stellenanzeige, student, BEWORBEN));
    }

    @Test
    void updateJobApplicationStatusTest() throws DatabaseLayerException {
        assertTrue(jobApplicationControl.updateJobApplicationStatus(stellenanzeige.getStellenId(), student.getStudentId(), ZURUCKGEZOGEN));
    }


    @Test
    void readAllApplicantTest() throws DatabaseLayerException {
        assertNotNull(jobApplicationControl.readAllApplicant(company));
    }
}