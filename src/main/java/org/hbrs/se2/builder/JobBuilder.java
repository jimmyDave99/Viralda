package org.hbrs.se2.builder;

import org.hbrs.se2.project.hellocar.dtos.impl.StellenanzeigeDTOImpl;

import java.time.LocalDate;
import java.util.Date;

public class JobBuilder {

    private StellenanzeigeDTOImpl jobDTO = null;

    public static JobBuilder getInstance() {
        return new JobBuilder();
    }

    public JobBuilder createNewJob() {
        jobDTO = new StellenanzeigeDTOImpl();
        return this;
    }

    public JobBuilder withStellenID(int stellenID) {
        this.jobDTO.setStellenId(stellenID);
        return this;
    }

    public JobBuilder withUnternehmenID(int unternehmenID) {
        this.jobDTO.setUnternehmenId(unternehmenID);
        return this;
    }

    public JobBuilder withTitle(String title) {
        this.jobDTO.setTitel(title);
        return this;
    }

    public JobBuilder withSalary(double salary ) {
        this.jobDTO.setGehalt(salary);
        return this;
    }

    public JobBuilder withWeeklyHours(double weeklyHours ) {
        this.jobDTO.setWochenstunden(weeklyHours);
        return this;
    }

    public JobBuilder withStartDate(LocalDate startDate ) {
        this.jobDTO.setEinstellungsdatum(startDate);
        return this;
    }

    public JobBuilder withBranche(String branche ) {
        this.jobDTO.setBereich(branche);
        return this;
    }

    public JobBuilder withDescription(String description ) {
        this.jobDTO.setBeschreibung(description);
        return this;
    }

    public StellenanzeigeDTOImpl build() {
        return this.jobDTO;
    }

}

