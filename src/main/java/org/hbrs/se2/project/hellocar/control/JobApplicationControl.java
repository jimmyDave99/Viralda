package org.hbrs.se2.project.hellocar.control;

import org.hbrs.se2.project.hellocar.dao.BewerbungDAO;
import org.hbrs.se2.project.hellocar.dtos.StellenanzeigeDTO;
import org.hbrs.se2.project.hellocar.dtos.UserDTO;
import org.hbrs.se2.project.hellocar.services.db.exceptions.DatabaseLayerException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JobApplicationControl {

    BewerbungDAO bewerbungDAO = new BewerbungDAO();

    public boolean createJobApplication(StellenanzeigeDTO stellenanzeigeDTO, UserDTO userDTO, String status) throws DatabaseLayerException {
        bewerbungDAO.insertOrUpdateJobApplication(stellenanzeigeDTO, userDTO, status);
        return true;
    }

    public boolean updateJobApplicationStatus(int stellenId, int studentId, String status) throws DatabaseLayerException {
         bewerbungDAO.updateStatusByJobId(stellenId, studentId, status);
         return true;
    }

    public String getJobStatus(StellenanzeigeDTO stellenanzeigeDTO , UserDTO userDTO) throws DatabaseLayerException {
        return bewerbungDAO.findStatusByJobIdAndStudentId(stellenanzeigeDTO, userDTO);
    }
    public List<UserDTO> readAllApplicant(UserDTO userDTO) throws DatabaseLayerException {
        return bewerbungDAO.findAllJobApplicant(userDTO);
    }

}