package org.hbrs.se2.project.hellocar.test;

import org.hbrs.se2.builder.JobBuilder;
import org.hbrs.se2.project.hellocar.dao.BewerbungDAO;
import org.hbrs.se2.project.hellocar.dao.StellenAnzeigeDAO;
import org.hbrs.se2.project.hellocar.dtos.BewerbungDTO;
import org.hbrs.se2.project.hellocar.dtos.StellenanzeigeDTO;
import org.hbrs.se2.project.hellocar.dtos.UserDTO;
import org.hbrs.se2.project.hellocar.dtos.impl.BewerbungDTOImpl;
import org.hbrs.se2.project.hellocar.dtos.impl.StellenanzeigeDTOImpl;
import org.hbrs.se2.project.hellocar.dtos.impl.UserDTOImpl;
import org.hbrs.se2.project.hellocar.services.db.exceptions.DatabaseLayerException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BewerbungDAOTest {

    private BewerbungDAO bewerbungDAO;

    @BeforeEach
    public void init() {
        bewerbungDAO = new BewerbungDAO();
    }

    @Test
    public void roundTripCRUD() throws DatabaseLayerException {
        //create instances
        UserDTO firstUser = new UserDTOImpl();
        firstUser.setStudentId(328);
        StellenanzeigeDTO firstStellenanzeige = new StellenanzeigeDTOImpl();
        firstStellenanzeige.setStellenId(91);
        String status = "Delete_BEWORBEN";

        //create
        bewerbungDAO.insertOrUpdateJobApplication(firstStellenanzeige, firstUser, status);
        String newStatus = bewerbungDAO.findStatusByJobIdAndStudentId(firstStellenanzeige, firstUser);
        assertEquals(status, newStatus);

        //update
        status = "Delete_ABGELEHNT";
        bewerbungDAO.updateStatusByJobId(firstStellenanzeige.getStellenId(), firstUser.getStudentId(), status);
        newStatus = bewerbungDAO.findStatusByJobIdAndStudentId(firstStellenanzeige, firstUser);
        assertEquals(status, newStatus);

        //delete
        bewerbungDAO.deleteJobApplication(firstStellenanzeige, firstUser);
        newStatus = bewerbungDAO.findStatusByJobIdAndStudentId(firstStellenanzeige, firstUser);
        assertEquals(newStatus, "");
    }

}
