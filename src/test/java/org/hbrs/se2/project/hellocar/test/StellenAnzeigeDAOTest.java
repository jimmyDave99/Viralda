package org.hbrs.se2.project.hellocar.test;

import org.hbrs.se2.project.hellocar.dao.StellenAnzeigeDAO;
import org.hbrs.se2.project.hellocar.dtos.StellenanzeigeDTO;
import org.hbrs.se2.project.hellocar.services.db.exceptions.DatabaseLayerException;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

}
