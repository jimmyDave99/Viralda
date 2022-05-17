package org.hbrs.se2.project.hellocar.dtos;

import java.awt.image.BufferedImage;
import java.time.LocalDate;
import java.util.List;

public interface UserDTO {
    //Setter
    void setUserId(int userId);
    void setEmail(String email);
    void setRole(String role);
    void setProfilePicture(BufferedImage profilePicture);
    void setPlz(int plz);
    void setLand(String land);
    void setStadt(String stadt);
    void setStrasse(String strasse);
    void setNummer(int nummer);
    void setStudentId(int studentId);
    void setFirstName(String firstName);
    void setLastName(String lastName);
    void setUnternehmenId(int unternehmenId);
    void setUnternehmenName(String unternehmenName);
    void setBranche(String branche);
    void setDescription(String description);

    //Getter
    int getUserId();
    String getEmail();
    String getRole();
    BufferedImage getProfilePicture();
    int getPlz();
    String getLand();
    String getStadt();
    String getStrasse();
    int getNummer();
    int getStudentId();
    String getFirstName();
    String getLastName();
    int getUnternehmenId();
    String getUnternehmenName();
    String getBranche();
    String getDescription();
}
