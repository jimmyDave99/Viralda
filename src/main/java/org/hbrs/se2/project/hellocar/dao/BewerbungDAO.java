package org.hbrs.se2.project.hellocar.dao;

import org.hbrs.se2.builder.UserBuilder;
import org.hbrs.se2.project.hellocar.dtos.StellenanzeigeDTO;
import org.hbrs.se2.project.hellocar.dtos.UserDTO;
import org.hbrs.se2.project.hellocar.dtos.impl.UserDTOImpl;
import org.hbrs.se2.project.hellocar.services.db.JDBCConnection;
import org.hbrs.se2.project.hellocar.services.db.exceptions.DatabaseLayerException;
import org.hbrs.se2.project.hellocar.util.Globals;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hbrs.se2.project.hellocar.util.Globals.Errors.PROBLEM;

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
                            "collathbrs.student.vorname, collathbrs.student.student_id,  collathbrs.user.email, " +
                            // ToDo:
                            // Ergänzungen für die LandingPageCompanyView mehr Details von Student
                            "collathbrs.student.fachbereich, collathbrs.student.semester, collathbrs.student.spezialisierung, " +
                            // mehr Details von Stellenazeige
                            "collathbrs.stellenanzeige.titel, collathbrs.stellenanzeige.einstellungsdatum, collathbrs.stellenanzeige.gehalt, collathbrs.stellenanzeige.wochenstunden, " +
                            // mehr Details von unser
                            "collathbrs.user.beschreibung " +
                            // Ende Ergänzungen
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
                        .withStudentID(rs.getInt("student_id"))
                        .withFirstName(rs.getString("vorname"))
                        .withLastName(rs.getString("nachname"))
                        .withEmail(rs.getString("email"))
                        .withApplicationDate(rs.getDate("bewerbungsdatum"))
                        .withStatus(rs.getString("status"))
                        // ToDo: falls Fehler sind
                        .withFaculty(rs.getString("fachbereich"))
                        .withSemester(Integer.parseInt(rs.getString("semester")))
                        .withSpecialization(rs.getString("spezialisierung"))
                        .withDescription(rs.getString("beschreibung"))
                        .build();

                list.add(applicant);
            }

            return list;

        } catch (SQLException ex) {
            DatabaseLayerException e = new DatabaseLayerException(PROBLEM);
            e.setReason(Globals.Errors.DATABASE);
            throw e;
        }
    }

    /**
     * Method for updating status
     *
     * @param stellenId
     * @param studentId
     * @param status
     * @return
     * @throws DatabaseLayerException
     */
    public void updateStatusByJobId(int stellenId, int studentId, String status) throws DatabaseLayerException {
        try {
            PreparedStatement statement = JDBCConnection.getInstance().getPreparedStatement(
                    "UPDATE collathbrs.bewerbung " +
                            "SET status = ? " +
                            "WHERE stellen_id = ? AND student_id = ?");

            statement.setString(1, status);
            statement.setInt(2, stellenId);
            statement.setInt(3, studentId);
            statement.executeUpdate();


        } catch (SQLException ex) {
            DatabaseLayerException e = new DatabaseLayerException(PROBLEM);
            e.setReason(Globals.Errors.DATABASE);
            throw e;
        }
    }

    /**
     * Method for finding Job status
     *
     * @param stellenanzeigeDTO
     * @param userDTO
     * @return
     * @throws DatabaseLayerException
     */
    public String findStatusByJobIdAndStudentId(StellenanzeigeDTO stellenanzeigeDTO, UserDTO userDTO) throws DatabaseLayerException {
        try {
            String status = "";
            PreparedStatement statement = JDBCConnection.getInstance().getPreparedStatement(
                    "SELECT status FROM collathbrs.bewerbung WHERE stellen_id = ? AND student_id = ?");
            statement.setInt(1, stellenanzeigeDTO.getStellenId());
            statement.setInt(2, userDTO.getStudentId());
            ResultSet rs = statement.executeQuery();
            while (rs.next())
                status = rs.getString(1);

            return status;

        } catch (SQLException ex) {
            DatabaseLayerException e = new DatabaseLayerException(PROBLEM);
            e.setReason(Globals.Errors.DATABASE);
            throw e;
        }
    }

    /**
     * Method to insert job Application
     *
     * @param stellenanzeigeDTO
     * @param userDTO
     * @param status
     * @throws DatabaseLayerException
     */
    public void insertOrUpdateJobApplication(StellenanzeigeDTO stellenanzeigeDTO, UserDTO userDTO, String status) throws DatabaseLayerException {
        try {
            PreparedStatement statement = JDBCConnection.getInstance().getPreparedStatement(
                    "UPDATE collathbrs.bewerbung " +
                            "SET status = ? " +
                            "WHERE student_id = ? AND stellen_id = ?; " +
                            "INSERT " +
                            "INTO collathbrs.bewerbung (student_id, stellen_id, bewerbungsdatum, status ) " +
                            "SELECT ?, ?, ?, ? " +
                            "WHERE NOT EXISTS " +
                            "(SELECT student_id, stellen_id FROM collathbrs.bewerbung WHERE student_id = ? AND stellen_id = ?)" );

            statement.setString(1, status);
            statement.setInt(2, userDTO.getStudentId());
            statement.setInt(3, stellenanzeigeDTO.getStellenId());
            statement.setInt(4, userDTO.getStudentId());
            statement.setInt(5, stellenanzeigeDTO.getStellenId());
            statement.setDate(6, Date.valueOf(LocalDate.now()));
            statement.setString(7, status);
            statement.setInt(8, userDTO.getStudentId());
            statement.setInt(9, stellenanzeigeDTO.getStellenId());

            statement.executeUpdate();
        } catch (SQLException ex) {
            DatabaseLayerException e = new DatabaseLayerException(PROBLEM);
            e.setReason(Globals.Errors.DATABASE);
            throw e;
        }
    }
}
