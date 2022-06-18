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
                    "SELECT * FROM collathbrs.stellenanzeige");

            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                StellenanzeigeDTOImpl userDTO = JobBuilder
                        .getInstance()
                        .createNewJob()
                        .withTitle(rs.getString("titel"))
                        .withBranche(rs.getString("bereich"))
                        .withDescription(rs.getString("beschreibung"))
                        .withStartDate(rs.getDate("einstellungsdatum").toLocalDate())
                        .withSalary(rs.getDouble("gehalt"))
                        .withWeeklyHours(rs.getDouble("wochenstunden"))
                        .build();

                list.add(userDTO);
            }

            return list;

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
                "INTO collathbrs.stellenanzeige (unternehmen_id, titel, bereich, beschreibung, einstellungsdatum, gehalt, wochenstunden) " +
                "VALUES (?,?,?,?,?,?,?)"
            );

            statement.setInt(1, userDTO.getUnternehmenId());
            statement.setString(2, stellenanzeigeDTO.getTitel());
            statement.setString(3, stellenanzeigeDTO.getBereich());
            statement.setString(4, stellenanzeigeDTO.getBeschreibung());
            statement.setDate(5, Date.valueOf(stellenanzeigeDTO.getEinstellungsdatum()));
            statement.setDouble(6, stellenanzeigeDTO.getGehalt());
            statement.setDouble(7, stellenanzeigeDTO.getWochenstunden());
        } catch (SQLException ex) {
            DatabaseLayerException e = new DatabaseLayerException("Probleme mit der Datenbank");
            e.setReason(Globals.Errors.DATABASE);
            throw e;
        }
    }

}
