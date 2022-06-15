package org.hbrs.se2.project.hellocar.dtos.impl;

import org.hbrs.se2.project.hellocar.dtos.StellenanzeigeDTO;

import java.awt.image.BufferedImage;
import java.util.Date;
// TODO: Eventuell eine weitere Variable um ein Firmenlogo/Bild abzuspeichern
// TODO: 15.06.22 Eine weitere Variable fuer den Status der Stellenanzeige (Aktiv/Inaktiv) 
public class StellenanzeigeDTOImpl implements StellenanzeigeDTO {

    private String titel;
    private String bereich;
    private String beschreibung;
    private Date einstellungsdatum;
    private float gehalt;
    private float wochenstunden;


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
    public String toString() {
        return "Titel: " + titel +
                ", Bereich: " + bereich +
                ", Beschreibung: " + beschreibung +
                ", Einstellungsdatum: " + einstellungsdatum +
                ", Gehalt: " + gehalt +
                ", Wochenstunden: " + wochenstunden;
    }
}
