package org.hbrs.se2.project.hellocar.control;

import org.hbrs.se2.project.hellocar.dao.UserDAO;
import org.hbrs.se2.project.hellocar.dtos.UserDTO;
import org.hbrs.se2.project.hellocar.services.db.exceptions.DatabaseLayerException;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class RegistrationControl {

    public static final String STUDENT = "student";
    public static final String UNTERNEHMEN = "unternehmen";

    UserDAO userDAO = new UserDAO();

    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-zA-Z]).{8}.*$";

    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    public boolean createUser( UserDTO userDTO ) throws DatabaseLayerException, NoSuchAlgorithmException {

        if ( userDTO.getPassword() == null || userDTO.getPassword().equals("")) {
            throw new DatabaseLayerException("Password missing");
        }
        if ( !isPasswordValid(userDTO.getPassword())) {
            throw new DatabaseLayerException("invalid password");
        }
        if (userDTO.getRole().equals(STUDENT)) {
            if (userDTO.getLastName() == null || userDTO.getLastName().equals("")) {
                throw new DatabaseLayerException("Lastname missing");
            }
        } else if (userDTO.getRole().equals(UNTERNEHMEN)) {
            if (userDTO.getCompanyName() == null || userDTO.getCompanyName().equals("")) {
                throw new DatabaseLayerException("company name missing");
            }
        }

        userDAO.insertUser(userDTO, hashPassword(userDTO.getPassword()));
        return true;
    }

    private static boolean isPasswordValid(final String password) {
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        return bytesToHex(digest.digest(password.getBytes(StandardCharsets.UTF_8)));
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder pwd = new StringBuilder();
        for (int i = 0; i < hash.length; i++) {
            pwd.append(Integer.toString((hash[i] & 0xff) + 0x100, 16).substring(1));
        }
        return pwd.toString();
    }
}
