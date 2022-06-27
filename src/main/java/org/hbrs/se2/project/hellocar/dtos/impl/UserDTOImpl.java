package org.hbrs.se2.project.hellocar.dtos.impl;

import org.hbrs.se2.project.hellocar.dtos.UserDTO;

import java.awt.image.BufferedImage;
import java.time.LocalDate;
import java.util.Date;

public class UserDTOImpl implements UserDTO {

    //User
    private int userId;
    private String email;
    private String role;
    private String password;
    private String confirmPassword;
    private BufferedImage profilePicture;
    private String faculty;
    private int semester;
    private String specialization;

    //Useranschrift
    private int plz;
    private String land;
    private String stadt;
    private String strasse;
    private int hausnummer;

    //Student
    private int studentId;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;

    //Unternehmen
    private int unternehmenId;
    private String companyName;
    private String branche;
    private String description;

    //Stelle
    private String status;
    private int stelleId;
    private Date bewerbungsdatum;
    private String title;
    private Date dateOfDeployment;
    private int salary;
    private int hoursPerWeek;

    //Setter
    @Override
    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public void setStatus(String status) {
        this.status = status;
    }
    @Override
    public void setStelleId(int stelleId) {
        this.stelleId = stelleId;
    }

    @Override
    public void setBewerbungsdatum(Date bewerbungsdatum) {
        this.bewerbungsdatum = bewerbungsdatum;
    }


    @Override
    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public void setPassword(String password){ this.password = password; }

    @Override
    public void setConfirmPassword(String confirmPassword){ this.confirmPassword = confirmPassword; }

    @Override
    public void setProfilePicture(BufferedImage profilePicture) {
        this.profilePicture = profilePicture;
    }

    @Override
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    @Override
    public void setFaculty(String faculty){ this.faculty = faculty; }

    @Override
    public void setSemester(int semester) { this.semester = semester; }

    @Override
    public void setSpecialization(String specialization){ this.specialization = specialization; }

    @Override
    public void setPlz(int plz) {
        this.plz = plz;
    }

    @Override
    public void setLand(String land) {
        this.land = land;
    }

    @Override
    public void setStadt(String stadt) {
        this.stadt = stadt;
    }

    @Override
    public void setStrasse(String strasse) {
        this.strasse = strasse;
    }

    @Override
    public void setHausnummer(int hausnummer) {
        this.hausnummer = hausnummer;
    }

    @Override
    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    @Override
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public void setUnternehmenId(int unternehmenId) {
        this.unternehmenId = unternehmenId;
    }

    @Override
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @Override
    public void setBranche(String branche) {
        this.branche = branche;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }


    // Setter details application
    @Override
    public void setTitle(String title) { this.title = title; }

    @Override
    public void setDateOfDeployment(Date dateOfDeployment) { this.dateOfDeployment = dateOfDeployment; }

    @Override
    public void setSalary(int salary) { this.salary = salary; }

    @Override
    public void setHoursPerWeek(int hoursPerWeek) { this.hoursPerWeek = hoursPerWeek; }




    //Getter
    @Override
    public int getUserId() {
        return userId;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public int getStelleId() {
        return stelleId;
    }

    @Override
    public Date getbewerbungsDatum() {
        return bewerbungsdatum;
    }

    @Override
    public String getRole() {
        return this.role;
    }

    @Override
    public String getPassword(){ return this.password; }

    @Override
    public String getConfirmPassword(){ return this.confirmPassword; }

    @Override
    public BufferedImage getProfilePicture() {
        return profilePicture;
    }

    @Override
    public LocalDate getDateOfBirth() { return dateOfBirth; }

    @Override
    public String getFaculty() {return faculty;}

    @Override
    public int getSemester(){return semester;}

    @Override
    public String getSpecialization(){return specialization;}

    @Override
    public int getPlz() {
        return plz;
    }

    @Override
    public String getLand() {
        return land;
    }

    @Override
    public String getStadt() {
        return stadt;
    }

    @Override
    public String getStrasse() {
        return strasse;
    }

    @Override
    public int getHausnummer() {
        return hausnummer;
    }

    @Override
    public int getStudentId() {
        return studentId;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public int getUnternehmenId() {
        return unternehmenId;
    }

    @Override
    public String getCompanyName() {
        return companyName;
    }

    @Override
    public String getBranche() {
        return branche;
    }

    @Override
    public String getDescription() {
        return description;
    }

    // Getter details application
    @Override
    public String getTitle() {return  title;}

    @Override
    public Date getDateOfDeployment() { return dateOfDeployment; }

    @Override
    public int getSalary() { return salary; }

    @Override
    public int getHoursPerWeek() { return hoursPerWeek; }

    @Override
    public String toString() {
        return "StelleID: " + stelleId +
                "UserId: " + userId +
                ", FirstName: " + firstName +
                ", LastName: " + lastName +
                ", email: " + email +
                ", bewerbungsdatum: " + bewerbungsdatum +
                ", status: " + status;
    }
}
