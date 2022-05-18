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
import java.util.List;

public class UserDAO {

    /**
     * Method for finding Users
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
            PreparedStatement statement = JDBCConnection.getInstance().getPreparedStatement("INSERT" +
                    "INTO collathbrs.user VALUES (?,?,?,?,?)");

            statement.setString(1, String.valueOf(userDTO.getUserId()));
            statement.setString(2, userDTO.getEmail());
            statement.setString(3, password);
            statement.setString(4, userDTO.getRole());
            statement.setNull(5, java.sql.Types.NULL);

            statement.executeUpdate();

            if (userDTO.getRole().equals("student")) {
                PreparedStatement studentStatement = JDBCConnection.getInstance().getPreparedStatement("INSERT" +
                        "INTO collathbrs.student VALUES (?,?,?,?)");

                studentStatement.setString(1, String.valueOf(userDTO.getStudentId()));
                studentStatement.setString(2, String.valueOf(userDTO.getUserId()));
                studentStatement.setString(3, userDTO.getFirstName());
                studentStatement.setString(4, userDTO.getLastName());

                studentStatement.executeUpdate();

            } else if (userDTO.getRole().equals("unternehmen")) {
                PreparedStatement unternehmenStatement = JDBCConnection.getInstance().getPreparedStatement("INSERT" +
                        "INTO collathbrs.unternehmen VALUES (?,?,?,?,?)");

                unternehmenStatement.setString(1, String.valueOf(userDTO.getUnternehmenId()));
                unternehmenStatement.setString(2, String.valueOf(userDTO.getUserId()));
                unternehmenStatement.setString(3, userDTO.getUnternehmenName());
                unternehmenStatement.setString(4, userDTO.getBranche());
                unternehmenStatement.setString(5, userDTO.getDescription());

                unternehmenStatement.executeUpdate();
            }

        } catch (SQLException ex) {
            DatabaseLayerException e = new DatabaseLayerException("Probleme mit der Datenbank");
            e.setReason(Globals.Errors.DATABASE);
            throw e;

        }
    }

}
