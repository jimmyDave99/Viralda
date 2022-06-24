package org.hbrs.se2.project.hellocar.dao;

import org.hbrs.se2.builder.UserBuilder;
import org.hbrs.se2.project.hellocar.dtos.UserDTO;
import org.hbrs.se2.project.hellocar.dtos.impl.UserDTOImpl;
import org.hbrs.se2.project.hellocar.services.db.JDBCConnection;
import org.hbrs.se2.project.hellocar.services.db.exceptions.DatabaseLayerException;
import org.hbrs.se2.project.hellocar.util.Globals;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BewerbungDAO {

    /**
     * Method to find All Job application
     *
     * @return List<BewerbungDTO>
     * @throws DatabaseLayerException
     */
    public List<UserDTO> findAllJobApplicant(UserDTO userDTO) throws DatabaseLayerException {

        try {
            List<UserDTO> list = new ArrayList<>();
            PreparedStatement statement = JDBCConnection.getInstance().getPreparedStatement(
                    "SELECT collathbrs.bewerbung.stellen_id, collathbrs.bewerbung.status, " +
                            "collathbrs.stellenanzeige.unternehmer_id, " +
                            "collathbrs.bewerbung.bewerbungsdatum, collathbrs.student.nachname, " +
                            "collathbrs.student.vorname, collathbrs.user.email " +
                            "FROM collathbrs.student " +
                            "INNER JOIN collathbrs.bewerbung " +
                            "ON " +
                            "collathbrs.student.student_id=collathbrs.bewerbung.student_id " +
                            "INNER JOIN collathbrs.user " +
                            "ON collathbrs.user.id = collathbrs.student.user_id " +
                            "INNER JOIN collathbrs.stellenanzeige " +
                            "ON collathbrs.stellenanzeige.stellen_id = collathbrs.bewerbung.stellen_id " +
                            "WHERE collathbrs.stellenanzeige.unternehmer_id = ?");

            statement.setInt(1, userDTO.getUnternehmenId());
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                UserDTOImpl applicant = UserBuilder
                        .getInstance()
                        .createNewUser()
                        .withJobId(rs.getInt("stellen_id"))
                        .withFirstName(rs.getString("vorname"))
                        .withLastName(rs.getString("nachname"))
                        .withEmail(rs.getString("email"))
                        .withApplicationDate(rs.getDate("bewerbungsdatum"))
                        .withStatus(rs.getString("status"))
                        .build();

                list.add(applicant);
            }

            return list;

        } catch (SQLException ex) {
            DatabaseLayerException e = new DatabaseLayerException("Probleme mit der Datenbank");
            e.setReason(Globals.Errors.DATABASE);
            throw e;
        }

    }
}
