package org.hbrs.se2.project.hellocar.dtos.impl;

import org.hbrs.se2.project.hellocar.dtos.RolleDTO;
import org.hbrs.se2.project.hellocar.dtos.UserDTO;

import java.awt.image.BufferedImage;
import java.time.LocalDate;
import java.util.List;

public class UserDTOImpl implements UserDTO {

    //User
    private int userId;
    private String email;
    private List<RolleDTO> roles;
    private BufferedImage profilePicture;

    //Useranschrift
    private int plz;
    private String land;
    private String stadt;
    private String strasse;
    private int nummer;

    //Student
    private int studentId;
    private String firstName;
    private String lastName;

    //Unternehmen
    private int unternehmenId;
    private String unternehmenName;
    private String branche;
    private String description;

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
    public void setRoles(List<RolleDTO> roles) {
        this.roles = roles;
    }

    @Override
    public void setProfilePicture(BufferedImage profilePicture) {
        this.profilePicture = profilePicture;
    }

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
    public void setNummer(int nummer) {
        this.nummer = nummer;
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
    public void setUnternehmenName(String unternehmenName) {
        this.unternehmenName = unternehmenName;
    }

    @Override
    public void setBranche(String branche) {
        this.branche = branche;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

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
    public List<RolleDTO> getRoles() {
        return this.roles;
    }

    @Override
    public BufferedImage getProfilePicture() {
        return profilePicture;
    }

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
    public int getNummer() {
        return nummer;
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
    public String getUnternehmenName() {
        return unternehmenName;
    }

    @Override
    public String getBranche() {
        return branche;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "UserId: " + userId +
                ", FirstName: " + firstName +
                ", LastName: " + lastName;
    }
}
