package org.hbrs.se2.project.hellocar.dtos;

import java.awt.image.BufferedImage;
import java.time.LocalDate;
import java.util.Date;

public interface UserDTO {
    //Setter
    void setUserId(int userId);
    void setEmail(String email);
    void setStatus(String status);
    void setStelleId(int stelleId);
    void setBewerbungsdatum(Date bewerbungsdatum);
    void setRole(String role);
    void setPassword(String password);
    void setConfirmPassword(String confirmPassword);
    void setProfilePicture(BufferedImage profilePicture);
    void setDateOfBirth(LocalDate dateOfBirth);
    void setFaculty(String faculty);
    void setSemester(int semester);
    void setSpecialization(String specialization);
    void setPlz(int plz);
    void setLand(String land);
    void setStadt(String stadt);
    void setStrasse(String strasse);
    void setHausnummer(int nummer);
    void setStudentId(int studentId);
    void setFirstName(String firstName);
    void setLastName(String lastName);
    void setUnternehmenId(int unternehmenId);
    void setCompanyName(String companyName);
    void setBranche(String branche);
    void setDescription(String description);

    //Getter
    int getUserId();
    String getEmail();
    String getStatus();
    int getStelleId();
    Date getbewerbungsDatum();
    String getRole();
    String getPassword();
    String getConfirmPassword();
    BufferedImage getProfilePicture();
    LocalDate getDateOfBirth();
    String getFaculty();
    int getSemester();
    String getSpecialization();
    int getPlz();
    String getLand();
    String getStadt();
    String getStrasse();
    int getHausnummer();
    int getStudentId();
    String getFirstName();
    String getLastName();
    int getUnternehmenId();
    String getCompanyName();
    String getBranche();
    String getDescription();
}
