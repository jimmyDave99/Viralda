package org.hbrs.se2.project.hellocar.control;

import org.hbrs.se2.project.hellocar.dao.UserDAO;
import org.hbrs.se2.project.hellocar.dtos.UserDTO;
import org.hbrs.se2.project.hellocar.services.db.exceptions.DatabaseLayerException;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hbrs.se2.project.hellocar.util.Globals.Roles.STUDENT;
import static org.hbrs.se2.project.hellocar.util.Globals.Roles.UNTERNEHMEN;

@Component
public class RegistrationControl {

    UserDAO userDAO = new UserDAO();

    private static final String PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8}.*$";

    private static final Pattern pattern = Pattern.compile(PATTERN);

    public boolean createUser( UserDTO userDTO ) throws DatabaseLayerException, NoSuchAlgorithmException, InvalidKeySpecException {

        if ( userDTO.getPassword() == null || userDTO.getPassword().equals("")) {
            throw new DatabaseLayerException("Password missing");
        }
        if(!(userDTO.getPassword().equals(userDTO.getConfirmPassword()))
                || userDTO.getConfirmPassword() == null
                || userDTO.getConfirmPassword().equals("")) {
            throw new DatabaseLayerException("password are different");
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

    public boolean isPasswordValid(final String password) {
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    protected static String hashPassword(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        int iterations = 1000;
        char[] chars = password.toCharArray();
        byte[] salt = new byte[16];

        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

        byte[] hash = skf.generateSecret(spec).getEncoded();
        return iterations + "" + bytesToHex(salt) + "" + bytesToHex(hash);
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder pwd = new StringBuilder();
        for (byte b : hash) {
            pwd.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }
        return pwd.toString();
    }
}
