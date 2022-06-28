package org.hbrs.se2.project.hellocar.test;

import org.hbrs.se2.builder.JobBuilder;
import org.hbrs.se2.project.hellocar.dao.StellenAnzeigeDAO;
import org.hbrs.se2.project.hellocar.dtos.StellenanzeigeDTO;
import org.hbrs.se2.project.hellocar.dtos.UserDTO;
import org.hbrs.se2.project.hellocar.dtos.impl.StellenanzeigeDTOImpl;
import org.hbrs.se2.project.hellocar.dtos.impl.UserDTOImpl;
import org.hbrs.se2.project.hellocar.services.db.exceptions.DatabaseLayerException;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

public class StellenAnzeigeDAOTest {

    private StellenAnzeigeDAO stellenAnzeigeDAO;

    @BeforeEach
    public void init() {
        stellenAnzeigeDAO = new StellenAnzeigeDAO();
    }

    @Test
    public void findAllJobsTest() throws DatabaseLayerException {
        List<StellenanzeigeDTO> jobs = stellenAnzeigeDAO.findAllJobs();

        // list is not null
        assertNotNull(jobs);

        // list is not empty
        assertNotEquals(jobs.size(), 0);

        for (StellenanzeigeDTO job : jobs) {
            // entry is not null
            assertNotNull(job);
            // entry is not empty
            assertNotEquals(job.toString(), "");
        }
    }

    @Test
    public void roundTripCRUD() throws DatabaseLayerException {
        //create instances
        StellenanzeigeDTO firstStellenanzeige = JobBuilder
                .getInstance()
                .createNewJob()
                .withTitle("Delete_title")
                .withBranche("IT")
                .withDescription("Delete_description")
                .withStartDate(LocalDate.of(2020, Month.JANUARY, 8))
                .withSalary(20.0).withWeeklyHours(20.0)
                .withStatus("AKTIV")
                .build();

        UserDTO unternehmen = new UserDTOImpl();
        unternehmen.setUnternehmenId(82);

        //create
        stellenAnzeigeDAO.insertStellenanzeige(firstStellenanzeige, unternehmen);

        List<StellenanzeigeDTO> stellenanzeigen = stellenAnzeigeDAO.findJobWithUnternehmenId(unternehmen.getUnternehmenId());
        StellenanzeigeDTO newStellenanzeige = findStellenanzeige(stellenanzeigen, firstStellenanzeige.getTitel());
        assertNotNull(newStellenanzeige);

        //update
        newStellenanzeige.setBereich("Maschinenbau");
        stellenAnzeigeDAO.updateCompanyJob(newStellenanzeige);

        stellenanzeigen = stellenAnzeigeDAO.findJobWithUnternehmenId(unternehmen.getUnternehmenId());
        StellenanzeigeDTO stellenanzeige = findStellenanzeige(stellenanzeigen, firstStellenanzeige.getTitel());
        assertNotNull(stellenanzeige);
        assertEquals(stellenanzeige.getBereich(), newStellenanzeige.getBereich());

        //delete
        stellenAnzeigeDAO.deleteJob(stellenanzeige);
        stellenanzeigen = stellenAnzeigeDAO.findJobWithUnternehmenId(unternehmen.getUnternehmenId());
        stellenanzeige = findStellenanzeige(stellenanzeigen, firstStellenanzeige.getTitel());
        assertNull(stellenanzeige);
    }

    private StellenanzeigeDTO findStellenanzeige(List<StellenanzeigeDTO> stellenanzeigen, String title) {
        StellenanzeigeDTO stellenanzeige = null;
        for (StellenanzeigeDTO s: stellenanzeigen) {
            if (s.getTitel().equals(title)) {
                stellenanzeige = s;
            }
        }
        return stellenanzeige;
    }

}
