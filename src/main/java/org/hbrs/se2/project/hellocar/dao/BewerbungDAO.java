package org.hbrs.se2.project.hellocar.dao;

import ch.qos.logback.core.net.SyslogOutputStream;
import org.hbrs.se2.builder.JobApplicationBuilder;
import org.hbrs.se2.builder.JobBuilder;
import org.hbrs.se2.project.hellocar.dtos.BewerbungDTO;
import org.hbrs.se2.project.hellocar.dtos.StellenanzeigeDTO;
import org.hbrs.se2.project.hellocar.dtos.impl.BewerbungDTOImpl;
import org.hbrs.se2.project.hellocar.dtos.impl.StellenanzeigeDTOImpl;
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
    public List<BewerbungDTO> findAllJobApplication() throws DatabaseLayerException {

        try {
            List<BewerbungDTO> list = new ArrayList<>();
            PreparedStatement statement = JDBCConnection.getInstance().getPreparedStatement(
                    "SELECT collathbrs.bewerbung.stellen_id, collathbrs.bewerbung.status, " +
                            "collathbrs.bewerbung.bewerbungsdatum, collathbrs.student.nachname, " +
                            "collathbrs.student.vorname, collathbrs.user.email FROM collathbrs.student " +
                            "INNER JOIN collathbrs.bewerbung " +
                            "ON " +
                            "collathbrs.student.student_id=collathbrs.bewerbung.student_id " +
                            "LEFT JOIN " +
                            "collathbrs.user " +
                            "ON collathbrs.user.id = collathbrs.student.user_id");

            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                BewerbungDTOImpl userDTO = JobApplicationBuilder
                        .getInstance()
                        .createNewJobApli()
                        .mitStellenID(rs.getInt("stellen_id"))
                        .mitBewerbungsdatum(rs.getDate("bewerbungsdatum"))
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
}
