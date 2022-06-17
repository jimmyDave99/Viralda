package org.hbrs.se2.project.hellocar.control;

import org.hbrs.se2.project.hellocar.dao.StellenAnzeigeDAO;
import org.hbrs.se2.project.hellocar.dtos.StellenanzeigeDTO;
import org.hbrs.se2.project.hellocar.services.db.exceptions.DatabaseLayerException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JobApplicationControl {

    StellenAnzeigeDAO stellenAnzeigeDAO = new StellenAnzeigeDAO();

    public List<StellenanzeigeDTO> readAllJobApplications() throws DatabaseLayerException {
        return stellenAnzeigeDAO.findAllJobs();
    }

}