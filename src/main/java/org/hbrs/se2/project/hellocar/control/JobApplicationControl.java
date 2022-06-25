package org.hbrs.se2.project.hellocar.control;

import org.hbrs.se2.project.hellocar.dao.BewerbungDAO;
import org.hbrs.se2.project.hellocar.dao.StellenAnzeigeDAO;
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

    public void createJobApplication(StellenanzeigeDTO stellenanzeigeDTO, UserDTO userDTO, String status) throws DatabaseLayerException {
        stellenAnzeigeDAO.insertOrUpdateJobApplication(stellenanzeigeDTO, userDTO, status);
    }

    public List<StellenanzeigeDTO> findJob(int jobId) throws DatabaseLayerException {
        return stellenAnzeigeDAO.findJobWithId(jobId);
    }

    public void updateJobApplicationStatus(int stellenId, int studentId, String status) throws DatabaseLayerException {
         stellenAnzeigeDAO.updateStatusByJobId(stellenId, studentId, status);
    }

    public String getJobStatus(StellenanzeigeDTO stellenanzeigeDTO , UserDTO userDTO) throws DatabaseLayerException {
        return stellenAnzeigeDAO.findStatusByJobIdAndStudentId(stellenanzeigeDTO, userDTO);
    }
    public List<UserDTO> readAllApplicant(UserDTO userDTO) throws DatabaseLayerException {
        return bewerbungDAO.findAllJobApplicant(userDTO);
    }

    public List<StellenanzeigeDTO> readCurrentCompanyJob(UserDTO userDTO) throws DatabaseLayerException {
        return stellenAnzeigeDAO.findCurrentCompanyJob(userDTO);
    }

    public void updateAnnouncement(StellenanzeigeDTO stellenanzeigeDTO) throws DatabaseLayerException {
        stellenAnzeigeDAO.updateCompanyJob(stellenanzeigeDTO);
    }

    public void updateJobStatus(StellenanzeigeDTO stellenanzeigeDTO, String status) throws DatabaseLayerException {
        stellenAnzeigeDAO.updateJobStatus(stellenanzeigeDTO, status);
    }

    public void deleteAnnouncement(StellenanzeigeDTO stellenanzeigeDTO) throws DatabaseLayerException {
        stellenAnzeigeDAO.deleteJob(stellenanzeigeDTO);
    }
}