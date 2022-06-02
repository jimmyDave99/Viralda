package org.hbrs.se2.project.hellocar.dao;

import org.hbrs.se2.project.hellocar.dtos.UserDTO;
import org.hbrs.se2.project.hellocar.dtos.impl.UserDTOImpl;
import org.hbrs.se2.project.hellocar.services.db.JDBCConnection;
import org.hbrs.se2.project.hellocar.services.db.exceptions.DatabaseLayerException;
import org.hbrs.se2.project.hellocar.util.Globals;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.hbrs.se2.project.hellocar.util.Globals.Roles.STUDENT;
import static org.hbrs.se2.project.hellocar.util.Globals.Roles.UNTERNEHMEN;


// TODO: CRUD komplett umsetzten, vorallem aber Delete
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
        // Set ResultSet to null;
        ResultSet set = null;

        // Set try-clause
        try {
            Statement statement = null;
            try {
                statement = JDBCConnection.getInstance().getStatement();
            } catch (DatabaseLayerException e) {
                e.printStackTrace();
            }

            set = statement.executeQuery(
                    "SELECT * "
                            + "FROM collathbrs.user "
                            + "WHERE collathbrs.user.email = \'" + email + "\'"
                            + "AND collathbrs.user.passwort = \'" + password + "\'");

            // JDBCConnection.getInstance().closeConnection();

        } catch (SQLException ex) {
            DatabaseLayerException e = new DatabaseLayerException("Fehler im SQL-Befehl!");
            e.setReason(Globals.Errors.SQLERROR);
            throw e;
        }
        catch (NullPointerException ex) {
            DatabaseLayerException e = new DatabaseLayerException("Fehler bei Datenbankverbindung!");
            e.setReason(Globals.Errors.DATABASE);
            throw e;
        }

        UserDTOImpl user = null;

        try {
            if (set.next()) {
                // Durchführung des Object-Relational-Mapping (ORM)

                user = new UserDTOImpl();
                user.setUserId( set.getInt(1));
                user.setEmail(set.getString(2));

                return user;

            } else {
                // Error Handling
                DatabaseLayerException e = new DatabaseLayerException("No User Could be found");
                e.setReason(Globals.Errors.NOUSERFOUND);
                throw e;
            }
        } catch (SQLException ex) {
            DatabaseLayerException e = new DatabaseLayerException("Probleme mit der Datenbank");
            e.setReason(Globals.Errors.DATABASE);
            throw e;

        } finally {
            JDBCConnection.getInstance().closeConnection();
        }

    }


    public void insertUser(UserDTO userDTO, String password) throws DatabaseLayerException {

        // Exception für UserDTO leer fehlt noch oder kommt das in RegistrationControl?

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
            DatabaseLayerException e = new DatabaseLayerException("Probleme mit der Datenbank");
            e.setReason(Globals.Errors.DATABASE);
            throw e;
        }
    }

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
            DatabaseLayerException e = new DatabaseLayerException("Probleme mit der Datenbank");
            e.setReason(Globals.Errors.DATABASE);
            throw e;
        }
    }

    public void updateUserByEmail(UserDTO userDTO) throws DatabaseLayerException {
        try {
            int userId = getUserIdByEmail(userDTO);

            //Update User
            Statement statement = JDBCConnection.getInstance().getStatement();
            statement.executeUpdate("UPDATE collathbrs.user "
                    + "SET email = \'" + userDTO.getEmail() + "\'"
                    + ", passwort = \'" + userDTO.getPassword() + "\' "
                    + "WHERE id = " + userId);

            //Update Student or Unternehmen
            if (userDTO.getRole().equals(STUDENT)) {
                Statement studentStatement = JDBCConnection.getInstance().getStatement();
                studentStatement.executeUpdate(
                        "UPDATE collathbrs.student "
                                + "SET vorname = \'" + userDTO.getFirstName() + "\'"
                                + ", nachname = \'" + userDTO.getLastName() + "\' "
                                + "WHERE user_id = " + userId);
            } else if (userDTO.getRole().equals(UNTERNEHMEN)) {
                Statement unternehemnStatement = JDBCConnection.getInstance().getStatement();
                unternehemnStatement.executeUpdate(
                        "UPDATE collathbrs.unternehmen "
                                + "SET company_name = \'" + userDTO.getCompanyName() + "\'"
                                + ", branche = \'" + userDTO.getBranche() + "\' "
                                + "WHERE user_id = " + userId);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void deleteUserByEmail(String email, String role) throws DatabaseLayerException {
        try {
            UserDTO userDTO = new UserDTOImpl();
            userDTO.setEmail(email);
            int userId = getUserIdByEmail(userDTO);

            //Delete Student or Unternehmen
            Statement statement = JDBCConnection.getInstance().getStatement();
            if (role.equals(STUDENT)) {
                statement.execute(
                        "DELETE "
                                + "FROM collathbrs.student "
                                + "WHERE user_id = " + userId);
            } else if (role.equals(UNTERNEHMEN)) {
                statement.execute(
                        "DELETE "
                                + "FROM collathbrs.unternehmen "
                                + "WHERE user_id = " + userId);
            }

            //Delete User
            Statement userStatement = JDBCConnection.getInstance().getStatement();
            userStatement.execute(
                    "DELETE "
                            + "FROM collathbrs.user "
                            + "WHERE id = " + userId);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
