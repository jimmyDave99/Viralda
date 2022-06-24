package org.hbrs.se2.project.hellocar.dtos.impl;

import org.hbrs.se2.project.hellocar.dtos.BewerbungDTO;

import java.awt.image.BufferedImage;
import java.util.Date;
// TODO: Eventuell eine weitere Variable um ein Firmenlogo/Bild abzuspeichern
public class BewerbungDTOImpl implements BewerbungDTO {

    private int studentId;
    private int stellenId;
    private int bewerberId;
    private String anschreiben;
    private String lebenslauf;
    private String weitereUnterlagen;
    private Date bewerbungsdatum;


    @Override
    public int getStudentId(){
        return studentId;
    }

    @Override
    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    @Override
    public int getStellenId() {
        return stellenId;
    }

    @Override
    public void setStellenId(int stellenId) {
        this.stellenId = stellenId;
    }

    @Override
    public int getBewerberId() {
        return bewerberId;
    }

    @Override
    public void setBewerberId(int bewerberId) {
        this.bewerberId = bewerberId;
    }

    @Override
    public String getAnschreiben() {
        return anschreiben;
    }

    @Override
    public void setAnschreiben(String anschreiben) {
        this.anschreiben = anschreiben;
    }

    @Override
    public String getLebenslauf() {
        return lebenslauf;
    }

    @Override
    public void setLebenslauf(String lebenslauf) {
        this.lebenslauf = lebenslauf;
    }

    @Override
    public String getWeitereUnterlagen() {
        return weitereUnterlagen;
    }

    @Override
    public void setWeitereUnterlagen(String weitereUnterlagen) {
        this.weitereUnterlagen = weitereUnterlagen;
    }

    @Override
    public Date getBewerbungsdatum() {
        return bewerbungsdatum;
    }

    @Override
    public void setBewerbungsdatum(Date bewerbungsdatum) {
        this.bewerbungsdatum = bewerbungsdatum;
    }

    @Override
    public String toString() {
        return "Studenten ID: " + studentId +
                ", Stellen ID: " + stellenId +
                ", Bewerber ID: " + bewerberId +
                ", Anschreiben: " + anschreiben +
                ", Lebenslauf: " + lebenslauf +
                ", Weitere Unterlagen: " + weitereUnterlagen +
                ", Bewerbungsdatum: " + bewerbungsdatum;
    }
}


