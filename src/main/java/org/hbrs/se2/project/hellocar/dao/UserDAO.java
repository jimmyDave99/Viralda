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


// TODO: CRUD komplett umsetzten, vorallem aber Delete
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
            PreparedStatement statement = JDBCConnection.getInstance().getPreparedStatement("INSERT " +
                    "INTO collathbrs.user (email, passwort, rolle) VALUES (?,?,?)");

            statement.setString(1, userDTO.getEmail());
            statement.setString(2, password);
            statement.setString(3, userDTO.getRole());

            statement.executeUpdate();

            if (userDTO.getRole().equals("Student")) {

                PreparedStatement studentStatement = JDBCConnection.getInstance().getPreparedStatement("INSERT " +
                        "INTO collathbrs.student (user_id, vorname, nachname) VALUES (?,?,?)");

                studentStatement.setInt(1, getUserIdByEmail(userDTO));
                studentStatement.setString(2, userDTO.getFirstName());
                studentStatement.setString(3, userDTO.getLastName());

                studentStatement.executeUpdate();

            } else if (userDTO.getRole().equals("Unternehmen")) {
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

}
