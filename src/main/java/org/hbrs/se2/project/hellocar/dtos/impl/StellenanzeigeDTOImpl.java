package org.hbrs.se2.project.hellocar.dtos.impl;

import org.hbrs.se2.project.hellocar.dtos.StellenanzeigeDTO;

import java.awt.image.BufferedImage;
import java.time.LocalDate;
import java.util.Date;
// TODO: Eventuell eine weitere Variable um ein Firmenlogo/Bild abzuspeichern
// TODO: 15.06.22 Eine weitere Variable fuer den Status der Stellenanzeige (Aktiv/Inaktiv) 
public class StellenanzeigeDTOImpl implements StellenanzeigeDTO {

    private int stellenId;
    private int unternehmenId;
    private String titel;
    private String bereich;
    private String beschreibung;
    private LocalDate einstellungsdatum;
    private double gehalt;
    private double wochenstunden;


    @Override
    public int getStellenId() {
        return stellenId;
    }

    @Override
    public void setStellenId(int stellenId) {
        this.stellenId = stellenId;
    }

    @Override
    public int getUnternehmenId() {
        return unternehmenId;
    }

    @Override
    public void setUnternehmenId(int unternehmenId) {
        this.unternehmenId = unternehmenId;
    }

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
    public LocalDate getEinstellungsdatum() {
        return einstellungsdatum;
    }

    @Override
    public void setEinstellungsdatum(LocalDate einstellungsdatum) {
        this.einstellungsdatum = einstellungsdatum;
    }

    @Override
    public double getGehalt() {
        return gehalt;
    }

    @Override
    public void setGehalt(double gehalt) {
        this.gehalt = gehalt;
    }

    @Override
    public double getWochenstunden() {
        return wochenstunden;
    }

    @Override
    public void setWochenstunden(double wochenstunden) {
        this.wochenstunden = wochenstunden;
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
