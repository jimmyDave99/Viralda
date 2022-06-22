package org.hbrs.se2.project.hellocar.control;

import org.hbrs.se2.project.hellocar.dao.UserDAO;
import org.hbrs.se2.project.hellocar.dtos.UserDTO;
import org.hbrs.se2.project.hellocar.services.db.exceptions.DatabaseLayerException;
import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;

import static org.hbrs.se2.project.hellocar.control.RegistrationControl.hashPassword;

@Component
public class ManageExistingUserControl {
    UserDAO userDAO = new UserDAO();

    public boolean updateUser(UserDTO userDTO, UserDTO userDTOSession) throws DatabaseLayerException {
        //if-Abfragen, was muss geupdated werden

        userDAO.updateUserByEmail(userDTO);
        return true;
    }

    public boolean updateUserPassword(UserDTO userDTO, UserDTO userDTOSession) throws DatabaseLayerException,
                NoSuchAlgorithmException {

        userDAO.updateUserPasswordByEmail(userDTOSession.getEmail(), hashPassword(userDTO.getPassword()));
        return true;
    }

    public boolean deleteUser(UserDTO userDTO) throws DatabaseLayerException {
        userDAO.deleteUserByEmail(userDTO.getEmail(), userDTO.getRole());
        return true;
    }
}
