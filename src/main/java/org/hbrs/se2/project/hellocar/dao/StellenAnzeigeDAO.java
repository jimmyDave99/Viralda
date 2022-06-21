package org.hbrs.se2.project.hellocar.dao;

import org.hbrs.se2.builder.JobBuilder;
import org.hbrs.se2.project.hellocar.dtos.StellenanzeigeDTO;
import org.hbrs.se2.project.hellocar.dtos.impl.StellenanzeigeDTOImpl;
import org.hbrs.se2.project.hellocar.services.db.JDBCConnection;
import org.hbrs.se2.project.hellocar.services.db.exceptions.DatabaseLayerException;
import org.hbrs.se2.project.hellocar.util.Globals;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
                    "SELECT * FROM collathbrs.stellenanzeige");

            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                StellenanzeigeDTOImpl job = JobBuilder
                        .getInstance()
                        .createNewJob()
                        .withJobId(rs.getInt("stellen_id"))
                        .withTitle(rs.getString("titel"))
                        .withBranche(rs.getString("bereich"))
                        .withDescription(rs.getString("beschreibung"))
                        .withStartDate(rs.getDate("einstellungsdatum"))
                        .withSalary(rs.getFloat("gehalt"))
                        .withWeeklyHours(rs.getFloat("wochenstunden"))
                        .withStatus(rs.getString("status"))
                        .build();

                list.add(job);
            }

            return list;

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
                        .withTitle(rs.getString("titel"))
                        .withBranche(rs.getString("bereich"))
                        .withDescription(rs.getString("beschreibung"))
                        .withStartDate(rs.getDate("einstellungsdatum"))
                        .withSalary(rs.getFloat("gehalt"))
                        .withWeeklyHours(rs.getFloat("wochenstunden"))
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
     * @param jobId
     * @param status
     * @return
     * @throws DatabaseLayerException
     */
    public void updateStatusByJobId(int jobId, String status) throws DatabaseLayerException {
        try {
            //Update Status
            PreparedStatement statement = JDBCConnection.getInstance().getPreparedStatement(
                    "UPDATE collathbrs.stellenanzeige " +
                            "SET status = ? " +
                            "WHERE stellen_id = ?");
            statement.setString(1, status);
            statement.setInt(2, jobId);
            statement.executeUpdate();


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
