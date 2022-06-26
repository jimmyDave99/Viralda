package org.hbrs.se2.project.hellocar.dao;

import org.hbrs.se2.project.hellocar.dtos.UserDTO;
import org.hbrs.se2.project.hellocar.dtos.impl.UserDTOImpl;
import org.hbrs.se2.project.hellocar.services.db.JDBCConnection;
import org.hbrs.se2.project.hellocar.services.db.exceptions.DatabaseLayerException;
import org.hbrs.se2.project.hellocar.util.Globals;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import static org.hbrs.se2.project.hellocar.util.Globals.Errors.PROBLEM;
import static org.hbrs.se2.project.hellocar.util.Globals.Roles.STUDENT;
import static org.hbrs.se2.project.hellocar.util.Globals.Roles.UNTERNEHMEN;


public class UserDAO {

    /**
     * Method for finding Users
     *
     * @param email
     * @param password
     * @return
     * @throws DatabaseLayerException
     */
    public UserDTO findUserByUserEmailAndPassword(String email, String password) throws DatabaseLayerException {
        ResultSet set = null;
        UserDTOImpl userDTO = null;

        try {
            //Get User
            PreparedStatement statement = JDBCConnection.getInstance().getPreparedStatement(
                    "SELECT * FROM collathbrs.user " +
                            "WHERE collathbrs.user.email = ? " +
                            "AND collathbrs.user.passwort = ?");
            statement.setString(1, email);
            statement.setString(2, password);
            set = statement.executeQuery();

            //Map User
            if (set.next()) {
                userDTO = new UserDTOImpl();
                userDTO.setUserId(set.getInt(1));
                userDTO.setEmail(set.getString(2));
                userDTO.setPassword(set.getString(3));
                userDTO.setRole(set.getString(4));
                userDTO.setDescription(set.getString(6));

                if (userDTO.getRole().equals(STUDENT)) {
                    //Get Student
                    PreparedStatement studentStatement = JDBCConnection.getInstance().getPreparedStatement(
                            "SELECT * FROM collathbrs.student WHERE user_id = ?");
                    studentStatement.setInt(1, userDTO.getUserId());
                    set = studentStatement.executeQuery();

                    //Map Student
                    if (set.next()) {
                        userDTO.setStudentId(set.getInt(1));
                        userDTO.setFirstName(set.getString(3));
                        userDTO.setLastName(set.getString(4));
                        userDTO.setFaculty(set.getString(5));
                        userDTO.setSemester(set.getInt(6));
                        userDTO.setSpecialization(set.getString(7));
                    }
                } else if (userDTO.getRole().equals(UNTERNEHMEN)) {
                    //Get Unternehmen
                    PreparedStatement unternehmenStatement = JDBCConnection.getInstance().getPreparedStatement(
                            "SELECT * FROM collathbrs.unternehmen WHERE user_id = ?");
                    unternehmenStatement.setInt(1, userDTO.getUserId());
                    set = unternehmenStatement.executeQuery();

                    //Map Unternehmen
                    if (set.next()) {
                        userDTO.setUnternehmenId(set.getInt(1));
                        userDTO.setCompanyName(set.getString(3));
                        userDTO.setBranche(set.getString(4));
                    }
                }
            } else {
                throw new DatabaseLayerException("Kein User mit dieser Email und diesem Passwort gefunden");
            }
        } catch (SQLException ex) {
            DatabaseLayerException e = new DatabaseLayerException("Fehler im SQL-Befehl!");
            e.setReason(Globals.Errors.SQLERROR);
            throw e;
        } catch (NullPointerException ex) {
            DatabaseLayerException e = new DatabaseLayerException("Fehler bei Datenbankverbindung!");
            e.setReason(Globals.Errors.DATABASE);
            throw e;
        } finally {
            JDBCConnection.getInstance().closeConnection();
        }

        return userDTO;
    }

    /**
     * Method to insert Users
     *
     * @param userDTO
     * @param password
     * @throws DatabaseLayerException
     */
    public void insertUser(UserDTO userDTO, String password) throws DatabaseLayerException {


        try {
            PreparedStatement statement = JDBCConnection.getInstance().getPreparedStatement("INSERT " +
                    "INTO collathbrs.user (email, passwort, rolle) VALUES (?,?,?)");

            statement.setString(1, userDTO.getEmail());
            statement.setString(2, password);
            statement.setString(3, userDTO.getRole());

            statement.executeUpdate();

            if (userDTO.getRole().equals(STUDENT)) {

                PreparedStatement studentStatement = JDBCConnection.getInstance().getPreparedStatement("INSERT " +
                        "INTO collathbrs.student (user_id, vorname, nachname) VALUES (?,?,?)");

                studentStatement.setInt(1, getUserIdByEmail(userDTO));
                studentStatement.setString(2, userDTO.getFirstName());
                studentStatement.setString(3, userDTO.getLastName());

                studentStatement.executeUpdate();

            } else if (userDTO.getRole().equals(UNTERNEHMEN)) {
                PreparedStatement unternehmenStatement = JDBCConnection.getInstance().getPreparedStatement("INSERT " +
                        "INTO collathbrs.unternehmen (user_id, company_name, branche) VALUES (?,?,?)");

                unternehmenStatement.setInt(1, getUserIdByEmail(userDTO));
                unternehmenStatement.setString(2, userDTO.getCompanyName());
                unternehmenStatement.setString(3, userDTO.getBranche());

                unternehmenStatement.executeUpdate();
            }

        } catch (SQLException ex) {
            DatabaseLayerException e = new DatabaseLayerException(PROBLEM);
            e.setReason(Globals.Errors.DATABASE);
            throw e;
        }
    }


