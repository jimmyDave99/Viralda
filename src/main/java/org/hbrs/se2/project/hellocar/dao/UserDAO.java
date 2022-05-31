package org.hbrs.se2.project.hellocar.dao;

import org.hbrs.se2.project.hellocar.dtos.UserDTO;
import org.hbrs.se2.project.hellocar.dtos.impl.UserDTOImpl;
import org.hbrs.se2.project.hellocar.services.db.JDBCConnection;
import org.hbrs.se2.project.hellocar.services.db.exceptions.DatabaseLayerException;
import org.hbrs.se2.project.hellocar.util.Globals;
import org.springframework.jdbc.core.metadata.HsqlTableMetaDataProvider;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;


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
        } catch (NullPointerException ex) {
            DatabaseLayerException e = new DatabaseLayerException("Fehler bei Datenbankverbindung!");
            e.setReason(Globals.Errors.DATABASE);
            throw e;
        }

        UserDTOImpl user = null;

        try {
            if (set.next()) {
                // Durchführung des Object-Relational-Mapping (ORM)

                user = new UserDTOImpl();
                user.setUserId(set.getInt(1));
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
                    "INTO collathbrs.user VALUES (?,?,?,?,?,?)");

            /* todo generate keys automatic (UserID)
             see https://www.ibm.com/docs/en/db2/11.5?topic=applications-retrieving-auto-generated-keys-insert-statement
             */
            statement.setInt(1, userDTO.getUserId());
            statement.setString(2, userDTO.getEmail());
            statement.setString(3, password);
            statement.setString(4, userDTO.getRole());
            statement.setNull(5, java.sql.Types.NULL);
            statement.setNull(6, java.sql.Types.NULL);

            statement.executeUpdate();

            if (userDTO.getRole().equals("Student")) {
                PreparedStatement studentStatement = JDBCConnection.getInstance().getPreparedStatement("INSERT " +
                        "INTO collathbrs.student VALUES (?,?,?,?)");

                // todo generate keys automatic (UserID, StudentID)
                studentStatement.setInt(1, userDTO.getUserId());
                studentStatement.setInt(2, userDTO.getStudentId());
                studentStatement.setString(3, userDTO.getFirstName());
                studentStatement.setString(4, userDTO.getLastName());

                studentStatement.executeUpdate();

            } else if (userDTO.getRole().equals("Unternehmen")) {
                PreparedStatement unternehmenStatement = JDBCConnection.getInstance().getPreparedStatement("INSERT " +
                        "INTO collathbrs.unternehmen VALUES (?,?,?,?,?)");

                // todo generate keys automatic (UserID, UnternehmerID)
                unternehmenStatement.setInt(1, 9995);
                unternehmenStatement.setInt(2, 9995);
                unternehmenStatement.setString(3, userDTO.getCompanyName());
                unternehmenStatement.setString(4, userDTO.getBranche());
                unternehmenStatement.setString(5, userDTO.getDescription());

                unternehmenStatement.executeUpdate();
            }

        } catch (SQLException ex) {
            DatabaseLayerException e = new DatabaseLayerException("Probleme mit der Datenbank");
            e.setReason(ex.getMessage());
            throw e;

        }
    }

    public void updateUser(UserDTO userDTO) throws DatabaseLayerException {
        try {
            //Update User
            Statement statement = JDBCConnection.getInstance().getStatement();
            statement.executeUpdate("UPDATE collathbrs.user "
                    + "SET email = \'" + userDTO.getEmail() + "\'"
                    + ", passwort = \'" + userDTO.getPassword() + "\' "
                    + "WHERE \"userID\" = " + userDTO.getUserId());

            //Update Student or Unternehmen
            if (userDTO.getRole().equals("Student")) {
                Statement studentStatement = JDBCConnection.getInstance().getStatement();
                studentStatement.executeUpdate(
                        "UPDATE collathbrs.student "
                                + "SET vorname = \'" + userDTO.getFirstName() + "\'"
                                + ", nachname = \'" + userDTO.getLastName() + "\' "
                                + "WHERE \"userID\" = " + userDTO.getUserId());
            } else if (userDTO.getRole().equals("Unternehmen")) {
                Statement unternehemnStatement = JDBCConnection.getInstance().getStatement();
                unternehemnStatement.executeUpdate(
                        "UPDATE collathbrs.unternehmen "
                                + "SET \"uName\" = \'" + userDTO.getCompanyName() + "\'"
                                + ", branche = \'" + userDTO.getBranche() + "\' "
                                + "WHERE \"userID\" = " + userDTO.getUserId());
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void deleteUser(int userId, String role) throws DatabaseLayerException {
        try {
            //Delete Student or Unternehmen
            Statement statement = JDBCConnection.getInstance().getStatement();
            if (role.equals("Student")) {
                statement.execute(
                        "DELETE "
                                + "FROM collathbrs.student "
                                + "WHERE \"userID\" = " + userId);
            } else if (role.equals("Unternehmen")) {
                statement.execute(
                        "DELETE "
                                + "FROM collathbrs.unternehmen "
                                + "WHERE \"userID\" = " + userId);
            }

            //Delete User
            Statement userStatement = JDBCConnection.getInstance().getStatement();
            userStatement.execute(
                    "DELETE "
                            + "FROM collathbrs.user "
                            + "WHERE \"userID\" = " + userId);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
