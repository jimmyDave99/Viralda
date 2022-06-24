package org.hbrs.se2.project.hellocar.dtos;

import java.util.*;

public interface BewerbungDTO {
    //getter-Methode
    public int getStudentId();
    public int getStellenId();
    public int getBewerberId();
    public String getstudentvorname();
    public String getstudentnachname();
    public String getstudentemail();
    public String getAnschreiben();
    public String getLebenslauf();
    public String getWeitereUnterlagen();
    public Date getBewerbungsdatum();
    //Setter-Methode
    public void setStudentId(int StudentId);
    public void setStellenId(int StellenId);
    public void setBewerberId(int BewerberId);
    public String setstudentvorname(String studentvorname);
    public String setstudentnachname(String studentnachname);
    public String setstudentemail(String studentemail);
    public void setAnschreiben(String Anschreiben);
    public void setLebenslauf(String Lebenslauf);
    public void setWeitereUnterlagen(String WeitereUnterlagen);
    public void setBewerbungsdatum(Date Bewerbungsdatum);

}
