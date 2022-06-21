package org.hbrs.se2.project.hellocar.control;

import org.hbrs.se2.project.hellocar.dao.UserDAO;
import org.hbrs.se2.project.hellocar.dtos.UserDTO;
import org.hbrs.se2.project.hellocar.services.db.exceptions.DatabaseLayerException;
import org.springframework.stereotype.Component;

@Component
public class ManageExistingUserControl {
    UserDAO userDAO = new UserDAO();

    public boolean updateUser(UserDTO userDTO) throws DatabaseLayerException {
        //if-Abfragen, was muss geupdated werden

        userDAO.updateUserByEmail(userDTO);
        return true;
    }

    public boolean deleteUser(UserDTO userDTO) throws DatabaseLayerException {
        userDAO.deleteUserByEmail(userDTO.getEmail(), userDTO.getRole());
        return true;
    }
}
