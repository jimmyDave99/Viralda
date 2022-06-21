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
                    "SELECT * FROM collathbrs.bewerbung");

            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                BewerbungDTOImpl userDTO = JobApplicationBuilder
                        .getInstance()
                        .createNewJobApli()
                        .mitStellenID(rs.getInt("stellen_id"))
                        .mitBewerberId(rs.getInt("bewerber_id"))
                        .mitAnschreiben(rs.getString("anschreiben"))
                        .mitLebenslauf(rs.getString("lebenslauf"))
                        .mitWeitereUnterlagen(rs.getString("weitere_unterlagen"))
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
