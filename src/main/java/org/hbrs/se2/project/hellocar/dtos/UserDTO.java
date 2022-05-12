package org.hbrs.se2.project.hellocar.dtos;

import org.hbrs.se2.project.hellocar.entities.Rolle;

import java.time.LocalDate;
import java.util.List;

public interface UserDTO {
    public int getId();
    public String getFirstName();
    public String getLastName();
    public List<RolleDTO> getRoles();
    public String getEmail();
    public String getPassword();
    public LocalDate getDateOfBirth();
}
