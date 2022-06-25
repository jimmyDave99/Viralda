package org.hbrs.se2.project.hellocar.dao;

import org.hbrs.se2.builder.JobBuilder;
import org.hbrs.se2.project.hellocar.dtos.StellenanzeigeDTO;
import org.hbrs.se2.project.hellocar.dtos.UserDTO;
import org.hbrs.se2.project.hellocar.dtos.impl.StellenanzeigeDTOImpl;
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

public class StellenAnzeigeDAO {

    /**
     * Method to find All Jobs
     *
     * @return List<StellenanzeigeDTO>
     * @throws DatabaseLayerException
     */
    public List<StellenanzeigeDTO> findAllJobs() throws DatabaseLayerException {

        try {
            List<StellenanzeigeDTO> list = new ArrayList<>();
            PreparedStatement statement = JDBCConnection.getInstance().getPreparedStatement(
                    "SELECT * FROM collathbrs.stellenanzeige " +
                            "WHERE status = 'AKTIV'");

            return getStellenanzeigeDTOS(list, statement);

        } catch (SQLException ex) {
            DatabaseLayerException e = new DatabaseLayerException("Probleme mit der Datenbank");
            e.setReason(Globals.Errors.DATABASE);
            throw e;
        }

    }

    /**
     * Method for finding a Job with stellen_id
     *
     * @param jobId
     * @return List<StellenanzeigeDTO>
     * @throws DatabaseLayerException
     */
    public List<StellenanzeigeDTO> findJobWithId(int jobId) throws DatabaseLayerException {
        try {
            List<StellenanzeigeDTO> list = new ArrayList<>();
            PreparedStatement statement = JDBCConnection.getInstance().getPreparedStatement("SELECT * " +
                    "FROM collathbrs.stellenanzeige WHERE stellen_id = ?");
            statement.setInt(1, jobId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                StellenanzeigeDTOImpl currentJob = JobBuilder
                        .getInstance()
                        .createNewJob()
                        .withStellenID(rs.getInt("stellen_id"))
                        .withTitle(rs.getString("titel"))
                        .withBranche(rs.getString("bereich"))
                        .withDescription(rs.getString("beschreibung"))
                        .withStartDate(rs.getDate("einstellungsdatum").toLocalDate())
                        .withSalary(rs.getDouble("gehalt"))
                        .withWeeklyHours(rs.getDouble("wochenstunden"))
                        .withStatus(rs.getString("status"))
                        .build();
                 list.add(currentJob);
            }

            return list;

        } catch (SQLException ex) {
            DatabaseLayerException e = new DatabaseLayerException("Probleme mit der Datenbank");
            e.setReason(Globals.Errors.DATABASE);
            throw e;
        }
    }

    /**
     * Method for updating status
     *
     * @param stellenanzeigeDTO
     * @param status
     * @return
     * @throws DatabaseLayerException
     */
    public void updateStatusByJobId(StellenanzeigeDTO stellenanzeigeDTO, UserDTO userDTO, String status) throws DatabaseLayerException {
        try {
            PreparedStatement statement = JDBCConnection.getInstance().getPreparedStatement(
                    "UPDATE collathbrs.bewerbung " +
                            "SET status = ? " +
                            "WHERE stellen_id = ? AND student_id = ?");

            statement.setString(1, status);
            statement.setInt(2, stellenanzeigeDTO.getStellenId());
            statement.setInt(3, userDTO.getStudentId());
            statement.executeUpdate();


        } catch (SQLException throwables) {
            throwables.printStackTrace();
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
            DatabaseLayerException e = new DatabaseLayerException("Probleme mit der Datenbank");
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
            DatabaseLayerException e = new DatabaseLayerException("Probleme mit der Datenbank");
            e.setReason(Globals.Errors.DATABASE);
            throw e;
        }
    }

    /**
     * Method to insert Stellenanzeige
     *
     * @param stellenanzeigeDTO
     * @param userDTO
     * @throws DatabaseLayerException
     */
    public void insertStellenanzeige(StellenanzeigeDTO stellenanzeigeDTO, UserDTO userDTO) throws DatabaseLayerException {
        try {
            PreparedStatement statement = JDBCConnection.getInstance().getPreparedStatement(
            "INSERT " +
                "INTO collathbrs.stellenanzeige (unternehmer_id, titel, bereich, beschreibung, einstellungsdatum, gehalt, wochenstunden) " +
                "VALUES (?,?,?,?,?,?,?)"
            );

            statement.setInt(1, userDTO.getUnternehmenId());
            statement.setString(2, stellenanzeigeDTO.getTitel());
            statement.setString(3, stellenanzeigeDTO.getBereich());
            statement.setString(4, stellenanzeigeDTO.getBeschreibung());
            statement.setDate(5, Date.valueOf(stellenanzeigeDTO.getEinstellungsdatum()));
            statement.setDouble(6, stellenanzeigeDTO.getGehalt());
            statement.setDouble(7, stellenanzeigeDTO.getWochenstunden());

            statement.executeUpdate();
        } catch (SQLException ex) {
            DatabaseLayerException e = new DatabaseLayerException("Probleme mit der Datenbank");
            e.setReason(Globals.Errors.DATABASE);
            throw e;
        }
    }

