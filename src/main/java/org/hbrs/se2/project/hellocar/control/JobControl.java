package org.hbrs.se2.project.hellocar.control;

import org.hbrs.se2.project.hellocar.dao.StellenAnzeigeDAO;
import org.hbrs.se2.project.hellocar.dtos.StellenanzeigeDTO;
import org.hbrs.se2.project.hellocar.dtos.UserDTO;
import org.hbrs.se2.project.hellocar.services.db.exceptions.DatabaseLayerException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JobControl {

    StellenAnzeigeDAO stellenAnzeigeDAO = new StellenAnzeigeDAO();

    public List<StellenanzeigeDTO> readAllJobApplications() throws DatabaseLayerException {
        return stellenAnzeigeDAO.findAllJobs();
    }

    public boolean createStellenanzeige(StellenanzeigeDTO stellenanzeigeDTO, UserDTO userDTO) throws DatabaseLayerException {
        stellenAnzeigeDAO.insertStellenanzeige(stellenanzeigeDTO, userDTO);
        return true;
    }

    public List<StellenanzeigeDTO> readCurrentCompanyJob(UserDTO userDTO) throws DatabaseLayerException {
        return stellenAnzeigeDAO.findCurrentCompanyJob(userDTO);
    }

    public boolean updateAnnouncement(StellenanzeigeDTO stellenanzeigeDTO) throws DatabaseLayerException {
        stellenAnzeigeDAO.updateCompanyJob(stellenanzeigeDTO);
        return true;
    }

    public boolean updateJobStatus(StellenanzeigeDTO stellenanzeigeDTO, String status) throws DatabaseLayerException {
        stellenAnzeigeDAO.updateJobStatus(stellenanzeigeDTO, status);
        return true;
    }

    public boolean deleteAnnouncement(StellenanzeigeDTO stellenanzeigeDTO) throws DatabaseLayerException {
        stellenAnzeigeDAO.deleteJob(stellenanzeigeDTO);
        return true;
    }

    public List<StellenanzeigeDTO> findJob(int jobId) throws DatabaseLayerException {
        return stellenAnzeigeDAO.findJobWithId(jobId);
    }
}