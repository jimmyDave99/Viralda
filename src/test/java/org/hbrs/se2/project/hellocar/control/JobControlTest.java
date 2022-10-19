package org.hbrs.se2.project.hellocar.control;

import org.hbrs.se2.builder.JobBuilder;
import org.hbrs.se2.builder.UserBuilder;
import org.hbrs.se2.project.hellocar.dao.StellenAnzeigeDAO;
import org.hbrs.se2.project.hellocar.dtos.StellenanzeigeDTO;
import org.hbrs.se2.project.hellocar.dtos.impl.StellenanzeigeDTOImpl;
import org.hbrs.se2.project.hellocar.dtos.impl.UserDTOImpl;
import org.hbrs.se2.project.hellocar.services.db.exceptions.DatabaseLayerException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.hbrs.se2.project.hellocar.util.Globals.JobStatus.*;
import static org.junit.jupiter.api.Assertions.*;

class JobControlTest {

    private JobControl jobControl = null;

    private StellenAnzeigeDAO stellenAnzeigeDAO = null;

    private StellenanzeigeDTOImpl stellenanzeige = null;

    private StellenanzeigeDTOImpl stellenanzeigeUpdated = null;

    private UserDTOImpl company = null;

    private final String TITLE = "Werkstudent (m/w/d)";

    private final String BRANCHE = "IT";

    private final String DESCRIPTION = "Werde Werkstudent (m/w/d) in Bonn " +
            "Du hast erste Erfahrungen im IT-Bereich und Interesse an HR-Prozessen?" +
            " Werde Werkstudent (m/w/d) in der Informatik in Bonn.Was du machen wirst:" +
            "Als Werkstudent (m/w/d) in der Informatik in Bonn erwarten dich die folgenden" +
            " spannenden...";

    private final int SALARY = 11;

    private final int WEEKLY_HOURS = 18;

    private final LocalDate START_DATE = LocalDate.now();

    private final String NEW_TITLE = "Werkstudent (m/w/d) Informatik";

    private final String NEW_BRANCHE = "IT";

    private final String NEW_DESCRIPTION = "Werde Werkstudent (m/w/d) in der Informatik in Bonn " +
            "Du hast erste Erfahrungen im IT-Bereich und Interesse an HR-Prozessen?" +
            " Werde Werkstudent (m/w/d) in der Informatik in Bonn.Was du machen wirst:" +
            "Als Werkstudent (m/w/d) in der Informatik in Bonn erwarten dich die folgenden" +
            " spannenden...";

    private final int NEW_SALARY = 12;

    private final int NEW_WEEKLY_HOURS = 20;

    private final LocalDate NEW_START_DATE = LocalDate.now();



    @BeforeEach
    public void init() {
        jobControl = new JobControl();
        stellenAnzeigeDAO = new StellenAnzeigeDAO();

         stellenanzeige = JobBuilder
                .getInstance()
                .createNewJob()
                .withTitle(TITLE)
                .withBranche(BRANCHE)
                .withDescription(DESCRIPTION)
                .withSalary(SALARY)
                .withStartDate(START_DATE)
                .withWeeklyHours(WEEKLY_HOURS)
                 .withStatus(INAKTIV)
                 .withStellenID(91)
                .build();

        stellenanzeigeUpdated = new JobBuilder()
                .withTitle(NEW_TITLE)
                .withBranche(NEW_BRANCHE)
                .withDescription(NEW_DESCRIPTION)
                .withSalary(NEW_SALARY)
                .withStartDate(NEW_START_DATE)
                .withWeeklyHours(NEW_WEEKLY_HOURS)
                .withStellenID(stellenanzeige.getStellenId())
                .build();

         company = UserBuilder
                .getInstance()
                .createDefaultUserCompany()
                .build();
    }

    @Test
    void findJobTest() throws DatabaseLayerException {
        List<StellenanzeigeDTO> currentJob = jobControl.findJob(stellenanzeige.getStellenId());
        assertEquals(1, currentJob.size());
    }


    @Test
    void updateAnnouncementTest() throws DatabaseLayerException {
        assertTrue(jobControl.updateAnnouncement(stellenanzeigeUpdated));

        List<StellenanzeigeDTO> currentUpdatedJob = jobControl.findJob(stellenanzeige.getStellenId());
        assertEquals(1, currentUpdatedJob.size());

        assertEquals(stellenanzeigeUpdated.getTitel(), currentUpdatedJob.get(0).getTitel());
        assertEquals(stellenanzeigeUpdated.getStellenId(), currentUpdatedJob.get(0).getStellenId());
        assertEquals(stellenanzeigeUpdated.getBereich(), currentUpdatedJob.get(0).getBereich());
        assertEquals(stellenanzeigeUpdated.getBeschreibung(), currentUpdatedJob.get(0).getBeschreibung());
        assertEquals(stellenanzeigeUpdated.getGehalt(), currentUpdatedJob.get(0).getGehalt());
        assertEquals(stellenanzeigeUpdated.getWochenstunden(), currentUpdatedJob.get(0).getWochenstunden());
        assertEquals(stellenanzeigeUpdated.getEinstellungsdatum(), currentUpdatedJob.get(0).getEinstellungsdatum());
    }

    @Test
    void updateJobStatusTest() throws DatabaseLayerException {
        assertTrue(jobControl.updateJobStatus(stellenanzeige, AKTIV));

        List<StellenanzeigeDTO> currentUpdatedStatusJob = jobControl.findJob(stellenanzeige.getStellenId());

        assertEquals(1, currentUpdatedStatusJob.size());
        assertEquals(AKTIV, currentUpdatedStatusJob.get(0).getStatus());
    }
}