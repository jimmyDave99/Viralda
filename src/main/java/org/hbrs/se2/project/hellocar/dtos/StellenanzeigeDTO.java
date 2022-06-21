package org.hbrs.se2.project.hellocar.dtos;

import java.time.LocalDate;
import java.util.Date;

public interface StellenanzeigeDTO {
    // getter-Methoden
    public int getStellenId();
    public int getUnternehmenId();
    public String getTitel();
    public String getBereich();
    public String getBeschreibung();
    public LocalDate getEinstellungsdatum();
    public double getGehalt();
    public double getWochenstunden();
    public String getStatus();

    // setter-Methoden
    public void setStellenId(int stellenId);
    public void setUnternehmenId(int unternehmenId);
    public void setTitel(String titel);
    public void  setBereich(String bereich);
    public void  setBeschreibung(String beschreibung);
    public void  setEinstellungsdatum(LocalDate einstellungsdatum);
    public void  setGehalt(double gehalt);
    public void  setWochenstunden(double wochenstunden);
    public void setStatus(String status);
}
