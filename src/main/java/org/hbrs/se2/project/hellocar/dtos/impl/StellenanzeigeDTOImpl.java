package org.hbrs.se2.project.hellocar.dtos.impl;

import org.hbrs.se2.project.hellocar.dtos.StellenanzeigeDTO;

import java.util.Date;
// TODO: Eventuell eine weitere Variable um ein Firmenlogo/Bild abzuspeichern
public class StellenanzeigeDTOImpl implements StellenanzeigeDTO {

    private String titel;
    private String bereich;
    private String beschreibung;
    private Date einstellungsdatum;
    private String status;
    private float gehalt;
    private float wochenstunden;
    private int jobId;


    @Override
    public String getTitel() {
        return titel;
    }

    @Override
    public void setTitel(String titel) {
        this.titel = titel;
    }

    @Override
    public String getBereich() {
        return bereich;
    }

    @Override
    public void setBereich(String bereich) {
        this.bereich = bereich;
    }

    @Override
    public String getBeschreibung() {
        return beschreibung;
    }

    @Override
    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    @Override
    public Date getEinstellungsdatum() {
        return einstellungsdatum;
    }

    @Override
    public void setEinstellungsdatum(Date einstellungsdatum) {
        this.einstellungsdatum = einstellungsdatum;
    }

    @Override
    public float getGehalt() {
        return gehalt;
    }

    @Override
    public void setGehalt(float gehalt) {
        this.gehalt = gehalt;
    }

    @Override
    public float getWochenstunden() {
        return wochenstunden;
    }

    @Override
    public void setWochenstunden(float wochenstunden) {
        this.wochenstunden = wochenstunden;
    }

    @Override
    public int getJobId() {
        return this.jobId;
    }

    @Override
    public String getStatus() {
        return this.status;
    }

    @Override
    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    @Override
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Titel: " + titel +
                ", Bereich: " + bereich +
                ", Beschreibung: " + beschreibung +
                ", Einstellungsdatum: " + einstellungsdatum +
                ", Gehalt: " + gehalt +
                ", Wochenstunden: " + wochenstunden;
    }
}
