package org.hbrs.se2.project.hellocar.control;

import org.hbrs.se2.project.hellocar.dao.UserDAO;
import org.hbrs.se2.project.hellocar.dtos.UserDTO;
import org.hbrs.se2.project.hellocar.services.db.exceptions.DatabaseLayerException;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class RegistrationControl {

    UserDAO userDAO = new UserDAO();

    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-zA-Z]).{8}.*$";

    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    public void createUser( UserDTO userDTO ) throws DatabaseLayerException, NoSuchAlgorithmException {

        if ( userDTO.getPassword() == null || userDTO.getPassword().equals("")) {
            throw new DatabaseLayerException("Password missing");
        }
        if ( !isPasswordValid(userDTO.getPassword())) {
            throw new DatabaseLayerException("Passwort muss mind. 8 Zeichen lang sein. Es muss aus mind. einem Buchstaben und einer Zahl bestehen");
        }
        if ( userDTO.getLastName() == null || userDTO.getLastName().equals("")) {
            throw new DatabaseLayerException("Lastname missing");
        }
        /*if (isPasswordAndConfirmPasswordNotEquals(userDTO)) {
            throw new DatabaseLayerException("Password and confirmation password don't match");
        }*/

        userDAO.insertUser(userDTO, hashPassword(userDTO.getPassword()));
    }

    private static boolean isPasswordValid(final String password) {
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    // todo check if the Password and Confirm Password are same
    private boolean isPasswordAndConfirmPasswordNotEquals(UserDTO userDTO){
        return false;
    }

    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

        return Arrays.toString(hash);
    }

    private boolean check(UserDTO userDTO){
        return true;
    }

}
