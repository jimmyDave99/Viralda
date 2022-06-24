package org.hbrs.se2.builder;

import org.hbrs.se2.project.hellocar.dtos.impl.BewerbungDTOImpl;
import org.hbrs.se2.project.hellocar.dtos.impl.StellenanzeigeDTOImpl;

import java.util.Date;

public class JobApplicationBuilder {

    private BewerbungDTOImpl jobAppliDTO = null;

    public static JobApplicationBuilder getInstance() {
        return new JobApplicationBuilder();
    }

    public JobApplicationBuilder createNewJobApli() {
        jobAppliDTO = new BewerbungDTOImpl();
        return this;
    }

    public JobApplicationBuilder mitStellenID(int stellenId) {
        this.jobAppliDTO.setStellenId(stellenId);
        return this;
    }

    public JobApplicationBuilder mitBewerberId(int bewerberId ) {
        this.jobAppliDTO.setBewerberId(bewerberId);
        return this;
    }
    public JobApplicationBuilder mitStudentVorname(String studentVorname ) {
        this.jobAppliDTO.setstudentvorname(studentVorname);
        return this;
    }
    public JobApplicationBuilder mitStudentNachname(String studentNachname ) {
        this.jobAppliDTO.setstudentvorname(studentNachname);
        return this;
    }
    public JobApplicationBuilder mitStudentEmail(String studentEmail ) {
        this.jobAppliDTO.setstudentvorname(studentEmail);
        return this;
    }
    public JobApplicationBuilder mitAnschreiben(String anschreiben ) {
        this.jobAppliDTO.setAnschreiben(anschreiben);
        return this;
    }

    public JobApplicationBuilder mitLebenslauf(String lebenslauf ) {
        this.jobAppliDTO.setLebenslauf(lebenslauf);
        return this;
    }

    public JobApplicationBuilder mitWeitereUnterlagen(String weitereUnterlagen ) {
        this.jobAppliDTO.setWeitereUnterlagen(weitereUnterlagen);
        return this;
    }

    public JobApplicationBuilder mitBewerbungsdatum(Date bewerbungsdatum ) {
        this.jobAppliDTO.setBewerbungsdatum(bewerbungsdatum);
        return this;
    }


    public BewerbungDTOImpl build() {
        return this.jobAppliDTO;
    }
}
