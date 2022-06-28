package org.hbrs.se2.builder;

import org.hbrs.se2.project.hellocar.dtos.impl.UserDTOImpl;

import java.awt.image.BufferedImage;
import java.time.LocalDate;
import java.util.Date;

import static org.hbrs.se2.project.hellocar.util.Globals.Roles.UNTERNEHMEN;

public class UserBuilder {

    private UserDTOImpl userDTO = null;

    public static UserBuilder getInstance() {
        return new UserBuilder();
    }

    public UserBuilder createNewUser() {
        userDTO = new UserDTOImpl();
        return this;
    }

    public UserBuilder withUserID(int id) {
        this.userDTO.setUserId(id);
        return this;
    }

    public UserBuilder withEmail(String email) {
        this.userDTO.setEmail(email);
        return this;
    }
    public UserBuilder withStatus(String status) {
        this.userDTO.setStatus(status);
        return this;
    }
    public UserBuilder withJobId(int siteId) {
        this.userDTO.setStelleId(siteId);
        return this;
    }
    public UserBuilder withApplicationDate(Date applicationDate) {
        this.userDTO.setBewerbungsdatum(applicationDate);
        return this;
    }
    public UserBuilder withRole( String role ) {
        this.userDTO.setRole(role);
        return this;
    }

    public UserBuilder withPassword( String password ) {
        this.userDTO.setPassword(password);
        return this;
    }

    public UserBuilder withConfirmPassword( String confirmPassword ) {
        this.userDTO.setConfirmPassword(confirmPassword);
        return this;
    }

    public UserBuilder withProfilePicture( BufferedImage profilePicture ) {
        this.userDTO.setProfilePicture(profilePicture);
        return this;
    }

    public UserBuilder withFaculty( String faculty ){
        this.userDTO.setFaculty(faculty);
        return this;
    }

    public UserBuilder withSemester( int semester ){
        this.userDTO.setSemester(semester);
        return this;
    }

    public UserBuilder withSpecialization( String specialization ){
        this.userDTO.setSpecialization(specialization);
        return this;
    }

    public UserBuilder withDateOfBirth( LocalDate dateOfBirth ){
        this.userDTO.setDateOfBirth(dateOfBirth);
        return this;
    }

    public UserBuilder withPlz( int plz ) {
        this.userDTO.setPlz(plz);
        return this;
    }

    public UserBuilder withLand( String land ) {
        this.userDTO.setLand(land);
        return this;
    }

    public UserBuilder withStadt( String stadt ) {
        this.userDTO.setStadt(stadt);
        return this;
    }

    public UserBuilder withStrasse( String strasse ) {
        this.userDTO.setStrasse(strasse);
        return this;
    }

    public UserBuilder withHausnummer( int hausnummer ) {
        this.userDTO.setHausnummer(hausnummer);
        return this;
    }

    public UserBuilder withStudentID( int studentID ) {
        this.userDTO.setStudentId(studentID);
        return this;
    }

    public UserBuilder withFirstName(String firstName){
        this.userDTO.setFirstName(firstName);
        return this;
    }

    public UserBuilder withLastName(String lastName){
        this.userDTO.setLastName(lastName);
        return this;
    }

    public UserBuilder withUnternehmenID( int unternehmenID ) {
        this.userDTO.setUnternehmenId(unternehmenID);
        return this;
    }

    public UserBuilder withCompanyName( String companyName ) {
        this.userDTO.setCompanyName(companyName);
        return this;
    }

    public UserBuilder withBranche( String branche ) {
        this.userDTO.setBranche(branche);
        return this;
    }

    public UserBuilder withDescription( String description ) {
        this.userDTO.setDescription(description);
        return this;
    }


    // methods for more details from an employment
    public UserBuilder withTitle( String title) {
        this.userDTO.setTitle(title);
        return this;
    }

    public UserBuilder withDateOfDeployment(Date dateOfDeployment) {
        this.userDTO.setDateOfDeployment(dateOfDeployment);
        return this;
    }

    public UserBuilder withSalary(int salary) {
        this.userDTO.setSalary(salary);
        return this;
    }

    public UserBuilder withHoursPerWeek(int hoursPerWeek) {
        this.userDTO.setHoursPerWeek(hoursPerWeek);
        return this;
    }

    public UserBuilder withEmploymentDescription( String employmentDescription) {
        this.userDTO.setEmploymentDescription(employmentDescription);
        return this;
    }




    public UserBuilder createDefaultUserCompany() {
        userDTO = new UserDTOImpl();
        this.userDTO.setRole(UNTERNEHMEN);
        this.userDTO.setEmail("viralda@company.de");
        this.userDTO.setCompanyName("viralda");
        this.userDTO.setBranche("IT");
        this.userDTO.setPassword("2A3Ene3vf");
        this.userDTO.setConfirmPassword("2A3Ene3vf");
        this.userDTO.setDescription("...");
        this.userDTO.setUnternehmenId(82);
        return this;
    }

    public UserDTOImpl build() {
        return this.userDTO;
    }

}

