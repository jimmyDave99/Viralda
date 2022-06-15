package org.hbrs.se2.project.hellocar.dtos;

import java.util.Date;

public interface StellenanzeigeDTO {
    // getter-Methoden
    public String getTitel();
    public String getBereich();
    public String getBeschreibung();
    public Date getEinstellungsdatum();
    public float getGehalt();
    public float getWochenstunden();

    // setter-Methoden
    public void setTitel(String titel);
    public void  setBereich(String bereich);
    public void  setBeschreibung(String beschreibung);
    public void  setEinstellungsdatum(Date einstellungsdatum);
    public void  setGehalt(float gehalt);
    public void  setWochenstunden(float wochenstunden);
}
