package org.hbrs.se2.project.hellocar.control;

import org.hbrs.se2.project.hellocar.dao.BewerbungDAO;
import org.hbrs.se2.project.hellocar.dao.StellenAnzeigeDAO;
import org.hbrs.se2.project.hellocar.dtos.BewerbungDTO;
import org.hbrs.se2.project.hellocar.dtos.StellenanzeigeDTO;
import org.hbrs.se2.project.hellocar.dtos.UserDTO;
import org.hbrs.se2.project.hellocar.services.db.exceptions.DatabaseLayerException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JobApplicationControl {

    StellenAnzeigeDAO stellenAnzeigeDAO = new StellenAnzeigeDAO();
    BewerbungDAO bewerbungDAO = new BewerbungDAO();
    public List<StellenanzeigeDTO> readAllJobApplications() throws DatabaseLayerException {
        return stellenAnzeigeDAO.findAllJobs();
    }

    public void createStellenanzeige(StellenanzeigeDTO stellenanzeigeDTO, UserDTO userDTO) throws DatabaseLayerException {
        stellenAnzeigeDAO.insertStellenanzeige(stellenanzeigeDTO, userDTO);
    }

    public List<BewerbungDTO> readAllJobApplications2() throws DatabaseLayerException {
        return bewerbungDAO.findAllJobApplication();
    }

}