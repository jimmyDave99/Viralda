package org.hbrs.se2.project.hellocar.control;

import org.hbrs.se2.project.hellocar.dao.UserDAO;
import org.hbrs.se2.project.hellocar.dtos.UserDTO;
import org.hbrs.se2.project.hellocar.services.db.exceptions.DatabaseLayerException;
import org.hbrs.se2.project.hellocar.services.db.exceptions.ViewException;
import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import static org.hbrs.se2.project.hellocar.control.RegistrationControl.hashPassword;

@Component
public class ManageExistingUserControl {
    UserDAO userDAO = new UserDAO();

    public boolean updateUser(UserDTO userDTO) throws DatabaseLayerException {
        if(userDTO == null){
            throw new RuntimeException("DTO ist null!");
        }
        userDAO.updateUserByEmail(userDTO);
        return true;
    }

    public boolean updateUserPassword(UserDTO userDTO, String oldPassword, String email) throws DatabaseLayerException,
            NoSuchAlgorithmException, InvalidKeySpecException, ViewException {

        if(userDTO == null){
            throw new ViewException("DTO ist null!");
        } else if(oldPassword.equals(userDTO.getPassword()))
            throw new DatabaseLayerException("Neues Passwort entspricht dem alten Passwort!");
        else if(!userDTO.getPassword().equals(userDTO.getConfirmPassword()))
            throw new DatabaseLayerException("Neues Passwort und neues Passswort bestätigen stimmen nicht überein!");
        else if(userDTO.getPassword().equals("") || userDTO.getPassword() == null)
            throw new DatabaseLayerException("Passwort konnte nicht übertragen werden.");
        else {
            userDAO.updateUserPasswordByEmail(email, RegistrationControl.hashPassword(userDTO.getPassword()));
            return true;
        }
    }

    public boolean updateProfilePicture(UserDTO userDTO){
        return false;
    }

    public boolean deleteUser(UserDTO userDTO) throws DatabaseLayerException {
        userDAO.deleteUserByEmail(userDTO.getEmail(), userDTO.getRole());
        return true;
    }
}