    /**
     * Method for finding UserId
     *
     * @param userDTO
     * @return int
     * @throws DatabaseLayerException
     */
    public int getUserIdByEmail(UserDTO userDTO) throws DatabaseLayerException {
        try {
            int userId = 0;
            PreparedStatement ps = JDBCConnection.getInstance().getPreparedStatement("SELECT id " +
                    "FROM collathbrs.user WHERE email LIKE ?");
            ps.setString(1, userDTO.getEmail());
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                userId = rs.getInt(1);

            return userId;

        } catch (SQLException ex) {
            DatabaseLayerException e = new DatabaseLayerException(PROBLEM);
            e.setReason(Globals.Errors.DATABASE);
            throw e;
        }
    }

    /**
     * Method for updating Users
     *
     * @param userDTO
     * @return
     * @throws DatabaseLayerException
     */
    public void updateUserByEmail(UserDTO userDTO) throws DatabaseLayerException {
        try {
            int userId = getUserIdByEmail(userDTO);

            //Update User
            PreparedStatement statement = JDBCConnection.getInstance().getPreparedStatement(
                    "UPDATE collathbrs.user " +
                            "SET email = ? " +
                            ", beschreibung = ? " +
                            "WHERE id = ?");
            statement.setString(1, userDTO.getEmail());
            statement.setString(2, userDTO.getDescription());
            statement.setInt(3, userId);
            statement.executeUpdate();

            //Update Student or Unternehmen
            if (userDTO.getRole().equals(STUDENT)) {
                PreparedStatement studentStatement = JDBCConnection.getInstance().getPreparedStatement(
                        "UPDATE collathbrs.student " +
                                "SET vorname = ? " +
                                ", nachname = ? " +
                                ", fachbereich = ? " +
                                ", semester = ? " +
                                ", spezialisierung = ? " +
                                "WHERE user_id = ?");
                studentStatement.setString(1, userDTO.getFirstName());
                studentStatement.setString(2, userDTO.getLastName());
                studentStatement.setString(3, userDTO.getFaculty());
                studentStatement.setInt(4, userDTO.getSemester());
                studentStatement.setString(5, userDTO.getSpecialization());
                studentStatement.setInt(6, userId);
                studentStatement.executeUpdate();
            } else if (userDTO.getRole().equals(UNTERNEHMEN)) {
                PreparedStatement unternehmenStatement = JDBCConnection.getInstance().getPreparedStatement(
                        "UPDATE collathbrs.unternehmen " +
                                "SET company_name = ? " +
                                ", branche = ? " +
                                "WHERE user_id = ?");
                unternehmenStatement.setString(1, userDTO.getCompanyName());
                unternehmenStatement.setString(2, userDTO.getBranche());
                unternehmenStatement.setInt(3, userId);
                unternehmenStatement.executeUpdate();
            }
        } catch (SQLException ex) {
            DatabaseLayerException e = new DatabaseLayerException(PROBLEM);
            e.setReason(Globals.Errors.DATABASE);
            throw e;
        }
    }

    /**
     * Method for updating Users
     *
     * @param email, password
     * @return
     * @throws DatabaseLayerException
     */
    public void updateUserPasswordByEmail(String email, String password) throws DatabaseLayerException {
        try {
            UserDTO userDTO = new UserDTOImpl();
            userDTO.setEmail(email);
            int userId = getUserIdByEmail(userDTO);

            //Update User
            PreparedStatement statement = JDBCConnection.getInstance().getPreparedStatement(
                    "UPDATE collathbrs.user " +
                            "SET passwort = ? " +
                            "WHERE id = ?");
            statement.setString(1, password);
            statement.setInt(2, userId);
            statement.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Method for deleting Users
     *
     * @param email
     * @param role
     * @throws DatabaseLayerException
     */
    public void deleteUserByEmail(String email, String role) throws DatabaseLayerException {
        try {
            UserDTO userDTO = new UserDTOImpl();
            userDTO.setEmail(email);
            int userId = getUserIdByEmail(userDTO);

            //Delete Student or Unternehmen
            if (role.equals(STUDENT)) {
                PreparedStatement studentStatement = JDBCConnection.getInstance().getPreparedStatement(
                        "DELETE FROM collathbrs.student WHERE user_id = ?");
                studentStatement.setInt(1, userId);
                studentStatement.executeUpdate();
            } else if (role.equals(UNTERNEHMEN)) {
                PreparedStatement unternehmenStatement = JDBCConnection.getInstance().getPreparedStatement(
                        "DELETE FROM collathbrs.unternehmen WHERE user_id = ?");
                unternehmenStatement.setInt(1, userId);
                unternehmenStatement.executeUpdate();
            }

            //Delete User
            PreparedStatement statement = JDBCConnection.getInstance().getPreparedStatement(
                    "DELETE FROM collathbrs.user WHERE id = ?");
            statement.setInt(1, userId);
            statement.executeUpdate();
        } catch (SQLException ex) {
            DatabaseLayerException e = new DatabaseLayerException(PROBLEM);
            e.setReason(Globals.Errors.DATABASE);
            throw e;
        }
    }

}
