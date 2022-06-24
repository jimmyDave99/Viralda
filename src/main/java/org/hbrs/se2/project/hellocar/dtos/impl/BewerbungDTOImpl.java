package org.hbrs.se2.project.hellocar.dtos.impl;

import org.hbrs.se2.project.hellocar.dtos.BewerbungDTO;

import java.util.Date;
// TODO: Eventuell eine weitere Variable um ein Firmenlogo/Bild abzuspeichern
public class BewerbungDTOImpl implements BewerbungDTO {

    private int studentId;
    private int stellenId;
    private int bewerberId;
    private String studentVorname;
    private String studentnachname;
    private String studentemail;
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
    public String getstudentvorname() {
        return studentVorname;
    }

    @Override
    public String getstudentnachname() {
        return studentnachname;
    }

    @Override
    public String getstudentemail() {
        return studentemail;
    }

    @Override
    public void setBewerberId(int bewerberId) {
        this.bewerberId = bewerberId;
    }

    @Override
    public String setstudentvorname(String studentvorname) {
        return null;
    }

    @Override
    public String setstudentnachname(String studentnachname) {
        return null;
    }

    @Override
    public String setstudentemail(String studentemail) {
        return null;
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