    /**
     * Method to find current companyJob
     *
     * @param userDTO
     * @throws DatabaseLayerException
     */
    public List<StellenanzeigeDTO> findCurrentCompanyJob(UserDTO userDTO) throws DatabaseLayerException {

        try {
            List<StellenanzeigeDTO> list = new ArrayList<>();
            PreparedStatement statement = JDBCConnection.getInstance().getPreparedStatement(
                    "SELECT * FROM collathbrs.stellenanzeige WHERE unternehmer_id = ? ");
            statement.setInt(1, userDTO.getUnternehmenId() );

            return getStellenanzeigeDTOS(list, statement);

        }catch (SQLException ex) {
            DatabaseLayerException e = new DatabaseLayerException("Probleme mit der Datenbank");
            e.setReason(Globals.Errors.DATABASE);
            throw e;
        }
    }

    /**
     * Method to update companyJob
     *
     * @param stellenanzeigeDTO
     * @throws DatabaseLayerException
     */
    public void updateCompanyJob(StellenanzeigeDTO stellenanzeigeDTO) throws DatabaseLayerException {
        try {
            PreparedStatement statement = JDBCConnection.getInstance().getPreparedStatement(
                    "UPDATE collathbrs.stellenanzeige " +
                            "SET titel = ?, bereich = ?, beschreibung = ?, einstellungsdatum = ?, gehalt = ?, wochenstunden = ? " +
                            "WHERE stellen_id = ?");

            statement.setString(1, stellenanzeigeDTO.getTitel());
            statement.setString(2, stellenanzeigeDTO.getBereich());
            statement.setString(3, stellenanzeigeDTO.getBeschreibung());
            statement.setDate(4, Date.valueOf(stellenanzeigeDTO.getEinstellungsdatum()));
            statement.setDouble(5, stellenanzeigeDTO.getGehalt());
            statement.setDouble(6, stellenanzeigeDTO.getWochenstunden());
            statement.setInt(7, stellenanzeigeDTO.getStellenId());
            statement.executeUpdate();

        } catch (SQLException ex) {
            DatabaseLayerException e = new DatabaseLayerException("Probleme mit der Datenbank");
            e.setReason(Globals.Errors.DATABASE);
            throw e;
        }
    }

    /**
     * Method for updating status job
     *
     * @param stellenanzeigeDTO
     * @param status
     * @return
     * @throws DatabaseLayerException
     */
    public void updateJobStatus(StellenanzeigeDTO stellenanzeigeDTO, String status) throws DatabaseLayerException {
        try {
            PreparedStatement statement = JDBCConnection.getInstance().getPreparedStatement(
                    "UPDATE collathbrs.stellenanzeige " +
                            "SET status = ? " +
                            "WHERE stellen_id = ?");

            statement.setString(1, status);
            statement.setInt(2, stellenanzeigeDTO.getStellenId());
            statement.executeUpdate();


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Method for deleting Announcement
     *
     * @param stellenanzeigeDTO
     * @return
     * @throws DatabaseLayerException
     */
    public void deleteJob(StellenanzeigeDTO stellenanzeigeDTO) throws DatabaseLayerException {
        try {
            PreparedStatement statement = JDBCConnection.getInstance().getPreparedStatement(
                    "DELETE FROM collathbrs.stellenanzeige " +
                            "WHERE stellen_id = ?");

            statement.setInt(1, stellenanzeigeDTO.getStellenId());
            statement.executeUpdate();


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private List<StellenanzeigeDTO> getStellenanzeigeDTOS(List<StellenanzeigeDTO> list, PreparedStatement statement) throws SQLException {
        ResultSet result = statement.executeQuery();
        while (result.next()){
            StellenanzeigeDTOImpl currentCompanyJob = JobBuilder
                    .getInstance()
                    .createNewJob()
                    .withStellenID(result.getInt("stellen_id"))
                    .withTitle(result.getString("titel"))
                    .withBranche(result.getString("bereich"))
                    .withDescription(result.getString("beschreibung"))
                    .withStartDate(result.getDate("einstellungsdatum").toLocalDate())
                    .withSalary(result.getFloat("gehalt"))
                    .withWeeklyHours(result.getFloat("wochenstunden"))
                    .withStatus(result.getString("status"))
                    .build();

            list.add(currentCompanyJob);
        }

        return list;
    }
}
